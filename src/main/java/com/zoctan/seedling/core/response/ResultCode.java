package com.zoctan.seedling.core.response;

/**
 * 响应状态码枚举类
 *
 * @author Zoctan
 * @date 2018/07/14
 */
public enum ResultCode {

    /**
     * {@code 4001 Need authorized}
     */
    UNAUTHORIZED(4001, "Need authorized"),

    /**
     * {@code 4000 Succeed request, but result not expired}
     */
    SUCCEED_REQUEST_FAILED_RESULT(4000, "Succeed request, but result not expired"),

    /**
     * {@code 5000 Service error}
     */
    SERVICE_ERROR(5000, "Service error");

    private final int value;

    private final String reason;

    ResultCode(final int value, final String reason) {
        this.value = value;
        this.reason = reason;
    }

    public int getValue() {
        return this.value;
    }

    public String getReason() {
        return this.reason;
    }
}