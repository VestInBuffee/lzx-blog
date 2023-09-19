package com.lzx.controller;

import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.QueryArticleDto;
import com.lzx.domain.entity.Article;
import com.lzx.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult addArticle(@RequestBody Article article){
        return articleService.addArticle(article);
    }
    @GetMapping("/list")
    public ResponseResult queryArticle(Integer pageNum ,Integer pageSize,
                                       QueryArticleDto queryArticleDto){
        return articleService.queryArticle(pageNum, pageSize, queryArticleDto);
    }
    @GetMapping("{id}")
    public ResponseResult getArticleWithTagByArticleId(@PathVariable("id") Long id){
        return articleService.getArticleWithTagByArticleId(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article){
        return articleService.updateArticle(article);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteOneArticleById(@PathVariable("id") Long id){
        return articleService.deleteOneArticleById(id);
    }
}
