package com.example.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Sys;

public interface ISysService extends IService<Sys> {
  IPage<Sys> getSysList(IPage<Sys> page);
  boolean addSys(Sys sys);
  boolean deleteSys(Long id);
  boolean updateSys(Sys sys);
}
