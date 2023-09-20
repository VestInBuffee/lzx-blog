package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.constants.SystemConstants;
import com.lzx.domain.ResponseResult;
import com.lzx.domain.entity.Comment;
import com.lzx.domain.vo.CommentVo;
import com.lzx.domain.vo.PageVo;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.mapper.CommentMapper;
import com.lzx.service.CommentService;
import com.lzx.service.UserService;
import com.lzx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-08-13 15:30:31
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //1.查询指定article、回复评论id的父级comment
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //1.1.如果是article下的comment，查询指定Id的article下的父级comment
        lambdaQueryWrapper.eq(SystemConstants.COMMENT_TYPE_ARTICLE.equals(commentType),
                Comment::getArticleId, articleId);
        //1.2.父级comment回复对象的id为-1
        lambdaQueryWrapper.eq(Comment::getToCommentId, -1);
        //1.3.父级comment的type为指定type
        lambdaQueryWrapper.eq(Comment::getType, commentType);
        //1.4查询父级comment
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        //2.获取commentVo
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(
                page.getRecords(), CommentVo.class);

        //3.查询到昵称,并给评论对象昵称赋值,然后查询子级comment
        //3.1查询到昵称,并给评论对象昵称赋值
        commentVos = queryNicknameAndToCommentUsername(commentVos);
        //3.2查询子级comment
        commentVos = commentVos.stream()
                .peek(commentVo -> {
                    //3.2.1子级comment需要满足rootId和父评论id之间相等
                    Long id = commentVo.getId();
                    LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Comment::getRootId, id);
                    List<CommentVo> commentVosWithSubCommentVo = BeanCopyUtils.copyBeanList(
                            list(queryWrapper), CommentVo.class
                    );
                    //3.2.2子级comment需要满足3.1
                    commentVo.setChildren(
                            queryNicknameAndToCommentUsername(commentVosWithSubCommentVo));
                }).collect(Collectors.toList());

        //4.返回
        return ResponseResult.okResult(new PageVo(commentVos, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        save(comment);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    /**
     * 对操作对象查询到昵称,并给评论对象昵称赋值
     * @param commentVos 操作对象
     * @return 操作结果
     */
    private List<CommentVo> queryNicknameAndToCommentUsername(List<CommentVo> commentVos){
        return commentVos.stream()
                .peek(commentVo -> {
                    //1.查询到昵称
                    if(!commentVo.getCreateBy().equals(-1L)){
                        String nickName = userService.
                                getById(commentVo.getCreateBy()).getNickName();
                        commentVo.setUsername(nickName);
                    }
                    //2.给评论对象昵称赋值
                    Long commentUserId;
                    if(-1 != (commentUserId = commentVo.getToCommentUserId())){
                        String commentUserName = userService.
                                getById(commentUserId).getNickName();
                        commentVo.setToCommentUserName(commentUserName);
                    }
                }).collect(Collectors.toList());
    }
}

