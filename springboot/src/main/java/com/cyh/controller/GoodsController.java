package com.cyh.controller;

import com.cyh.common.Result;
import com.cyh.entity.Goods;
import com.cyh.service.GoodsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 农产品信息操作接口
 **/
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * ES搜索
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public Result search(
            @RequestParam(defaultValue = "有机") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Goods> p = goodsService.search(keyword, page, size);
        PageInfo<Goods> pageInfo = convertToPageInfo(p);
        return Result.success(pageInfo);
    }
    private PageInfo<Goods> convertToPageInfo(Page<Goods> page) {
        PageInfo<Goods> pageInfo = new PageInfo<>();
        pageInfo.setTotal(page.getTotalElements());
        pageInfo.setList(page.getContent());
        pageInfo.setPageNum(page.getNumber() + 1); // Pageable page numbers are 0-based, convert to 1-based
        pageInfo.setPageSize(page.getSize());
        pageInfo.setSize(page.getSize());
        pageInfo.setStartRow((page.getNumber() * page.getSize()) + 1);
        pageInfo.setEndRow(Math.min(page.getTotalElements(), pageInfo.getStartRow() + pageInfo.getPageSize() - 1));
        pageInfo.setPages((int) Math.ceil((double) page.getTotalElements() / page.getSize()));
        pageInfo.setPrePage(page.getNumber() > 0 ? page.getNumber() : 0);
        pageInfo.setNextPage(page.getTotalElements() > (page.getNumber() + 1) * page.getSize() ? page.getNumber() + 2 : 0);
        pageInfo.setIsFirstPage(page.getNumber() == 0);
        pageInfo.setIsLastPage(page.getTotalElements() <= (page.getNumber() + 1) * page.getSize());
        pageInfo.setHasPreviousPage(page.getNumber() > 0);
        pageInfo.setHasNextPage(page.getTotalElements() > (page.getNumber() + 1) * page.getSize());
        pageInfo.setNavigatePages(8); // Example: fixed number of navigate pages
        pageInfo.setNavigatepageNums(generateNavigatepageNums(page.getTotalElements(), page.getNumber(), page.getSize(), 8));
        pageInfo.setNavigateFirstPage(1);
        pageInfo.setNavigateLastPage(page.getTotalPages());
        return pageInfo;
    }

    private int[] generateNavigatepageNums(long totalElements, int currentPage, int pageSize, int maxNavigatePages) {
        List<Integer> navigatepageNums = new ArrayList<>();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);
        int start = Math.max(currentPage - maxNavigatePages / 2, 0);
        int end = Math.min(start + maxNavigatePages, totalPages);
        for (int i = start; i < end; i++) {
            navigatepageNums.add(i + 1); // Convert to 1-based page numbers
        }
        return navigatepageNums.stream().mapToInt(i -> i).toArray();
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Goods goods) {
        goodsService.saveGoods(goods);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        goodsService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Goods goods) {
        goodsService.updateById(goods);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Goods goods = goodsService.selectById(id);
        return Result.success(goods);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Goods goods) {
        List<Goods> list = goodsService.selectAll(goods);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Goods goods,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Goods> page = goodsService.selectPage(goods, pageNum, pageSize);
        return Result.success(page);
    }

}