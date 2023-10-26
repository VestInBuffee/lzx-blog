package com.lzx.service.impl;

import cn.easyes.core.biz.PageInfo;
import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.ArticleListDto;
import com.lzx.domain.dto.QueryArticleDto;
import com.lzx.domain.dto.TagArticleDto;
import com.lzx.domain.entity.Article;
import com.lzx.domain.entity.Category;
import com.lzx.domain.entity.Tag;
import com.lzx.domain.vo.*;
import com.lzx.mapper.ArticleMapper;
import com.lzx.mapper.easyes.EsArticleMapper;
import com.lzx.service.ArticleService;
import com.lzx.service.CategoryService;
import com.lzx.service.TagService;
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

    @Autowired
    private TagService tagService;
    @Autowired
    private EsArticleMapper esArticleMapper;

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
    public ResponseResult articleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        //1.获取到article
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1.2必须是已发布的文章
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //1.?id需大于上一页的最大id TODO 当两个topId 相差太远， 会损失数据
        Long lastPageMaxArticleId = articleListDto.getLastPageMaxArticleId();
        lambdaQueryWrapper.gt(Objects.nonNull(lastPageMaxArticleId) && lastPageMaxArticleId>0
                , Article::getId, lastPageMaxArticleId);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop).orderByAsc(Article::getId);

        Page<Article> page = new Page<>(1, pageSize);
        page(page, lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        Long total = page.getTotal();

        PageVo pageVo = getPageVoByArticleAndTotal(articles, total);

        //4.返回pageVo
        return ResponseResult.okResult(pageVo);
    }

    private List<Long> getArticleIdByTagId(Integer pageSize, ArticleListDto articleListDto) {
        return getBaseMapper().getArticleIdByTagId(pageSize, articleListDto);
    }

    private List<TagArticleDto> getTagListByArticleId(Long id) {
        List<Long> tagIds = getBaseMapper().getTagIdByArticleId(id);
        List<TagArticleDto> tagArticleDtos = null;
        if(tagIds.size() > 0){
            List<Tag> tags = tagService.listByIds(tagIds);
            tagArticleDtos = BeanCopyUtils.copyBeanList(tags, TagArticleDto.class);
        }
        return tagArticleDtos;
    }


    private PageVo getPageVoByPageNumAndPageSizeAndLambdaEsQueryWrapper(Integer pageNum, Integer pageSize, LambdaEsQueryWrapper wrapper) {
        PageInfo<Article> pageInfo = esArticleMapper.pageQuery(wrapper, pageNum, pageSize);
        List<Article> articles = pageInfo.getList();

        List<ArticleListVo> articleListVos = getArticleListVosByArticle(articles);

        PageVo pageVo = new PageVo(articleListVos, pageInfo.getTotal());
        return pageVo;
    }

    private List<ArticleListVo> getArticleListVosByArticle(List<Article> articles) {
        List<Article> articleWithCategoryNameList =
                articles.stream()
                        .peek(article ->
                                article.setCategoryName(
                                        categoryService.getById(
                                                article.getCategoryId()).getName()))
                        .collect(Collectors.toList());
        //2.3 Tag
        List<Article> articleWithCategoryNameAndTagList =
                articleWithCategoryNameList.stream().
                        peek(article -> {
                            article.setTagList(
                                    getTagListByArticleId(article.getId()));
                        }).collect(Collectors.toList());

        //3.将article封装成articleVo,然后封装成pagevo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(
                articleWithCategoryNameAndTagList, ArticleListVo.class);

        return articleListVos;
    }

    @Override
    public ResponseResult articleListUsingCategoryId(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        //1.获取到article
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getCategoryId, articleListDto.getCategoryId());

        //1.2必须是已发布的文章
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        Long lastPageMaxArticleId = articleListDto.getLastPageMaxArticleId();
        lambdaQueryWrapper.gt(Objects.nonNull(lastPageMaxArticleId) && lastPageMaxArticleId>0
                , Article::getId, lastPageMaxArticleId);
        //1.3对是否置顶进行排序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop).orderByAsc(Article::getId);

        Page<Article> page = new Page<>(1, pageSize);
        page(page, lambdaQueryWrapper);

        List<Article> articles = page.getRecords();
        Long total = page.getTotal();

        PageVo pageVo = getPageVoByArticleAndTotal(articles, total);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult articleListUsingTagId(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        //1.获取到article
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        List<Long> articleIds = getArticleIdByTagId(pageSize, articleListDto);
        wrapper.in(articleIds.size() > 0, Article::getId, articleIds);
        //1.?id需大于上一页的最大id TODO 当两个topId 相差太远， 会损失数据

        Page<Article> page = new Page<>(1, pageSize);
        page(page, wrapper);

        List<Article> articles = page.getRecords();
        Long total = getTotalByArticleAndTag(articleListDto);

        PageVo pageVo = getPageVoByArticleAndTotal(articles, total);

        return ResponseResult.okResult(pageVo);
    }

    private PageVo getPageVoByArticleAndTotal(List<Article> articles, Long total) {
        List<ArticleListVo> articleListVos = getArticleListVosByArticle(articles);
        PageVo pageVo = new PageVo(articleListVos, total);
        return pageVo;
    }

    private Long getTotalByArticleAndTag(ArticleListDto articleListDto) {
        return getBaseMapper().getTotalByArticleAndTag(articleListDto);
    }

    @Override
    public ResponseResult articleListUsingQueryContent(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        LambdaEsQueryWrapper<Article> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.match(Article::getContent, articleListDto.getQueryContent());
        wrapper.eq(Article::getDelFlag, SystemConstants.ARTICLE_NOTDELETE);
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        wrapper.orderByDesc(Article::getIsTop).orderByAsc(Article::getId);

        PageVo pageVo = getPageVoByPageNumAndPageSizeAndLambdaEsQueryWrapper(
                pageNum, pageSize, wrapper);

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
        //查找到tag
        List<TagArticleDto> tagArticleDtoList = getTagListByArticleId(id);
        articleDetailVo.setTagList(tagArticleDtoList);
        //返回结果
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(String id) {
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEWCOUNT_REDIS_KEY,
                id, 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addArticle(Article article) {
        //更新article表
        save(article);
        //更新article_tag表
        getBaseMapper().insertArticleTagRelation(article.getId(),
                article.getTagList().stream()
                        .map(TagArticleDto::getId).collect(Collectors.toList()));
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
    public ResponseResult getArticleWithTagByArticleId(Long id) {
        Article article = getById(id);
        List<TagArticleDto> tagArticleDtos = getTagListByArticleId(id);
        article.setTagList(tagArticleDtos);
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
        getBaseMapper().insertArticleTagRelation(article.getId(),
                article.getTagList().stream()
                    .map(TagArticleDto::getId).collect(Collectors.toList()));
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
