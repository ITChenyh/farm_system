package com.cyh.elesticsearch;

import com.cyh.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsESRepository extends ElasticsearchRepository<Goods, Long> {

    // 1. 基础名称搜索
    Page<Goods> findByName(String name, Pageable pageable);

    // 2. 多字段联合搜索（名称、描述、特色）
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name\", \"description\", \"specials\"]}}")
    Page<Goods> search(String keyword, Pageable pageable);



}

