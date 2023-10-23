package com.lzx.domain.entity;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.common.constants.Analyzer;
import cn.easyes.common.enums.FieldType;
import cn.easyes.common.enums.IdType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.domain.dto.TagArticleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * 文章表(Article)表实体类
 *
 * @author makejava
 * @since 2023-08-06 17:14:00
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("lzx_article")
@IndexName(value = "lzx_blog")
public class Article {
    @IndexId(type = IdType.CUSTOMIZE)
    @TableId
    private Long id;
    //标题
    private String title;
    //文章内容
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_SMART)
    private String content;
    //文章摘要
    private String summary;
    //所属分类id
    @IndexField("category_id")
    private Long categoryId;
    //所属分类名字
    @TableField(exist = false)
    @IndexField(exist = false)
    private String categoryName;
    //TagList
    @TableField(exist = false)
    @IndexField(exist = false)
    private List<TagArticleDto> tagList;

    //缩略图
    private String thumbnail;
    //是否置顶（0否，1是）
    @IndexField("is_top")
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //访问量
    @IndexField("view_count")
    private Long viewCount;
    //是否允许评论 1是，0否
    @IndexField("is_comment")
    private String isComment;
    @TableField(fill = FieldFill.INSERT)
    @IndexField("create_by")
    private Long createBy;
    @TableField(fill = FieldFill.INSERT)
    @IndexField("create_time")
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @IndexField("update_by")
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @IndexField("update_time")
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    @IndexField("del_flag")
    private Integer delFlag;

    public Article(Long id, Long viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }

}

