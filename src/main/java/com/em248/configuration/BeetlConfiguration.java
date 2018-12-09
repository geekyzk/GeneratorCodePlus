package com.em248.configuration;

import cn.hutool.core.util.StrUtil;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.IOException;

public class BeetlConfiguration {

    public static GroupTemplate groupTemplate;

    private static String templatePath = "GeneratorTemplates/";

    static {
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader(templatePath);
        Configuration cfg = null;
        try {
            cfg = Configuration.defaultConfiguration();

        } catch (IOException e) {
            e.printStackTrace();
        }
        groupTemplate = new GroupTemplate(resourceLoader, cfg);
        groupTemplate.registerFunctionPackage("StringUtil",cn.hutool.core.util.StrUtil.class);
    }
}
