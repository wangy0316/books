package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Sys;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author www
 * @since 2026-07-01
 */
@Mapper
public interface SysMapper extends BaseMapper<Sys> {
  List<Sys> selectList();
}
