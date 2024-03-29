package com.ucar.smadmin.gds.service;

import com.ucar.smadmin.gds.vo.CommentReplyVO;
import com.ucar.smapi.gds.re.CommentDetailRE;

/**
 * 版权声明： Copyright (c) 2008 ucarinc. All Rights Reserved.
 *
 * @author 何麒（qi.he@ucarinc.com）
 * @Version 1.0
 * @date 2018/11/17
 */
public interface CommentReplyService {
    /**
     * 获取评论的后台回复
     * @param commentId
     * @return
     */
    String getAdminReplyContent(Long commentId);

    /**
     * 根据评论id获取评论详情
     * @param commentReplyVO
     * @return
     */
    CommentDetailRE getCommentDetailByCommentId(CommentReplyVO commentReplyVO);

    /**
     * 新增商品详情回复
     * @param commentReplyVO
     * @return
     */
    Long insertCommentReply(CommentReplyVO commentReplyVO);

    /**
     * 根据评论ID修改商家评论信息
     * @param commentReplyVO
     * @return
     */
    Integer editCommentReply(CommentReplyVO commentReplyVO);

    /**
     * 删除商品详情回复
     * @param commentReplyVO
     * @return
     */
    Integer deleteCommentReplyById(CommentReplyVO commentReplyVO);
}
