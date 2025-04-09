package com.cyh.inserter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BulkGoodsInserter {
    // 数据库配置
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/farm_system?useSSL=false&rewriteBatchedStatements=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final int BATCH_SIZE = 1000; // 每批插入数量

    // 数据生成配置
    private static final String[] PRODUCT_NAMES = {"有机白菜", "生态大米", "散养鸡蛋", "高山绿茶", "新鲜草莓"};
    private static final String[] UNITS = {"斤", "公斤", "箱", "袋", "盒"};
    private static final int MAX_CATEGORY = 5;

    private final Random random = new Random();

    public static void main(String[] args) {
        new BulkGoodsInserter().insertGoods(1000000);
    }

    public void insertGoods(int totalRecords) {
        long startTime = System.nanoTime();

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            conn.setAutoCommit(false); // 关闭自动提交

            String sql = "INSERT INTO goods (name, img, description, specials, price, unit, store, category_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 1; i <= totalRecords; i++) {
                    fillPreparedStatement(ps);
                    ps.addBatch();

                    if (i % BATCH_SIZE == 0 || i == totalRecords) {
                        ps.executeBatch();
                        conn.commit();
                        System.out.printf("已插入: %d/%d (%.1f%%)%n",
                                i, totalRecords, (i * 100.0 / totalRecords));
                    }
                }
            }

            long duration = System.nanoTime() - startTime;
            printStatistics(totalRecords, duration);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillPreparedStatement(PreparedStatement ps) throws SQLException {
        String productName = PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)];

        ps.setString(1, productName + " " + random.nextInt(1000)); // 名称
        ps.setString(2, "http://localhost:9090/files/download/1741598165176-bf07a2d7737de4531242cb411f0e2e3.jpg");        // 图片
        ps.setString(3, generateText(20, 50));                     // 简介
        ps.setString(4, generateText(10, 30));                     // 特色
        ps.setBigDecimal(5, randomPrice());                       // 价格
        ps.setString(6, UNITS[random.nextInt(UNITS.length)]);      // 单位
        ps.setInt(7, random.nextInt(1000));                        // 库存
        ps.setInt(8, 1 + random.nextInt(MAX_CATEGORY));            // 分类ID
    }

    // 生成随机价格（0.01-999.99）
    private BigDecimal randomPrice() {
        return new BigDecimal(1 + random.nextDouble() * 999)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // 生成随机文本（汉字）
    private String generateText(int minLen, int maxLen) {
        int length = minLen + random.nextInt(maxLen - minLen + 1);
        StringBuilder sb = new StringBuilder();

        // 生成随机汉字（0x4e00-0x9fa5）
        for (int i = 0; i < length; i++) {
            sb.append((char) (0x4e00 + random.nextInt(0x9fa5 - 0x4e00 + 1)));
        }
        return sb.toString();
    }

    private void printStatistics(int totalRecords, long nanos) {
        double seconds = TimeUnit.NANOSECONDS.toSeconds(nanos);
        double recPerSec = totalRecords / seconds;

        System.out.println("\n插入统计：");
        System.out.printf("总记录数：%,d 条%n", totalRecords);
        System.out.printf("总耗时：%.2f 秒%n", seconds);
        System.out.printf("插入速度：%,.0f 条/秒%n", recPerSec);
    }
}

