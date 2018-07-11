package com.zoctan.seedling.core.response;

import com.alibaba.fastjson.JSON;

/**
 * 统一API响应结果封装
 *
 * @author Zoctan
 * @date 2018/06/09
 */
public class Result {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;
    /**
     * 数据内容，比如列表，实体
     */
    private Object data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }

    public Result setCode(final Integer code) {
        this.code = code;
        return this;
    }

    public Result setMessage(final String message) {
        this.message = message;
        return this;
    }

    public Result setData(final Object data) {
        this.data = data;
        return this;
    }
}
