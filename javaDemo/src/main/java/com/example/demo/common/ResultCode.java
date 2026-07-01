package com.example.demo.common;

public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    VALIDATION_ERROR(400, "参数校验失败"),
    BUSINESS_ERROR(405, "业务异常"),
    DATABASE_ERROR(501, "数据库操作异常"),
    NETWORK_ERROR(502, "网络异常");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}