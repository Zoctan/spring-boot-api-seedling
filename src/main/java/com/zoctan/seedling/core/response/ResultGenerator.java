package com.zoctan.seedling.core.response;

import org.springframework.http.HttpStatus;

/**
 * 响应结果生成工具
 *
 * @author Zoctan
 * @date 2018/06/09
 */
public class ResultGenerator {
    private static final String DEFAULT_UNAUTHORIZED_MESSAGE = "Need authorized";
    private static final String DEFAULT_METHOD_NOT_ALLOWED_MESSAGE = "Request method incorrect";

    public static Result genOkResult() {
        return new Result()
                .setCode(HttpStatus.OK.value());
    }

    public static Result genOkResult(final Object data) {
        return new Result()
                .setCode(HttpStatus.OK.value())
                .setData(data);
    }

    public static Result genFailedResult(final String message) {
        return new Result()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(message);
    }

    public static Result genMethodErrorResult() {
        return new Result()
                .setCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .setMessage(DEFAULT_METHOD_NOT_ALLOWED_MESSAGE);
    }

    public static Result genUnauthorizedResult() {
        return new Result()
                .setCode(HttpStatus.UNAUTHORIZED.value())
                .setMessage(DEFAULT_UNAUTHORIZED_MESSAGE);
    }

    public static Result genUnauthorizedResult(final String message) {
        return new Result()
                .setCode(HttpStatus.UNAUTHORIZED.value())
                .setMessage(message);
    }

    public static Result genInternalServerErrorResult(final String url) {
        return new Result()
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage("API [" + url + "] internal server error. Please call engineer to debug.");
    }
}
