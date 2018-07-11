package com.zoctan.seedling.core.service;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service层基础接口，其他Service接口 请继承该接口
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public interface Service<T> {
    /**
     * 持久化
     *
     * @param model 实体
     */
    void save(T model);

    /**
     * 批量持久化
     *
     * @param models 实体列表
     */
    void save(List<T> models);

    /**
     * 通过主鍵刪除
     *
     * @param id id
     */
    void deleteById(Object id);

    /**
     * 通过实体中某个成员变量名称（非数据表中column的名称）刪除
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @throws TooManyResultsException 多条结果异常
     */
    void deleteBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 批量刪除
     * ids -> “1,2,3,4”
     *
     * @param ids ids
     */
    void deleteByIds(String ids);

    //

    /**
     * 根据条件刪除
     *
     * @param condition 条件
     */
    void deleteByCondition(Condition condition);

    /**
     * 按组件更新
     *
     * @param model 实体
     */
    void update(T model);

    /**
     * 按条件更新
     *
     * @param model     实体
     * @param condition 条件
     */
    void updateByCondition(T model, Condition condition);

    /**
     * 通过ID查找
     *
     * @param id id
     * @return 实体
     */
    T findById(Object id);

    /**
     * 通过实体中某个成员变量名称查找
     * value需符合unique约束
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 实体
     * @throws TooManyResultsException 多条结果异常
     */
    T findBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 通过多个ID查找
     * ids -> “1,2,3,4”
     *
     * @param ids ids
     * @return 实体列表
     */
    List<T> findByIds(String ids);

    /**
     * 按条件查找
     *
     * @param condition 条件
     * @return 实体列表
     */
    List<T> findByCondition(Condition condition);

    /**
     * 获取所有实体
     *
     * @return 实体列表
     */
    List<T> findAll();
}
