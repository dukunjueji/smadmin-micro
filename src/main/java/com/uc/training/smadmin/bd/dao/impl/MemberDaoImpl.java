package com.uc.training.smadmin.bd.dao.impl;

import com.uc.training.smadmin.bd.dao.MemberDao;
import com.uc.training.smadmin.bd.model.Member;
import com.uc.training.smadmin.bd.re.MemberDetailRE;
import com.uc.training.smadmin.bd.re.MemberInfoRE;
import com.uc.training.smadmin.bd.vo.MemberBalanceVO;
import com.uc.training.smadmin.bd.vo.MemberGrowthVO;
import com.uc.training.smadmin.bd.vo.MemberIntegralVO;
import com.uc.training.smadmin.bd.vo.MemberListVO;
import com.uc.training.smadmin.bd.vo.MemberLoginVO;
import com.zuche.framework.dao.CarIsIbatisDaoImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 版权说明：Copyright (c) 2018 ucarinc. All Rights Reserved.
 *
 * @author：shixian.zhang@ucarinc.com
 * @version：v1.0
 * @date: 2018/10/16
 * 说明：用户持久化层实现类
 */
@Repository
public class MemberDaoImpl extends CarIsIbatisDaoImpl implements MemberDao {
    @Override
    public void insertMember(Member member) {
        this.insert("com.uc.training.smadmin.bd.dao.MemberDao.insertMember", member);
    }

    @Override
    public Member queryOneMember(Member member) {
        return (Member) this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.queryOneMember", member);
    }

    @Override
    public void updateMember(Member member) {
        this.update("com.uc.training.smadmin.bd.dao.MemberDao.updateMember", member);
    }

    @Override
    public void updateMemberBalance(Member member) {
        this.update("com.uc.training.smadmin.bd.dao.MemberDao.updateMemberBalance", member);
    }

    /**
     * 查询余额
     * @param memberId
     */
    @Override
    public BigDecimal queryBalances(Long memberId) {
         return (BigDecimal)this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.queryBalances",memberId);
    }

    @Override
    public MemberDetailRE getMemberDetailById(Long memberId) {
        return (MemberDetailRE)this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.getMemberDetailById", memberId);
    }

    @Override
    public Member getMemberLogin(MemberLoginVO memberLoginVO) {
        return (Member)this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.getMemberLogin", memberLoginVO);
    }

    @Override
    public int updateMemberInfo(Member member) {
        return this.update("com.uc.training.smadmin.bd.dao.MemberDao.updateMemberInfo", member);
    }

    @Override
    public MemberInfoRE queryOneMemberById(Long uid) {
        return (MemberInfoRE)this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.queryOneMemberById", uid);
    }

    @Override
    public void updateIntegralById(MemberIntegralVO memberIntegralVO) {
        this.update("com.uc.training.smadmin.bd.dao.MemberDao.updateIntegralById", memberIntegralVO);
    }

    @Override
    public void updateGrowthById(MemberGrowthVO memberGrowthVO) {
        this.update("com.uc.training.smadmin.bd.dao.MemberDao.updateGrowthById", memberGrowthVO);
    }

    @Override
    public List<Member> getMemberList(MemberListVO memberListVO) {
        return (List<Member>) this.queryForList("com.uc.training.smadmin.bd.dao.MemberDao.queryMemberList", memberListVO);
    }

    @Override
    public Long queryMemberCount(MemberListVO memberListVO) {
        return (Long) this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.queryMemberCount", memberListVO);
    }

    @Override
    public void updateBalance(MemberBalanceVO memberBalanceVO) {
        this.update("com.uc.training.smadmin.bd.dao.MemberDao.updateBalance", memberBalanceVO);
    }

    @Override
    public Member queryMemberTel(Long memberId) {
        return (Member) this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.queryMemberTel", memberId);
    }

    @Override
    public Member queryMemberByTel(String telephone) {
        return (Member) this.queryForObject("com.uc.training.smadmin.bd.dao.MemberDao.queryMemberByTel", telephone);
    }

}