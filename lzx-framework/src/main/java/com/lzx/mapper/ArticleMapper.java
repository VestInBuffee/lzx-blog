package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.domain.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    void insertArticleTagRelation(@Param("articleId")Long id,
                                  @Param("tags")List<Long> tags);

    List<Long> getTagIdByArticleId(@Param("id")Long id);

//    void updateArticleTag(@Param("id")Long id, @Param("tags")List<Long> tags);

    void deleteOldArticleTagRelationById(@Param("id")Long id);
}
