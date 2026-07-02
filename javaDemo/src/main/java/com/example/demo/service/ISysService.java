package com.example.demo.service;

import com.example.demo.entity.Sys;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface ISysService extends IService<Sys> {
  List<Sys> getSysList();
  boolean addSys(Sys sys);
  boolean deleteSys(Long id);
  boolean updateSys(Sys sys);
}
