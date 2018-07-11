package com.zoctan.seedling.mapper;

import com.zoctan.seedling.core.mapper.MyMapper;
import com.zoctan.seedling.model.Account;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author Zoctan
 * @date 2018/05/27
 */
public interface AccountMapper extends MyMapper<Account> {

    /**
     * 按条件查询账户
     *
     * @param param 条件参数
     * @return 账户
     */
    Account findByParam(Map<String, Object> param);

    /**
     * 按账户名更新最后登陆时间
     *
     * @param name 账户名
     */
    void updateLoginTimeByName(@Param("name") String name);
}