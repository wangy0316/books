package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Sys;
import com.example.demo.service.ISysService;

@RestController
@RequestMapping("/sys")
public class SysController {

  @Autowired
  private ISysService sysService;

  @PostMapping("/add")
  public Result<Boolean> addSys(@RequestBody Sys sys) {
    return Result.success(sysService.addSys(sys));
  }

  @DeleteMapping("/delete/{id}")
  public Result<Boolean> deleteSys(@PathVariable Long id) {
    return Result.success(sysService.deleteSys(id));
  }

  @PutMapping("/update")
  public Result<Boolean> updateSys(@RequestBody Sys sys) {
    return Result.success(sysService.updateSys(sys));
  }

  @GetMapping("/list")
  public Result<IPage<Sys>> getSysList(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    // 分页查询
    IPage<Sys> page = new Page<>(pageNum, pageSize);
    return Result.success(sysService.getSysList(page));
  }
}
