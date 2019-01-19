package com.bjiaolong.mybatis.plugins;

import org.junit.Before;
import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestGenerator {

    private File configFile;

    @Before
    public void before() {
        //读取mybatis参数
        configFile = new File("D:\\Project\\Activiti\\mybatisgeneratorplugins\\mybatisGeneratorPlugins\\src\\main\\resources\\generator-mybatis.xml");

//        configFile = new File("/Users/zhangsiyuan/Documents/MybatisFun/Mybatis-Chapter9-GeneratorPlugin/src/main/resources/mybatisConfig.xml");
    }

    @Test
    public void test() throws Exception{
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }
}