package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.QueryArticleDto;
import com.lzx.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(String id);

    ResponseResult addArticle(Article article);

    ResponseResult queryArticle(Integer pageNum, Integer pageSize, QueryArticleDto queryArticleDto);

    ResponseResult getArticleWithTagById(Long id);

    ResponseResult updateArticle(Article article);

    ResponseResult deleteOneArticleById(Long id);
}
