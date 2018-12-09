package com.em248.generator;

import cn.hutool.core.util.StrUtil;
import com.em248.configuration.BeetlConfiguration;
import com.em248.generator.dto.GeneratorEntity;
import com.em248.generator.dto.GeneratorEntityItem;
import org.beetl.core.Template;

import java.util.Arrays;

public class GeneratorFactory {


    public static String generatorModel(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/Entity.template");
        template.binding("model", generatorEntity);
        return template.render();
    }


    public static String generatorRepository(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/Repository.template");
        template.binding("model", generatorEntity);
        return template.render();
    }

    public static String generatorService(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/Service.template");
        template.binding("model", generatorEntity);
        return template.render();
    }

    public static String generatorServiceImpl(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/ServiceImpl.template");
        template.binding("model", generatorEntity);
        return template.render();
    }

    public static String generatorCreateRequest(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/Create.template");
        template.binding("model", generatorEntity);
        return template.render();
    }

    public static String generatorUpdateRequest(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/Update.template");
        template.binding("model", generatorEntity);
        return template.render();
    }

    public static String generatorController(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/Controller.template");
        template.binding("model", generatorEntity);
        return template.render();
    }

    public static

    public static String generatorJsApiFile(GeneratorEntity generatorEntity) {
        Template template = BeetlConfiguration.groupTemplate.getTemplate("/api.template");
        template.binding("model", generatorEntity);
        return template.render();
    }

    public static void main(String[] args) {
        GeneratorEntity generatorEntity = new GeneratorEntity();
        generatorEntity.setEntityName("User");
        generatorEntity.setPackageName("com.em248");
        generatorEntity.setRemark("用户实体类");
        generatorEntity.setTableName("op_user");

        GeneratorEntityItem item = new GeneratorEntityItem();
        item.setName("name");
        item.setType("String");
        item.setRemark("用户名");
        generatorEntity.setItems(Arrays.asList(item));

//        generatorModel(generatorEntity);
//        generatorRepository(generatorEntity);
//        generatorService(generatorEntity);
//        generatorServiceImpl(generatorEntity);
//        generatorUpdateRequest(generatorEntity);
//        generatorCreateRequest(generatorEntity);
        generatorController(generatorEntity);
    }
}
