package com.lzx.controller;


import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ArticleListDto;
import com.lzx.domain.entity.Article;

import com.lzx.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author lzx
 */
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public List<Article> list(){
        return articleService.list();
    }

    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        // 根据类别进行查询
        Long categoryId = articleListDto.getCategoryId();
        if(Objects.nonNull(categoryId) && categoryId>0){
            return articleService.articleListUsingCategoryId(pageNum, pageSize, articleListDto);
        }
        // 根据标签进行查询
        Long tagId = articleListDto.getTagId();
        if(Objects.nonNull(tagId) && tagId>0){
            return articleService.articleListUsingTagId(pageNum, pageSize, articleListDto);
        }
        //根据内容进行查询
        String queryContent = articleListDto.getQueryContent();
        if(StringUtils.hasText(queryContent)){
            return articleService.articleListUsingQueryContent(pageNum, pageSize, articleListDto);
        }
        //无特殊参数查询
        return articleService.articleList(pageNum, pageSize, articleListDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }

    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") String id){
        return articleService.updateViewCount(id);
    }
}
