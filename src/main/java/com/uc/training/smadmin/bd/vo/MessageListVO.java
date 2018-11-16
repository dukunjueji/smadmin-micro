package com.uc.training.smadmin.bd.vo;

import com.ycc.base.common.BaseDomain;

import java.io.Serializable;

/**
 * 版权说明：Copyright (c) 2018 ucarinc. All Rights Reserved.
 *
 * @author：shixian.zhang@ucarinc.com
 * @version：v1.0
 * @date: 2018/10/31
 * 说明：
 */
public class MessageListVO extends BaseDomain implements Serializable {
    private static final long serialVersionUID = -2369112649523641001L;

    /**
     * 会员id
     */
    private Long memberId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "MessageListVO{" +
                "memberId=" + memberId +
                '}';
    }
}