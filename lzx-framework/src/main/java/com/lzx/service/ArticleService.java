package com.lzx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ArticleListDto;
import com.lzx.domain.dto.QueryArticleDto;
import com.lzx.domain.dto.TagArticleDto;
import com.lzx.domain.entity.Article;
import com.lzx.domain.vo.PageVo;

import java.util.List;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(String id);

    ResponseResult addArticle(Article article);

    ResponseResult queryArticle(Integer pageNum, Integer pageSize, QueryArticleDto queryArticleDto);

    ResponseResult getArticleWithTagByArticleId(Long id);

    ResponseResult updateArticle(Article article);

    ResponseResult deleteOneArticleById(Long id);
    List<TagArticleDto> getTagListByArticleId(Long id);
    PageVo getPageVoByPageNumAndPageSizeAndLambdaQueryWrapper(Integer pageNum, Integer pageSize, LambdaQueryWrapper wrapper);

    ResponseResult articleListUsingCategoryId(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult articleListUsingTagId(Integer pageNum, Integer pageSize, Long tagId);

    ResponseResult articleListUsingQueryContent(Integer pageNum, Integer pageSize, String queryContent);
}
