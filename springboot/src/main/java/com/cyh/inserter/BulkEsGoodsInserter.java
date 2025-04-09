package com.cyh.inserter;

import com.cyh.entity.Goods;
import com.cyh.mapper.GoodsMapper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BulkEsGoodsInserter {

    @Resource
    private final GoodsMapper goodsMapper;  // MyBatis Mapper
    @Resource
    private final ElasticsearchOperations elasticsearchOperations;

    // 分页大小（根据内存调整，建议 5000~10000）
    private static final int BATCH_SIZE = 8000;
    // 线程池（IO密集型任务，线程数=CPU核心数*2）
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public void syncLargeDataToEs() {
        // 1. 获取最大ID范围
        int maxId = 1000001;
        int minId = 1;

        // 2. 按ID范围分片
        List<Future<?>> futures = new ArrayList<>();
        for (int startId = minId; startId <= maxId; startId += BATCH_SIZE) {
            int endId = Math.min(startId + BATCH_SIZE - 1, maxId);
            int finalStartId = startId;
            futures.add(executor.submit(() -> {
                try {
                    processBatch(finalStartId, endId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        // 3. 等待所有任务完成
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("数据同步任务异常", e);
            }
        });

        executor.shutdown();
    }

    private void processBatch(int startId, int endId) throws InterruptedException {
        // 1. 从MySQL批量读取（基于ID范围）
        List<Goods> goodsList = goodsMapper.selectByIdRange(startId, endId);

        // 2. 转换为ES文档
//        List<Goods> docs = goodsList.stream()
//                .map(goods -> new Goods(
//                        goods.getId().toString(),
//                        goods.getName(),
//                        goods.getDescription(),
//                        goods.getPrice()
//                        // 其他字段...
//                )).collect(Collectors.toList());

        // 3. 批量写入ES（带重试）
        this.executeBulkWithRetry(goodsList, 3);
    }

    private void executeBulkWithRetry(List<Goods> docs, int maxRetries) throws InterruptedException {
        int retryCount = 0;
        while (retryCount <= maxRetries) {
            try {
                elasticsearchOperations.save(docs);
                break;
            } catch (Exception e) {
                if (retryCount == maxRetries) {
                    System.out.println("批量写入ES失败，已达最大重试次数" + e.getMessage());
                    throw e;
                }
                System.out.println("批量写入ES失败，第{}次重试..." + retryCount);
                retryCount++;
                Thread.sleep(2000 * retryCount); // 指数退避
            }
        }
    }
}

