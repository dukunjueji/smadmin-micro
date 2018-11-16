package com.uc.training.smadmin.bd.controller;

import com.uc.training.common.annotation.AccessLogin;
import com.uc.training.common.base.controller.BaseController;
import com.uc.training.common.enums.GrowthEnum;
import com.uc.training.common.enums.SmsStatusEnum;
import com.uc.training.common.enums.SmsTypeEnum;
import com.uc.training.smadmin.bd.model.LoginLog;
import com.uc.training.smadmin.bd.model.Member;
import com.uc.training.smadmin.bd.model.Message;
import com.uc.training.smadmin.bd.re.AllMessageRE;
import com.uc.training.smadmin.bd.re.MemberDetailRE;
import com.uc.training.smadmin.bd.re.MemberInfoRE;
import com.uc.training.smadmin.bd.re.MemberLoginRE;
import com.uc.training.smadmin.bd.re.MessageRE;
import com.uc.training.smadmin.bd.service.MemberService;
import com.uc.training.smadmin.bd.service.MessageService;
import com.uc.training.smadmin.bd.vo.ChargeBalanceVO;
import com.uc.training.smadmin.bd.vo.CreateCodeVO;
import com.uc.training.smadmin.bd.vo.MemberInfoVO;
import com.uc.training.smadmin.bd.vo.MemberLoginVO;
import com.uc.training.smadmin.bd.vo.MemberRegisterVO;
import com.uc.training.smadmin.bd.vo.MessageDetailVO;
import com.uc.training.smadmin.bd.vo.MessageListVO;
import com.uc.training.smadmin.bd.vo.MessageVO;
import com.uc.training.smadmin.mq.MqProducer;
import com.uc.training.smadmin.mq.vo.MqVO;
import com.uc.training.smadmin.bd.vo.PasswordEditVO;
import com.uc.training.smadmin.bd.vo.SendCodeVO;
import com.uc.training.smadmin.ord.service.OrderService;
import com.uc.training.smadmin.redis.RedisConfigEnum;
import com.uc.training.smadmin.sms.service.SmsTemplateService;
import com.uc.training.smadmin.sms.vo.GenerateSmsVO;
import com.uc.training.smadmin.utils.EncryptUtil;
import com.uc.training.smadmin.utils.TokenUtil;
import com.ycc.base.common.Result;
import com.ycc.tools.middleware.metaq.MetaQUtils;
import com.ycc.tools.middleware.redis.RedisCacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版权说明：Copyright (c) 2018 ucarinc. All Rights Reserved.
 *
 * @author：shixian.zhang@ucarinc.com
 * @version：v1.0
 * @date: 2018/10/16
 * 说明：用户请求处理
 */
