package com.suncht.tkmybatis.generator.maven.plugin;

import com.suncht.tkmybatis.generator.maven.util.PluginUtils;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.internal.NullProgressCallback;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * 自动生成的xml文件放到另外的文件夹
 *
 * @author sunchangtan
 */
public class AutoGenXmlPlugin extends PluginAdapter {
	private GeneratedXmlFile customFile;
	private List<String> warnings;

	@Override
	public boolean validate(List<String> warnings) {
		this.warnings = warnings;
		return true;
	}

	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		customFile = sqlMap;
		//文件不存在才需要创建
		return !PluginUtils.existFile(customFile);
	}

	@Override
	public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
		if (customFile == null) {
			return null;
		}
		Document autoGenDocument = getDocument(introspectedTable);

		String autoGenFileName = "AutoGen" + customFile.getFileName();
		String targetPackage = customFile.getTargetPackage() + ".autogen";
		GeneratedXmlFile autoGen = new GeneratedXmlFile(autoGenDocument, autoGenFileName, targetPackage,
				customFile.getTargetProject(), false, context.getXmlFormatter());

		Document customDocument = getCustomFileDocument();
		//替换rootElement
		XmlElement autoRootElement = customDocument.getRootElement();
		XmlElement customRootElement = new XmlElement(autoRootElement.getName());
		for (Attribute attribute : autoRootElement.getAttributes()) {
			customRootElement.addAttribute(attribute);
		}
		customRootElement.addElement(new TextElement("<!-- 自定义SQL写在这个文件中 -->"));

		autoGenDocument.setRootElement(autoRootElement);
		customDocument.setRootElement(customRootElement);

		return Collections.singletonList(autoGen);
	}

	private Document getCustomFileDocument() {
		try {
			Class<? extends GeneratedXmlFile> clazz = customFile.getClass();
			Field field = clazz.getDeclaredField("document");
			field.setAccessible(true);
			return (Document) field.get(customFile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Document getDocument(IntrospectedTable introspectedTable) {
		XMLMapperGenerator xmlMapperGenerator = new XMLMapperGenerator();
		xmlMapperGenerator.setContext(context);
		xmlMapperGenerator.setIntrospectedTable(introspectedTable);
		xmlMapperGenerator.setProgressCallback(new NullProgressCallback());
		xmlMapperGenerator.setWarnings(warnings);

		return xmlMapperGenerator.getDocument();
	}
}
