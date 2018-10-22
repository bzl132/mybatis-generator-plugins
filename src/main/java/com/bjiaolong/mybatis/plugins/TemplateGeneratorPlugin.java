package com.bjiaolong.mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: long
 * @Description:
 * @Date: Create in 2018.10.13 19:49
 * @Modified By:
 * @ClassName: com.bjiaolong.mybatis.plugins.TemplateGeneratorPlugin
 */
public class TemplateGeneratorPlugin extends PluginAdapter {


    private String extDaoName;
    private String queryPackage;


    public boolean validate(List<String> list) {
        //校验参数是否正常,
        queryPackage = properties.getProperty("queryPackage");


        return true;
    }


    /**
     * 生成JAVA文件
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
        List<GeneratedJavaFile> javaFiles = new ArrayList<GeneratedJavaFile>(generatedJavaFiles);
        for (GeneratedJavaFile javaFile : javaFiles) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();
            String shortName = baseModelJavaType.getFullyQualifiedName();
            if (shortName.endsWith("DAO")) {
                generateExtDaoInterface(introspectedTable, baseModelJavaType, generatedJavaFiles);
                generateQueryObj(introspectedTable);
                generateExtDaoImpl(introspectedTable, baseModelJavaType, generatedJavaFiles);
            }
        }
        return generatedJavaFiles;
    }

    private void generateQueryObj(IntrospectedTable introspectedTable) {

    }

    private void generateExtDaoImpl(IntrospectedTable introspectedTable, FullyQualifiedJavaType baseModelJavaType, List<GeneratedJavaFile> generatedJavaFiles) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        String domainObjectName = tableConfiguration.getDomainObjectName();
        // tableConfiguration.get
        //生成扩展DAO
        JavaClientGeneratorConfiguration generatorConfiguration = context.getJavaClientGeneratorConfiguration();
        String remarks = introspectedTable.getRemarks();
        TopLevelClass extDaoImpl = new TopLevelClass(MessageFormat.format(
                "{0}.{1}{2}",
                generatorConfiguration.getImplementationPackage(),
                domainObjectName,
                "ExtDaoImpl"));
        extDaoImpl.setVisibility(JavaVisibility.PUBLIC);
        extDaoImpl.addJavaDocLine("/**");
        extDaoImpl.addJavaDocLine(" *" + remarks);

        extDaoImpl.addJavaDocLine(" * MyBatis Generator工具自动生成");
        extDaoImpl.addJavaDocLine(" */");

        extDaoImpl.addImportedType(baseModelJavaType);
        extDaoImpl.addSuperInterface(baseModelJavaType);

        FullyQualifiedJavaType queryJavaType = new FullyQualifiedJavaType(domainObjectName + "Query");
        Parameter parameter = new Parameter(queryJavaType, "condition");

        Method listMethod = new Method("selectByCondition");
        listMethod.setVisibility(JavaVisibility.DEFAULT);
        listMethod.addParameter(parameter);
        //TODO
        listMethod.addBodyLine("");

        FullyQualifiedJavaType listJavaType = new FullyQualifiedJavaType(List.class.getSimpleName());

        // introspectedTable
        FullyQualifiedJavaType typeJavaType = new FullyQualifiedJavaType(domainObjectName);
        listJavaType.addTypeArgument(typeJavaType);
        listMethod.setReturnType(listJavaType);
        listMethod.addJavaDocLine("/**");
        listMethod.addJavaDocLine(" *");
        listMethod.addJavaDocLine(" * @param conditon");
        listMethod.addJavaDocLine(" * @return 列表数据");
        listMethod.addJavaDocLine(" */");

        Method countMethod = new Method("selectByConditionForCount");
        countMethod.setVisibility(JavaVisibility.DEFAULT);
        FullyQualifiedJavaType countJavaType = new FullyQualifiedJavaType(Long.class.getName());
        countMethod.setReturnType(countJavaType);
        countMethod.addParameter(parameter);
        countMethod.addJavaDocLine("/**");
        countMethod.addJavaDocLine(" *");
        countMethod.addJavaDocLine(" * @param conditon");
        countMethod.addJavaDocLine(" * @return 总数");
        countMethod.addJavaDocLine(" */");

        //TODO
        countMethod.addBodyLine("");

