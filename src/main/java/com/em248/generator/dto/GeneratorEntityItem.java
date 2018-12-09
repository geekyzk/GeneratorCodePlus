package com.em248.generator.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GeneratorEntityItem {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String remark;

    @NotNull
    @NotEmpty
    private String type;

    private Boolean isRequire = false;

    private Boolean isUnique = false;


}
