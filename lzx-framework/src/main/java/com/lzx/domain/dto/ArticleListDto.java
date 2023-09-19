package com.lzx.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListDto {
    //所属分类id
    private Long categoryId;
    //查询标签id
    private Long tagId;
}
