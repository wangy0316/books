package com.example.demo.generator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

public class CodeGenerator {

  public static void main(String[] args) {
    FastAutoGenerator
        .create("jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", "root",
            "qwe123456")
        .globalConfig((scanner, builder) -> builder.author(scanner.apply("请输入作者名称？")).outputDir("src\\main\\java\\com\\example\\demo"))
        .packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？")))
        .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
            .entityBuilder()
            .enableLombok()
            .addTableFills(new Column("create_time", FieldFill.INSERT))
            .build())
        .templateEngine(new FreemarkerTemplateEngine())
        .execute();
  }

  protected static List<String> getTables(String tables) {
    return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
  }
}