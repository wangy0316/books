package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Sys;
import com.example.demo.mapper.SysMapper;
import com.example.demo.service.ISysService;

@Service
public class SysServiceImpl extends ServiceImpl<SysMapper, Sys> implements ISysService {
  @Override
  public List<Sys> getSysList() {
    return baseMapper.selectList();
  }

  @Override
  public boolean addSys(Sys sys) {
    return baseMapper.insert(sys) > 0;
  }

  @Override
  public boolean deleteSys(Sys sys) {
    return baseMapper.deleteById(sys) > 0;
  }

  @Override
  public boolean updateSys(Sys sys) {
    return baseMapper.updateById(sys) > 0;
  }
}
