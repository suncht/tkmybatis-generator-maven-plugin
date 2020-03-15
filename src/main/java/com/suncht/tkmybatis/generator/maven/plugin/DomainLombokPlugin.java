package com.suncht.tkmybatis.generator.maven.plugin;

import com.suncht.tkmybatis.generator.maven.util.PluginUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 给 model 添加 lombok 注释
 *
 * @author sunchangtan
 */
public class DomainLombokPlugin extends PluginAdapter {
    private static final Pattern PATTERN = Pattern.compile("(?m)^.*$");
    public static boolean useSwagger = true;
    public static List<String> ignoreFields = new ArrayList<>();

    @Override
    public boolean validate(List<String> warnings) {
//        useSwagger = "true".equalsIgnoreCase(properties.getProperty("useSwagger"));
//        String ignoreFieldStr = properties.getProperty("ignoreFields");
//        if (ignoreFieldStr != null) {
//            Collections.addAll(ignoreFields, ignoreFieldStr.split(","));
//        }
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass,
                                                 IntrospectedTable introspectedTable) {
        List<String> javaDocLines = topLevelClass.getJavaDocLines();
        String defaultRemarks = topLevelClass.getType().getShortName();
        PluginUtils.classComment(javaDocLines, introspectedTable, defaultRemarks, " Entity");

        //该代码表示在生成class的时候，向topLevelClass添加一个@Setter和@Getter注解
        PluginUtils.addLombokAnnotation(topLevelClass);
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addImportedType("lombok.Builder");
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (ignoreFields.contains(field.getName())) {
            return false;
        }

        List<String> javaDocLines = field.getJavaDocLines();
        javaDocLines.clear();
        //设置字段注释
        String remarks = PluginUtils.isEmpty(introspectedColumn.getRemarks()) ? field.getName() : introspectedColumn.getRemarks();
        Matcher matcher = PATTERN.matcher(remarks);

        javaDocLines.add("/**");
        String br = "<br/>";
        StringBuilder apiModelProperty = new StringBuilder("@ApiModelProperty(\"");
        while (matcher.find()) {
            String group = matcher.group();
            javaDocLines.add(" * " + group);
            apiModelProperty.append(group).append(br);
        }
        apiModelProperty.append("\")");
        javaDocLines.add(" */");

        int index = apiModelProperty.lastIndexOf(br);
        if (index != -1) {
            apiModelProperty = new StringBuilder(apiModelProperty.toString().replaceAll("<br/>\\s*\"\\)", "\")"));
        }

        //设置字段注释
        if (useSwagger) {
            field.addAnnotation(apiModelProperty.toString());
            topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
        }
        return true;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        //该方法在生成每一个属性的getter方法时候调用，如果我们不想生成getter，直接返回false即可；
        return false;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {
        //该方法在生成每一个属性的setter方法时候调用，如果我们不想生成setter，直接返回false即可；
        return false;
    }
}
