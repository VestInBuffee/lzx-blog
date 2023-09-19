package com.lzx.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.lzx.domain.dto.TagArticleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListVo {
    private Long id;
    //标题
    private String title;
    //摘要
    private String summary;
    //标签
    private List<TagArticleDto> tagList;
    //所属分类id
    @JSONField(name = "class_id")
    private Long categoryId;
    //所属分类名
    private String categoryName;
    //缩略图
    private String thumbnail;
    //浏览量
    private Long viewCount;
    private Date createTime;
}
