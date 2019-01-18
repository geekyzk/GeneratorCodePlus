package com.em248.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.em248.controller.BackException;
import com.em248.generator.GeneratorFactory;
import com.em248.generator.dto.GeneratorEntity;
import com.em248.service.dto.CreateGeneratorServerEntityRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.nio.ch.IOUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;

@Service
@Slf4j
public class GeneratorService {


    public void generatorServerEntity(CreateGeneratorServerEntityRequest request) {
        GeneratorEntity generatorEntity = new GeneratorEntity();
        BeanUtil.copyProperties(request, generatorEntity, CopyOptions.create().setIgnoreNullValue(false));
        String osPath = request.getOsPath();
        if (!FileUtil.exist(osPath)) {
            throw new BackException("项目main/java路径不存在，请输入正确项目根路径");
        }
        if (osPath.endsWith("/") || osPath.endsWith("\\")) {
            osPath = osPath.substring(0, osPath.length() - 1);
        }
        String packageName = request.getPackageName();
        String sourcePath = osPath + File.separator +  String.join(File.separator, packageName.split("\\."));
        if (!FileUtil.exist(sourcePath)) {
            FileUtil.mkdir(sourcePath);
        }

        generatorFile(GeneratorFactory.generatorModel(generatorEntity),
                request.getEntityName() + ".java",
                sourcePath + File.separator + "model");
        generatorFile(GeneratorFactory.generatorRepository(generatorEntity),
                request.getEntityName() + "Repository.java",
                sourcePath + File.separator + "repository");
        generatorFile(GeneratorFactory.generatorService(generatorEntity),
                request.getEntityName() + "Service.java",
                sourcePath + File.separator + "service");
        generatorFile(GeneratorFactory.generatorServiceImpl(generatorEntity),
                request.getEntityName() + "ServiceImpl.java",
                sourcePath + File.separator + "service" + File.separator + "impl");
        generatorFile(GeneratorFactory.generatorUpdateRequest(generatorEntity),
                "Update" + request.getEntityName() + "Request.java",
                sourcePath + File.separator + "service" + File.separator + "dto");
        generatorFile(GeneratorFactory.generatorCreateRequest(generatorEntity),
                "Create" + request.getEntityName() + "Request.java",
                sourcePath + File.separator + "service" + File.separator + "dto");
        generatorFile(GeneratorFactory.generatorCriteria(generatorEntity),
                request.getEntityName() + "Criteria.java",
                sourcePath + File.separator + "service" + File.separator + "dto" + File.separator + "query");
        generatorFile(GeneratorFactory.generatorController(generatorEntity),
                request.getEntityName() + "Controller.java",
                sourcePath + File.separator + "controller");

        String vuePath = request.getVuePath();
        if (StrUtil.isNotBlank(vuePath)) {
            generatorFile(GeneratorFactory.generatorJsApiFile(generatorEntity),
                    request.getEntityName() + "Api.js",
                    vuePath + File.separator + "api");
            generatorFile(GeneratorFactory.generatorVueFile(generatorEntity),
                    request.getEntityName() + ".vue",
                    vuePath + File.separator + "views" + File.separator + request.getEntityName().toLowerCase());

        }

        String entityJson = JSONUtil.toJsonPrettyStr(request);
        generatorFile(entityJson,'.' + request.getEntityName() + ".json",sourcePath + File.separator + "model");
    }


    private void generatorFile(String content,String fileName, String path) {
        log.info("create file path {}",path);
        File codeFolder = FileUtil.mkdir(path);
        File codeFile = FileUtil.touch(codeFolder, fileName);
        try {
            IoUtil.write(new FileOutputStream(codeFile), Charset.forName("utf-8"), true, content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
