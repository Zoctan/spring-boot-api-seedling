package com.zoctan.seedling.core.service;

import com.zoctan.seedling.core.exception.ResourcesNotFoundException;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service 层基础接口，其他 Service 接口 请继承该接口
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public interface Service<T> {

    /**
     * 确保实体存在
     *
     * @param id 实体id
     * @throws ResourcesNotFoundException 不存在实体异常
     */
    void assertById(Object id) throws ResourcesNotFoundException;

    /**
     * 确保实体存在
     *
     * @param entity 实体
     * @throws ResourcesNotFoundException 不存在实体异常
     */
    void assertBy(T entity) throws ResourcesNotFoundException;

    /**
     * 确保实体存在
     *
     * @param ids ids
     * @throws ResourcesNotFoundException 不存在实体异常
     */
    void assertByIds(String ids) throws ResourcesNotFoundException;

    /**
     * 确保实体存在
     *
     * @param condition 条件
     * @throws ResourcesNotFoundException 不存在实体异常
     */
    void assertByCondition(Condition condition) throws ResourcesNotFoundException;

    /**
     * 持久化
     *
     * @param entity 实体
     * @return 影响行数
     */
    int save(T entity);

    /**
     * 批量持久化
     *
     * @param entities 实体列表
     * @return 影响行数
     */
    int save(List<T> entities);

    /**
     * 通过主鍵刪除
     *
     * @param id id
     * @return 影响行数
     */
    int deleteById(Object id);

    /**
     * 通过实体中某个成员变量名称（非数据表中 column 的名称）刪除
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 影响行数
     * @throws TooManyResultsException 多条结果异常
     */
    int deleteBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 批量刪除
     * ids -> “1,2,3,4”
     *
     * @param ids ids
     * @return 影响行数
     */
    int deleteByIds(String ids);

    //

    /**
     * 根据条件刪除
     *
     * @param condition 条件
     * @return 影响行数
     */
    int deleteByCondition(Condition condition);

    /**
     * 按组件更新
     *
     * @param entity 实体
     * @return 影响行数
     */
    int update(T entity);

    /**
     * 按条件更新
     *
     * @param entity    实体
     * @param condition 条件
     * @return 影响行数
     */
    int updateByCondition(T entity, Condition condition);

    /**
     * 通过 id 查找
     *
     * @param id id
     * @return 实体
     */
    T findById(Object id);

    /**
     * 通过实体中某个成员变量名称查找
     * value 需符合 unique 约束
     *
     * @param fieldName 字段名
     * @param value     字段值
     * @return 实体
     * @throws TooManyResultsException 多条结果异常
     */
    T findBy(String fieldName, Object value) throws TooManyResultsException;

    /**
     * 通过多个 id 查找
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
