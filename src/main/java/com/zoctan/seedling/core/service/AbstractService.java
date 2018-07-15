package com.zoctan.seedling.core.service;


import com.zoctan.seedling.core.exception.ServiceException;
import com.zoctan.seedling.core.mapper.MyMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 基于通用 MyBatis Mapper 插件的 Service 接口的实现
 *
 * @author Zoctan
 * @date 2018/05/27
 */
public abstract class AbstractService<T> implements Service<T> {
    /**
     * 当前泛型的实体 Class
     */
    private final Class<T> modelClass;
    @Autowired
    protected MyMapper<T> mapper;

    protected AbstractService() {
        final ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        //noinspection unchecked
        this.modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public int save(final T model) {
        return this.mapper.insertSelective(model);
    }

    @Override
    public int save(final List<T> models) {
        return this.mapper.insertList(models);
    }

    @Override
    public int deleteById(final Object id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBy(final String fieldName, final Object value) throws TooManyResultsException {
        try {
            final T model = this.modelClass.newInstance();
            final Field field = this.modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return this.mapper.delete(model);
        } catch (final ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public int deleteByIds(final String ids) {
        return this.mapper.deleteByIds(ids);
    }

    @Override
    public int deleteByCondition(final Condition condition) {
        return this.mapper.deleteByCondition(condition);
    }

    @Override
    public int update(final T model) {
        return this.mapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public int updateByCondition(final T model, final Condition condition) {
        return this.mapper.updateByConditionSelective(model, condition);
    }

    @Override
    public T findById(final Object id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public T findBy(final String fieldName, final Object value) throws TooManyResultsException {
        try {
            final T model = this.modelClass.newInstance();
            final Field field = this.modelClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(model, value);
            return this.mapper.selectOne(model);
        } catch (final ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<T> findByIds(final String ids) {
        return this.mapper.selectByIds(ids);
    }

    @Override
    public List<T> findByCondition(final Condition condition) {
        return this.mapper.selectByCondition(condition);
    }

    @Override
    public List<T> findAll() {
        return this.mapper.selectAll();
    }
}
