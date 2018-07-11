package com.zoctan.seedling.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.zoctan.seedling.model.Account;
import com.zoctan.seedling.model.Role;

import java.util.Arrays;
import java.util.Collections;

/**
 * Json工具
 *
 * @author Zoctan
 * @date 2018/07/11
 */
public class JsonUtils {
    public static void main(final String[] args) {
        final Account account = new Account();
        account.setName("hello");
        account.setPassword("123456");
        account.setId(123L);
        account.setRoleList(Collections.singletonList(new Role()));
        System.out.println(JsonUtils.keepFields(account, "password", "id", "roleList"));
        System.out.println(JsonUtils.deleteFields(account, "password", "id", "roleList"));
    }

    private JsonUtils() {

    }

    /**
     * 保留某些字段
     *
     * @param target 目标对象
     * @param fields 字段
     * @return 保留字段后的Json
     */
    public static String keepFields(final Object target, final String... fields) {
        final SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getIncludes().addAll(Arrays.asList(fields));
        return JSONObject.toJSONString(target, filter);
    }

    /**
     * 去除某些字段
     *
     * @param target 目标对象
     * @param fields 字段
     * @return 去除字段后的Json
     */
    public static String deleteFields(final Object target, final String... fields) {
        final SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().addAll(Arrays.asList(fields));
        return JSONObject.toJSONString(target, filter);
    }
}
