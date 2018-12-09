package com.em248.generator.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeneratorEntity {


    private String entityName;

    private String tableName;

    private String packageName;

    private String remark;


    private List<GeneratorEntityItem> items;
}
