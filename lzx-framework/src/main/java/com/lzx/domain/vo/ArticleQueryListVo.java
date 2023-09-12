package com.lzx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleQueryListVo {
    //文章内容
    private String content;
    //所属分类id
    private Long categoryId;
    private Date createTime;
    private Long id;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //是否允许评论 1是，0否
    private String isComment;
    //缩略图
    private String thumbnail;
    //文章摘要
    private String summary;
    //标题
    private String title;
    //访问量
    private Long viewCount;
}