@Controller
@RequestMapping("api/member")
public class ApiMemberController extends BaseController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SmsTemplateService smsTemplateService;

    private Map<String, String> map = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiMemberController.class);
    /**
     *注册时，生成验证码及短信
     * @param createCodeVO 生成验证码的请求参数
     * @return 显示页面提示信息
     */
    @RequestMapping(value = "/createCode.do_", method = RequestMethod.POST)
    @ResponseBody
    @AccessLogin(required = false)
    public Result createCode(@Validated CreateCodeVO createCodeVO) {
        //根据手机号查询会员信息
        Member mem = new Member();
        mem.setTelephone(createCodeVO.getTelephone());
        Member member = memberService.queryOneMember(mem);
        if(member != null){
            return Result.getBusinessException("手机号码已被注册", null);
        }
        //生成消息体
        MqVO mqVO = new MqVO();
        GenerateSmsVO generateSmsVO = new GenerateSmsVO();
        generateSmsVO.setTelephone(createCodeVO.getTelephone());
        generateSmsVO.setCode(SmsTypeEnum.REGISTER.getCode());
        generateSmsVO.setType(SmsTypeEnum.REGISTER.getType());
        mqVO.setGenerateSmsVO(generateSmsVO);
        MetaQUtils.sendMsgNoException(new MqProducer(mqVO));
        return Result.getSuccessResult("验证码已发送");
    }

    /***
    *说明：会员注册
    *@param memberRegisterVO 会员注册的请求
    *@return：com.uc.training.smadmin.bd.re.MemberMegRE
    *@throws：
    */
    @RequestMapping(value = "/memberRegister.do_", method = RequestMethod.POST)
    @ResponseBody
    @AccessLogin(required = false)
    public Result memberRegister(@Validated MemberRegisterVO memberRegisterVO){
        Member member = new Member();
        member.setTelephone(memberRegisterVO.getTelephone());
        member.setPassword(memberRegisterVO.getPassword());
        Member mem = memberService.queryOneMember(member);
        if(mem != null){
            return Result.getBusinessException("手机号码已被注册",null);
        }
        //redis
        RedisCacheUtils redis = RedisCacheUtils.getInstance(RedisConfigEnum.SYS_CODE);
        String msg = redis.get(memberRegisterVO.getTelephone());
        if (msg == null) {
            return Result.getBusinessException("验证码已过期，请重新获取", null);
        }
        if(memberRegisterVO.getTelCode().equals(msg)){
            memberService.insertMember(member);
            return Result.getSuccessResult("成功");
        }else {
            return Result.getBusinessException("验证码不正确", null);
        }
    }

    /**
     * 会员登录
     * @param memberLoginVO 会员登录请求参数
     * @return com.ycc.base.common.Result<com.uc.training.smadmin.bd.re.MemberLoginRE>
     */
    @RequestMapping(value = "/memberLogin.do_", method = RequestMethod.POST)
    @ResponseBody
    @AccessLogin(required = false)
    public Result<MemberLoginRE> memberLogin(@Validated MemberLoginVO memberLoginVO){
        Result<MemberLoginRE> result;
        Member mem = memberService.queryMemberByTel(memberLoginVO.getTelephone());
        if (mem == null) {
            result = Result.getBusinessException("该账号不存在,请先注册", null);
            return result;
        }
        memberLoginVO.setPassword(EncryptUtil.md5(memberLoginVO.getPassword()));
        Member member = memberService.getMemberLogin(memberLoginVO);
        if (member == null) {
            result = Result.getBusinessException("您的密码错误", null);
        }else {
            String token = TokenUtil.sign(member.getId());
            MemberLoginRE memberLoginRE = new MemberLoginRE();
            memberLoginRE.setToken(token);

            //生成登陆日志
            LoginLog loginLog = new LoginLog();
            loginLog.setMemberId(member.getId());
            loginLog.setIp(getLocalhostIp());

            //生成消息体
            MqVO mqVO = new MqVO();
            mqVO.setMemberId(member.getId());
            mqVO.setGrowthType(GrowthEnum.LOGININ.getGrowthType());

            memberService.memberLogin(loginLog, mqVO);
            result = Result.getSuccessResult(memberLoginRE);
        }
        return result;
    }

    /**
     * 重置密码时，生成验证码及短信
     * @param createCodeVO 发送验证码的请求参数
     * @return 显示页面提示信息
     */
    @RequestMapping(value = "/passwordCode.do_", method = RequestMethod.POST)
    @ResponseBody
    @AccessLogin(required = false)
    public Result passwordCode(@Validated CreateCodeVO createCodeVO) {
        Member member = new Member();
        member.setTelephone(createCodeVO.getTelephone());
        member = memberService.queryOneMember(member);
        if(member == null){
            return Result.getBusinessException("手机号还没被注册", null);
        }
        //生成消息体
        MqVO mqVO = new MqVO();
        GenerateSmsVO generateSmsVO = new GenerateSmsVO();
        generateSmsVO.setTelephone(createCodeVO.getTelephone());
        generateSmsVO.setCode(SmsTypeEnum.FORGET_PASSWORD.getCode());
        generateSmsVO.setType(SmsTypeEnum.FORGET_PASSWORD.getType());
        mqVO.setGenerateSmsVO(generateSmsVO);

        MetaQUtils.sendMsgNoException(new MqProducer(mqVO));
        return Result.getSuccessResult("验证码已发送");
    }

    /***
     *说明：会员重置密码
     *@param memberRegisterVO 重置密码请求参数
     *@return：com.uc.training.smadmin.bd.re.MemberMegRE
     *@throws：
     */
    @RequestMapping(value = "/memberPassword.do_", method = RequestMethod.POST)
    @ResponseBody
    @AccessLogin(required = false)
    public Result memberPassword(@Validated MemberRegisterVO memberRegisterVO){
        Member mem = new Member();
        mem.setTelephone(memberRegisterVO.getTelephone());
        Member member = memberService.queryOneMember(mem);
        if(member == null){
            return Result.getBusinessException("手机号还没被注册", null);
        }
        //redis
        RedisCacheUtils redis = RedisCacheUtils.getInstance(RedisConfigEnum.SYS_CODE);
        String msg = redis.get(memberRegisterVO.getTelephone());
        if (msg == null) {
            return Result.getBusinessException("验证码已过期，请重新获取", null);
        }
        if(memberRegisterVO.getTelCode().equals(msg)){
            mem.setPassword(memberRegisterVO.getPassword());
            mem.setId(member.getId());
            memberService.updateMember(mem);
            return Result.getSuccessResult("重置密码成功");
        }else {
            return Result.getBusinessException("验证码不正确", null);
        }
    }

    /**
     *说明：会员充值金额
     * @param chargeBalanceVO 充值余额的请求参数
     * @return
     */
    @AccessLogin
    @RequestMapping(value = "/chargeBalance.do_", method = RequestMethod.POST)
    @ResponseBody
    public Result chargeBalance(@Validated ChargeBalanceVO chargeBalanceVO){
        String regExp = "^([1-9]\\d*|0)(\\.\\d{1,2})?$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(chargeBalanceVO.getBalance().toString());
        if(!m.matches()){
            return Result.getBusinessException("充值金额有误", null);
        }
        Member member = new Member();
        member.setId(getUid());
        member.setBalance(chargeBalanceVO.getBalance());
        BigDecimal bigDecimal = new BigDecimal(0);
        int i = chargeBalanceVO.getBalance().compareTo(bigDecimal);
        if (chargeBalanceVO.getBalance() == null || i == 0 ) {
            return Result.getBusinessException("充值余额必须大于0", null);
        }else {
            //生成消息体
            MqVO mqVO = new MqVO();
            mqVO.setMemberId(getUid());
            mqVO.setRechargeValue(chargeBalanceVO.getBalance());

            //生成短信
            Member mem = memberService.queryMemberTel(getUid());
            GenerateSmsVO generateSmsVO = new GenerateSmsVO();
            generateSmsVO.setTelephone(mem.getTelephone());
            generateSmsVO.setMessage(chargeBalanceVO.getBalance().toString());
            generateSmsVO.setCode(SmsTypeEnum.RECHARGE.getCode());
            generateSmsVO.setType(SmsTypeEnum.RECHARGE.getType());

            mqVO.setGenerateSmsVO(generateSmsVO);

            memberService.memberRecharge(member, mqVO);

            return Result.getSuccessResult("充值成功");
        }
    }

    /**
    *说明：通过id获取会员的详细信息(包括会员等级)
    *@param
    *@return：com.ycc.base.common.Result<com.uc.training.smadmin.bd.re.MemberDetailRE>
    *@throws：
    */
    @RequestMapping(value = "/getMemberDetailById.do_", method = RequestMethod.GET)
    @ResponseBody
    @AccessLogin
    public Result<MemberDetailRE> getMemberDetailById(){
        MemberDetailRE memberDetailRE = memberService.getMemberDetailById(getUid());
        //会员的订单数量
        Integer orderSum = orderService.queryOrderCount(getUid());
        memberDetailRE.setOrderSum(orderSum);
        //会员的消息数量
        Integer messageSum = messageService.queryMessageCount(getUid());
        memberDetailRE.setMessageSum(messageSum);

        return Result.getSuccessResult(memberDetailRE);
    }

    /**
    *说明：更新会员信息
    *@param memberInfoVO 更新会员信息请求参数
    *@return：com.ycc.base.common.Result
    *@throws：
    */
    @RequestMapping(value = "/editMemberInfo.do_", method = RequestMethod.POST)
    @ResponseBody
    @AccessLogin
    public Result<MemberInfoRE> editMemberInfo(@Validated MemberInfoVO memberInfoVO){
        Member member = new Member();
        member.setNickname(memberInfoVO.getNickname());
        member.setEmail(memberInfoVO.getEmail());
        member.setSex(memberInfoVO.getSex());
        member.setId(getUid());
        member.setImageUrl(memberInfoVO.getImageUrl());

        //更新会员信息
        memberService.updateMemberInfo(member);
        //查询指定会员信息
        MemberInfoRE memberInfoRE = memberService.queryOneMemberById(getUid());

        return Result.getSuccessResult(memberInfoRE);
    }

    /**
    *说明：修改密码时发送验证码
    *@param sendCodeVO 发送验证码接受的参数
    *@return：com.ycc.base.common.Result
    *@throws：
    */
    @ResponseBody
    @RequestMapping(value = "/sendCode.do_", method = RequestMethod.GET)
    @AccessLogin
    public Result sendCode(@Validated SendCodeVO sendCodeVO){
        Member member = memberService.queryMemberTel(getUid());
        // 判断旧密码是否和库里的一致
        String oldpassword = EncryptUtil.md5(sendCodeVO.getOldpassword());
        if(!((member.getPassword()).equals(oldpassword))){
            return Result.getBusinessException("原来的密码输入有误", null);
        }
        // 判断新密码和确认密码是否一致
        if ((sendCodeVO.getNewpassword()).equals(sendCodeVO.getConfirmpassword())){
            //生成消息体
            MqVO mqVO = new MqVO();
            GenerateSmsVO generateSmsVO = new GenerateSmsVO();
            generateSmsVO.setTelephone(member.getTelephone());
            generateSmsVO.setCode(SmsTypeEnum.CHANGE_PASSWORD.getCode());
            generateSmsVO.setType(SmsTypeEnum.CHANGE_PASSWORD.getType());
            mqVO.setGenerateSmsVO(generateSmsVO);
            MetaQUtils.sendMsgNoException(new MqProducer(mqVO));
            return Result.getSuccessResult("验证码已发送");
        }else {
            return Result.getBusinessException("新的密码和确认密码不一致", null);
        }
    }

    /**
    *说明：修改会员密码
    *@param passwordEditVO 修改会员密码接收的参数
    *@return：com.ycc.base.common.Result
    *@throws：
    */
    @ResponseBody
    @RequestMapping(value = "/editMemberPassword.do_", method = RequestMethod.POST)
    @AccessLogin
    public Result editMemberPassword(@Validated PasswordEditVO passwordEditVO){
        Member member = new Member();
        member.setId(getUid());
        Member mem = memberService.queryMemberTel(getUid());
        // 判断旧密码是否和库里的一致
        String oldpassword = EncryptUtil.md5(passwordEditVO.getOldpassword());
        if(!((mem.getPassword()).equals(oldpassword))){
            return Result.getBusinessException("原来的密码输入有误", null);
        }else if(!((passwordEditVO.getNewpassword()).equals(passwordEditVO.getConfirmpassword()))){
            return Result.getBusinessException("新的密码和确认密码不一致", null);
        }
        //redis
        RedisCacheUtils redis = RedisCacheUtils.getInstance(RedisConfigEnum.SYS_CODE);
        String msg = redis.get(mem.getTelephone());
        if (msg == null) {
            return Result.getBusinessException("验证码已过期，请重新获取", null);
        }
        if((passwordEditVO.getCode()).equals(msg)){
            member.setPassword(passwordEditVO.getNewpassword());
            memberService.updateMember(member);
            return Result.getSuccessResult("修改密码成功");
        }else {
            return Result.getBusinessException("输入的验证码有误", null);
        }
    }

    /**
    *说明：获取指定会员的消息列表
    *@param
    *@return：
    *@throws：
    */
    @ResponseBody
    @AccessLogin()
    @RequestMapping(value = "queryMessageList.do_", method = RequestMethod.GET)
    public Result<AllMessageRE> queryMessageList(MessageListVO messageListVO){
        messageListVO.setMemberId(getUid());
        List<MessageRE> messageREList = messageService.queryMessageList(messageListVO);
        Integer totalNum = messageService.queryAllMessageCount(getUid());

        AllMessageRE allMessageRE = new AllMessageRE();
        allMessageRE.setMessageREList(messageREList);
        allMessageRE.setTotalNum(totalNum);
        return Result.getSuccessResult(allMessageRE);
    }

    /**
    *说明：更新消息的状态
    *@param messageVO
    *@return：com.ycc.base.common.Result
    *@throws：
    */
    @RequestMapping(value = "/updateMessageStatus.do_", method = RequestMethod.POST)
    @AccessLogin
    @ResponseBody
    public Result updateMessageStatus(@Validated MessageVO messageVO){
        Message message = new Message();
        message.setId(messageVO.getId());
        message.setIsRead(messageVO.getIsRead());
        message.setMemberId(getUid());
        return Result.getSuccessResult( messageService.updateMessageStatus(message));
    }

    /**
    *说明：查询一个消息详情
    *@param messageId
    *@return：com.ycc.base.common.Result<com.uc.training.smadmin.bd.vo.MessageDetailVO>
    *@throws：
    */
    @RequestMapping(value = "/queryOneMessageById.do_", method = RequestMethod.GET)
    @AccessLogin
    @ResponseBody
    public Result<MessageDetailVO> queryOneMessageById(Long messageId){
        MessageDetailVO messageDetailVO = messageService.queryOneMessageById(messageId);
        return Result.getSuccessResult( messageDetailVO);
    }
}