        FullyQualifiedJavaType returnJavaType = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + domainObjectName);
        FullyQualifiedJavaType queryAJavaType = new FullyQualifiedJavaType(queryPackage + "." + domainObjectName + "Query");
        FullyQualifiedJavaType listAJavaType = new FullyQualifiedJavaType(List.class.getName());
        extDaoImpl.addImportedType(listAJavaType);
        extDaoImpl.addImportedType(typeJavaType);
        extDaoImpl.addImportedType(queryAJavaType);
        extDaoImpl.addImportedType(returnJavaType);

        extDaoImpl.addMethod(listMethod);
        extDaoImpl.addMethod(countMethod);

        GeneratedJavaFile extJavaFile = new GeneratedJavaFile(extDaoImpl, generatorConfiguration.getTargetProject(), javaFormatter);
        generatedJavaFiles.add(extJavaFile);
    }

    private void generateExtDaoInterface(IntrospectedTable introspectedTable, FullyQualifiedJavaType baseModelJavaType, List<GeneratedJavaFile> generatedJavaFiles) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
        String domainObjectName = tableConfiguration.getDomainObjectName();
        // tableConfiguration.get
        //生成扩展DAO
        JavaClientGeneratorConfiguration generatorConfiguration = context.getJavaClientGeneratorConfiguration();
        String remarks = introspectedTable.getRemarks();
        Interface extInterface = new Interface(MessageFormat.format(
                "{0}.{1}{2}",
                generatorConfiguration.getTargetPackage(),
                domainObjectName,
                "ExtDao"));
        extInterface.setVisibility(JavaVisibility.PUBLIC);
        extInterface.addJavaDocLine("/**");
        extInterface.addJavaDocLine(" *" + remarks);

        extInterface.addJavaDocLine(" * MyBatis Generator工具自动生成");
        extInterface.addJavaDocLine(" */");

        extInterface.addImportedType(baseModelJavaType);
        extInterface.addSuperInterface(baseModelJavaType);

        FullyQualifiedJavaType queryJavaType = new FullyQualifiedJavaType(domainObjectName + "Query");
        Parameter parameter = new Parameter(queryJavaType, "condition");

        Method listMethod = new Method("selectByCondition");
        listMethod.setVisibility(JavaVisibility.DEFAULT);
        listMethod.addParameter(parameter);

        FullyQualifiedJavaType listJavaType = new FullyQualifiedJavaType(List.class.getSimpleName());

        // introspectedTable
        FullyQualifiedJavaType typeJavaType = new FullyQualifiedJavaType(domainObjectName);
        listJavaType.addTypeArgument(typeJavaType);
        listMethod.setReturnType(listJavaType);
        listMethod.addJavaDocLine("/**");
        listMethod.addJavaDocLine(" *");
        listMethod.addJavaDocLine(" * @param conditon");
        listMethod.addJavaDocLine(" * @return 列表数据");
        listMethod.addJavaDocLine(" */");

        Method countMethod = new Method("selectByConditionForCount");
        countMethod.setVisibility(JavaVisibility.DEFAULT);
        FullyQualifiedJavaType countJavaType = new FullyQualifiedJavaType(Long.class.getName());
        countMethod.setReturnType(countJavaType);
        countMethod.addParameter(parameter);
        countMethod.addJavaDocLine("/**");
        countMethod.addJavaDocLine(" *");
        countMethod.addJavaDocLine(" * @param conditon");
        countMethod.addJavaDocLine(" * @return 总数");
        countMethod.addJavaDocLine(" */");

        FullyQualifiedJavaType returnJavaType = new FullyQualifiedJavaType(context.getJavaModelGeneratorConfiguration().getTargetPackage() + "." + domainObjectName);
        FullyQualifiedJavaType queryAJavaType = new FullyQualifiedJavaType(queryPackage + "." + domainObjectName + "Query");
        FullyQualifiedJavaType listAJavaType = new FullyQualifiedJavaType(List.class.getName());
        extInterface.addImportedType(listAJavaType);
        extInterface.addImportedType(typeJavaType);
        extInterface.addImportedType(queryAJavaType);
        extInterface.addImportedType(returnJavaType);

        extInterface.addMethod(listMethod);
        extInterface.addMethod(countMethod);

        GeneratedJavaFile extJavaFile = new GeneratedJavaFile(extInterface, generatorConfiguration.getTargetProject(), javaFormatter);
        generatedJavaFiles.add(extJavaFile);
    }


}
