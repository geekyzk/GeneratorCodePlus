package com.em248.service.dto;

import com.em248.generator.dto.GeneratorEntityItem;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateGeneratorServerEntityRequest {

    @NotEmpty
    @NotNull
    private String osPath;

    @NotNull
    @NotEmpty
    private String entityName;

    @NotNull
    @NotEmpty
    private String tableName;

    @NotNull
    @NotEmpty
    private String packageName;

    @NotNull
    @NotEmpty
    private String remark;

    private String vuePath;

    private List<GeneratorEntityItem> items;
}
