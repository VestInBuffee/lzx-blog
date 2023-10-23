package com.lzx.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lzx.domain.dto.ArticleListDto;
import com.lzx.domain.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
//    IPage<Article> getArticlePageList(IPage<Article> page, @Param(Constants.WRAPPER)LambdaQueryWrapper<Article> wrapper,
//                                      @Param("pageSize") Integer pageSize);

    void insertArticleTagRelation(@Param("articleId")Long id,
                                  @Param("tags")List<Long> tags);

    List<Long> getTagIdByArticleId(@Param("id")Long id);

//    void updateArticleTag(@Param("id")Long id, @Param("tags")List<Long> tags);

    void deleteOldArticleTagRelationById(@Param("id")Long id);

    List<Long> getArticleIdByTagId(@Param("pageSize")Integer pageSize,
                                   @Param("articleListDto")ArticleListDto articleListDto);
}
