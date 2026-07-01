package com.example.demo.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user")
public class User {
  private Long id;
  private String name;
  private Integer age;

  @TableField("create_time")
  private LocalDateTime createTime;
  @TableField("update_time")
  private LocalDateTime updateTime;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Integer getAge() {
    return age;
  }
  public void setAge(Integer age) {
    this.age = age;
  }
  
}
