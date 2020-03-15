package com.suncht.tkmybatis.generator.maven.plugin;

import com.suncht.tkmybatis.generator.maven.util.PluginUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 给每个Mapper生成父类
 *
 * @author sunchangtan
 */
public class ParentMapperPlugin extends PluginAdapter {
	private Interface child;

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		child = interfaze;

		JavaClientGeneratorConfiguration configuration = this.getContext().getJavaClientGeneratorConfiguration();
		String targetProject = configuration.getTargetProject();
		String targetPackage = configuration.getTargetPackage();
		String fileName = interfaze.getType().getShortName() + ".java";
		boolean existFile = PluginUtils.existFile(targetProject, targetPackage, fileName);
		//文件不存在才需要创建
		return !existFile;
	}

	@Override
	public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
		if (child == null) {
			return new ArrayList<>(0);
		}

		String packageName = child.getType().getPackageName() + ".autogen";
		String name = "Base" + child.getType().getShortNameWithoutTypeArguments();
		FullyQualifiedJavaType parentType = new FullyQualifiedJavaType(packageName + "." + name);
		Interface parent = new Interface(parentType);
		//父类导包
		parent.addImportedTypes(child.getImportedTypes());
		//父类设置子类的方法
		for (Method method : child.getMethods()) {
			parent.addMethod(method);
		}
		//父类设置为公共的
		parent.setVisibility(JavaVisibility.PUBLIC);
		parent.getJavaDocLines().add("/**");
		parent.getJavaDocLines().add(" * @mbg.generated");
		parent.getJavaDocLines().add(" */");

		GeneratedJavaFile gjf = new GeneratedJavaFile(parent,
				context.getJavaClientGeneratorConfiguration()
						.getTargetProject(),
				context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
				context.getJavaFormatter());

		//设置child的父类
		child.addSuperInterface(parentType);
		//清空子类的方法
		child.getMethods().clear();
		//清空子类导包，只需要导入父类的包就可以
		child.getImportedTypes().clear();
		child.addImportedType(parentType);

		//子类添加 @Mapper 注解
		child.addAnnotation("@Mapper");
		child.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));

		return Collections.singletonList(gjf);
	}

}
