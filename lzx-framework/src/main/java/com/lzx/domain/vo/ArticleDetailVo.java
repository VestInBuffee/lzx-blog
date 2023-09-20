package com.lzx.domain.vo;

import com.lzx.domain.dto.TagArticleDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    //标题
    private String title;
    //摘要
    private String content;
    //所属分类id
    private Long categoryId;
    //所属分类名
    private String categoryName;
    //tagList
    private List<TagArticleDto> tagList;
    //缩略图
    private String thumbnail;
    //浏览量
    private Long viewCount;
    private Date createTime;
}
