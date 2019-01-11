package com.uc.training.base.bd.controller;

import com.uc.training.base.bd.re.MemberRechargeHistoryRE;
import com.uc.training.base.bd.service.MemberRechargeHistoryService;
import com.uc.training.base.bd.vo.MemberRechargeHistoryVO;
import com.smgoods.common.annotation.AccessLogin;
import com.smgoods.common.base.controller.BaseController;
import com.smgoods.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 版权声明： Copyright (c) 2008 ucarinc. All Rights Reserved.
 *
 * @author 何麒（qi.he@ucarinc.com）
 * @Version 1.0
 * @date 2018/11/15
 */
@Controller
@RequestMapping("/api/rechargeHistory")
public class MemberRechargeHistoryController extends BaseController {

    @Autowired
    private MemberRechargeHistoryService memberRechargeHistoryService;

    /**
     * 通过memberId获取充值历史
     * @param memberRechargeHistoryVO
     * @return
     */
    @ResponseBody
    @AccessLogin
    @RequestMapping(value = "/getHistoryList.do_", method = RequestMethod.POST)
    public Result<MemberRechargeHistoryRE> getHistoryListByMemberId(MemberRechargeHistoryVO memberRechargeHistoryVO) {
        MemberRechargeHistoryRE memberRechargeHistoryRE = new MemberRechargeHistoryRE();
        memberRechargeHistoryVO.setMemberId(getUid());
        memberRechargeHistoryRE.setMemberRechargeHistoryList(memberRechargeHistoryService.getRechargeHistoryListByMemberId(memberRechargeHistoryVO));
        memberRechargeHistoryRE.setTotalNum(memberRechargeHistoryService.getCountByMemberId(getUid()));

        return Result.getSuccessResult(memberRechargeHistoryRE);
    }

}
