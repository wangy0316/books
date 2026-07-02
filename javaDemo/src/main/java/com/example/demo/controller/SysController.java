package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.service.ISysService;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.entity.Sys;
import com.example.demo.common.Result;
import java.util.List;

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
  public Result<List<Sys>> getSysList() {
    return Result.success(sysService.getSysList());
  }
}
