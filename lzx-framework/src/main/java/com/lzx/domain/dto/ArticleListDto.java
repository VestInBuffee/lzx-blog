package com.lzx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListDto {
    //上次分页的最大articleId
    private Long lastPageMaxArticleId;
    //内容
    private String queryContent;
    //所属分类id
    private Long categoryId;
    //查询标签id
    private Long tagId;
}
