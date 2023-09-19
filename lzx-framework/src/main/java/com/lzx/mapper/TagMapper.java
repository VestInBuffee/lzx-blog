package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-21 16:28:37
 */
public interface TagMapper extends BaseMapper<Tag> {

//    List<Long> getTagIdByArticleId(@Param("articleId")Long id);
}

