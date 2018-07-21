package com.zoctan.seedling.core.service;

import com.zoctan.seedling.core.exception.ResourcesNotFoundException;
import com.zoctan.seedling.core.exception.ServiceException;
import com.zoctan.seedling.core.mapper.MyMapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

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
    private final Class<T> entityClass;
    @Autowired
    protected MyMapper<T> mapper;

    protected AbstractService() {
        final ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        //noinspection unchecked
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public void assertById(final Object id) throws ResourcesNotFoundException {
        Optional.ofNullable(this.mapper.selectByPrimaryKey(id))
                .orElseThrow(ResourcesNotFoundException::new);
    }

    @Override
    public void assertBy(final T entity) throws ResourcesNotFoundException {
        Optional.ofNullable(this.mapper.select(entity))
                .orElseThrow(ResourcesNotFoundException::new);
    }

    @Override
    public void assertByIds(final String ids) throws ResourcesNotFoundException {
        Optional.ofNullable(this.mapper.selectByIds(ids))
                .orElseThrow(ResourcesNotFoundException::new);
    }


    @Override
    public void assertByCondition(final Condition condition) throws ResourcesNotFoundException {
        Optional.ofNullable(this.mapper.selectByCondition(condition))
                .orElseThrow(ResourcesNotFoundException::new);
    }

    @Override
    public int save(final T entity) {
        return this.mapper.insertSelective(entity);
    }

    @Override
    public int save(final List<T> entities) {
        return this.mapper.insertList(entities);
    }

    @Override
    public int deleteById(final Object id) {
        this.assertById(id);

        return this.mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBy(final String fieldName, final Object value) throws TooManyResultsException {
        try {
            final T entity = this.entityClass.newInstance();
            final Field field = this.entityClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);

            this.assertBy(entity);

            return this.mapper.delete(entity);
        } catch (final ReflectiveOperationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public int deleteByIds(final String ids) {
        this.assertByIds(ids);

        return this.mapper.deleteByIds(ids);
    }

    @Override
    public int deleteByCondition(final Condition condition) {
        this.assertByCondition(condition);

        return this.mapper.deleteByCondition(condition);
    }

    @Override
    public int update(final T entity) {
        this.assertBy(entity);

        return this.mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int updateByCondition(final T entity, final Condition condition) {
        this.assertByCondition(condition);

        return this.mapper.updateByConditionSelective(entity, condition);
    }

    @Override
    public T findById(final Object id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    @Override
    public T findBy(final String fieldName, final Object value) throws TooManyResultsException {
        try {
            final T entity = this.entityClass.newInstance();
            final Field field = this.entityClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
            return this.mapper.selectOne(entity);
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
