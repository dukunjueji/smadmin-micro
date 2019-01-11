package com.ucar.smadmin.common.mq;

import com.rabbitmq.client.Channel;
import com.ucar.smadmin.base.sms.service.SmsTemplateService;
import com.ucar.smadmin.base.sms.vo.GenerateSmsVO;
import com.ucar.smadmin.common.mq.vo.MqVO;
import com.ucar.smadmin.enums.SmsStatusEnum;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 版权声明： Copyright (c) 2008 ucarinc. All Rights Reserved.
 *
 * @author 何麒（qi.he@ucarinc.com）
 * @Version 1.0
 * @date 2018/11/12
 */
@Component
@RabbitListener(queues = RabbitConfig.SMS_QUEUE)
public class SmsMqConsumer {

    @Autowired
    private SmsTemplateService smsTemplateService;

    @RabbitHandler
    public void handlerMessage(MqVO mqVO, Channel channel, Message message) throws IOException {
        //this.smsTemplateService = InjectionUtils.getInjectionInstance(SmsTemplateService.class);
        GenerateSmsVO generateSmsVO = mqVO.getGenerateSmsVO();
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        this.smsTemplateService.generateSms(generateSmsVO);
        //判断消息实体是否为空
        /*if (generateSmsVO != null) {
            Integer status = this.smsTemplateService.generateSms(generateSmsVO);
            if (status > 0) {
                *//**
                 * 手动发送确认消息
                 *//*
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                *//**
                 * 失败重新投递消息
                 *//*
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            if (status == SmsStatusEnum.FAIL.getKey()) {
                System.out.println(SmsStatusEnum.FAIL.getValue());
            } else if (status == SmsStatusEnum.TEMPLATE_NOT_EXIST.getKey()) {
                System.out.println(SmsStatusEnum.TEMPLATE_NOT_EXIST.getValue());
            } else if (status == SmsStatusEnum.SEND.getKey()) {
                System.out.println(SmsStatusEnum.SEND.getValue());
            }
        }*/
    }
}
