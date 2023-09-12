package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.QueryArticleDto;
import com.lzx.domain.entity.Article;
import com.lzx.domain.entity.Category;
import com.lzx.domain.vo.*;
import com.lzx.mapper.ArticleMapper;
import com.lzx.service.ArticleService;
import com.lzx.service.CategoryService;
import com.lzx.utils.BeanCopyUtils;
import com.lzx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lzx
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    @Lazy
    CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);

        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        //调用自己写的BeanCopyUtils工具类，将Article列表Copy到HotArticle列表
        List<HotArticleVo> hotArticleVos =
                BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //1.获取到article
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1.1是否传入正确地categoryId
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0,
                Article::getCategoryId, categoryId);
        //1.2必须是已发布的文章
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //1.3对是否置顶进行排序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //2.查询指定页面的article，查找到categoryName
        //2.1查询指定页面的article
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);
        //2.2查找到categoryName
        List<Article> articles = page.getRecords();
        List<Article> articleWithCategoryNameList =
                articles.stream()
                .peek(article ->
                        article.setCategoryName(
                                categoryService.getById(
                                        article.getCategoryId()).getName()))
                        .collect(Collectors.toList());

        //3.将article封装成articleVo,然后封装成pagevo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(
                articleWithCategoryNameList, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        //4.返回pageVo
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //查询指定id的article
        Article article = getById(id);
        //从redis中查询viewCount
        Integer viewCount = redisCache.getCacheMapValue(
                SystemConstants.ARTICLE_VIEWCOUNT_REDIS_KEY, id.toString());
        article.setViewCount(viewCount.longValue());
        //封装成指定vo
        ArticleDetailVo articleDetailVo =
                BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //查找到CategoryName
        Category category = categoryService.getById(articleDetailVo.getCategoryId());
        articleDetailVo.setCategoryName(category.getName());
        //返回结果
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(String id) {
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_REDIS_KEY,
                id, 1);
        return null;
    }

    @Override
    @Transactional
    public ResponseResult addArticle(Article article) {
        //更新article表
        save(article);
        //更新article_tag表
        getBaseMapper().insertArticleTagRelation(article.getId(), article.getTags());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryArticle(Integer pageNum, Integer pageSize, QueryArticleDto queryArticleDto) {
        //查询article
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(queryArticleDto.getTitle()),
                Article::getTitle, queryArticleDto.getTitle());
        wrapper.like(StringUtils.hasText(queryArticleDto.getSummary()),
                Article::getSummary, queryArticleDto.getSummary());
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        //序列化成ArticleQueryListVo
        List<Article> articles = page.getRecords();
        List<ArticleQueryListVo> articleQueryListVos =
                BeanCopyUtils.copyBeanList(articles, ArticleQueryListVo.class);

        //返回
        return ResponseResult.okResult(new PageVo(articleQueryListVos, page.getTotal()));
    }

    @Override
    public ResponseResult getArticleWithTagById(Long id) {
        Article article = getById(id);
        List<Long> tags = getBaseMapper().getTagById(id);
        article.setTags(tags);
        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult updateArticle(Article article) {
        //1.更新article表
        updateById(article);
        //2.更新article_tag表
        //2.1删除旧关系
        getBaseMapper().deleteOldArticleTagRelationById(article.getId());
        //2.2添加新关系
        getBaseMapper().insertArticleTagRelation(article.getId(), article.getTags());
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteOneArticleById(Long id) {
        //article表
        removeById(id);
        //article_tag表
        getBaseMapper().deleteOldArticleTagRelationById(id);
        return ResponseResult.okResult();
    }
}
