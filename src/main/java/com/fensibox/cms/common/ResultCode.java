package com.fensibox.cms.common;

public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 操作失败
     */
    DEFAULT_ERROR(110110, "操作失败"),

    /**
     * 系统错误
     */
    SYS_ERROR(402, "系统错误"),
    /**
     * 参数错误
     */
    PARAMS_ERROR(403, "参数错误"),

    /**
     * 无效token
     */
    INVALID_AUTHTOKEN(405, "无效的Token"),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PWD_ERROR(406, "用户名或密码错误"),

    /**
     * 验证码发送失败
     */
    SEND_SMS_CODE_FAILED(407, "验证码发送失败"),

    /**
     * 验证码错误
     */
    SMS_CODE_ERROR(408, "验证码错误"),

    /**
     * 邀请码错误
     */
    INVITE_CODE_ERROR(409, "邀请码错误"),

    /** 没有该用户 */
    NOT_USER(410, "没有该用户"),

    /** 用户已存在 */
    USER_EXISZTS(420, "用户已存在"),

    /** 商品不存在 */
    PRODUCT_NOT_EXISZTS(430, "商品不存在"),

    /** 价格不足 */
    PRICE_FAILED(450, "价格不足"),

    /** 旧密码错误 */
    OLD_PASSWORD_ERROR(470, "旧密码错误"),


    /**
     * 服务器异常
     */
    INTERNAL_SERVER_ERROR(500, "服务器异常");

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
