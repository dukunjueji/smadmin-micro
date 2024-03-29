package com.ucar.smadmin.enums;

/**
 * @Author: 余旭东
 * @Date: 2018/10/24 16:52
 * @Description: 短信状态枚举类：0-失败，1-成功
 */
public enum SmsStatusEnum {
    /**
     * 短信发送失败
     */
    FAIL(0, "短信发送失败"),
    /**
     * 短信发送成功
     */
    SUCCESS(1, "短信发送成功"),
    /**
     * 短信发送成功
     */
    TEMPLATE_NOT_EXIST(2, "短信模板不存在"),
    /**
     * 短信发送成功
     */
    SEND(3, "短信已发送");

    private int key;
    private String value;

    SmsStatusEnum(int key, String value){
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
