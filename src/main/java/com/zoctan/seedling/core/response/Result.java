package com.zoctan.seedling.core.response;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Zoctan
 * @date 2018/07/15
 */
@ApiModel(value = "响应结果")
public class Result {
    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "数据")
    private Object data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
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

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }
}
