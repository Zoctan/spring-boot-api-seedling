package com.zoctan.seedling.core.dto;

import com.google.common.base.Converter;
import org.springframework.beans.BeanUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.ParameterizedType;

/**
 * DTO->DO抽象转换器
 *
 * @author Zoctan
 * @date 2018/11/28
 */
@SuppressWarnings("unchecked")
public abstract class AbastractConverter<DTO, DO> extends Converter<DTO, DO> {
  private final Class<DO> doClass;
  private final DTO aDTO = this.setDTO();
  private DO aDO;

  public AbastractConverter() {
    final ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
    this.doClass = (Class<DO>) pt.getActualTypeArguments()[1];
  }

  protected abstract DTO setDTO();

  @Override
  @ParametersAreNonnullByDefault
  public DO doForward(final DTO aDTO) {
    BeanUtils.copyProperties(aDTO, this.aDO);
    return this.aDO;
  }

  @Override
  @ParametersAreNonnullByDefault
  public DTO doBackward(final DO aDO) {
    BeanUtils.copyProperties(aDO, this.aDTO);
    return this.aDTO;
  }

  public DO convertToDO() {
    try {
      this.aDO = this.doClass.getDeclaredConstructor().newInstance();
      return this.convert(this.aDTO);
    } catch (final Exception ignored) {
      return null;
    }
  }

  public DTO convertFor(final DO aDO) {
    return this.reverse().convert(aDO);
  }
}
