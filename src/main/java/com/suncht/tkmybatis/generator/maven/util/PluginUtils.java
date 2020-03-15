package com.suncht.tkmybatis.generator.maven.util;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * @author sunchangtan
 */
public class PluginUtils {
    public static ShellCallback shellCallback = new DefaultShellCallback(true);

    /**
     * 类注释
     *
     * @param javaDocLines
     * @param introspectedTable
     */
    public static void classComment(List<String> javaDocLines, IntrospectedTable introspectedTable, String defaultRemarks, String suffix) {
        javaDocLines.clear();
        //设置类注释
        String remarks = isEmpty(introspectedTable.getRemarks()) ? defaultRemarks : introspectedTable.getRemarks();
        remarks += " " + suffix;
        javaDocLines.add("/**");
        javaDocLines.add(" * " + remarks);
        javaDocLines.add(" * Po代码是自动生成的，请勿修改");
        javaDocLines.add(" * @author @mbg.generated");
        javaDocLines.add(" */");
    }

    /**
     * 判断文件是否存在
     *
     * @param targetProject targetProject
     * @param targetPackage targetPackage
     * @param fileName fileName
     * @return exist=true
     */
    public static boolean existFile(String targetProject, String targetPackage, String fileName) {
        try {
            File directory = shellCallback.getDirectory(targetProject, targetPackage);
            File targetFile = new File(directory, fileName);
            return targetFile.exists();
        } catch (ShellException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean existFile(GeneratedXmlFile file){
        return existFile(file.getTargetProject(), file.getTargetPackage(), file.getFileName());
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        String property = properties.getProperty(key);
        return property == null || "".equals(property) ? defaultValue : property;
    }

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static void setMethodValue(Object instance, String methodName, Object value, Class... parameterTypes) {
        try {
            //设置key类到entity/key中
            Class<?> type = instance.getClass();
            java.lang.reflect.Method method = type.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            method.invoke(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addLombokAnnotation(TopLevelClass topLevelClass) {
        topLevelClass.addAnnotation("@Getter");
        topLevelClass.addAnnotation("@Setter");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addImportedType("lombok.Getter");
        topLevelClass.addImportedType("lombok.Setter");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");
    }
}
