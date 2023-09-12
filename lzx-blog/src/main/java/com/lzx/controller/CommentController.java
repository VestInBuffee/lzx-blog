package com.lzx.controller;

import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.dto.AddCommentDto;
import com.lzx.domain.entity.Comment;
import com.lzx.service.CommentService;
import com.lzx.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lzx
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_ARTICLE,
                articleId, pageNum, pageSize);
    }
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto){
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Long articleId, Integer pageNum, Integer pageSize){
        return commentService.commentList(SystemConstants.COMMENT_TYPE_LINK,
                null, pageNum, pageSize);
    }
}
