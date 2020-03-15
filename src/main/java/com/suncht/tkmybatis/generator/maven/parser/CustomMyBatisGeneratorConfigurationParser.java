package com.suncht.tkmybatis.generator.maven.parser;

import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.xml.MyBatisGeneratorConfigurationParser;
import org.w3c.dom.Node;

import java.util.Properties;

/**
 * 功能:
 * 1. 支持解析yml
 * 2. 支持 table 标签新增加的package属性, 用于定义包
 *
 * @author sunchangtan
 */
public class CustomMyBatisGeneratorConfigurationParser extends MyBatisGeneratorConfigurationParser {

	public CustomMyBatisGeneratorConfigurationParser(Properties extraProperties) {
		super(extraProperties);
	}

	@Override
	protected void parseTable(Context context, Node node) {
		super.parseTable(context, node);
		//获取刚刚解析出来的table
//		TableConfiguration tc = context.getTableConfigurations().get(context.getTableConfigurations().size() - 1);
//
//		Properties attributes = parseAttributes(node);
//
//		//$NON-NLS-1$
//		String packageName = attributes.getProperty("package");
//		CustomTableConfiguration newConfig = new CustomTableConfiguration(context);
//		newConfig.setPackageName(packageName);
//		BeanUtils.copyProperties(newConfig, );

//		newConfig.setInsertStatementEnabled(tc.isInsertStatementEnabled());
//		newConfig.setSelectByPrimaryKeyStatementEnabled(tc.isSelectByPrimaryKeyStatementEnabled());
//		newConfig.setSelectByExampleStatementEnabled(tc.isSelectByExampleStatementEnabled());
//		newConfig.setUpdateByPrimaryKeyStatementEnabled(tc.isUpdateByPrimaryKeyStatementEnabled());
//		newConfig.setDeleteByPrimaryKeyStatementEnabled(tc.isDeleteByPrimaryKeyStatementEnabled());
//		newConfig.setDeleteByExampleStatementEnabled(tc.isDeleteByExampleStatementEnabled());
//		newConfig.setCountByExampleStatementEnabled(tc.isCountByExampleStatementEnabled());
//		newConfig.setUpdateByExampleStatementEnabled(tc.isUpdateByExampleStatementEnabled());
//		for (ColumnOverride columnOverride : tc.getColumnOverrides()) {
//			newConfig.addColumnOverride(columnOverride);
//		}
//
//		newConfig.setIgnoredColumns(tc.getIgnoredColumns());
//		newConfig.setGeneratedKey(tc.getGeneratedKey());
//		newConfig.setSelectByPrimaryKeyQueryId(tc.getSelectByPrimaryKeyQueryId());
//		newConfig.setSelectByExampleQueryId(tc.getSelectByExampleQueryId());
//		newConfig.setCatalog(tc.getCatalog());
//		newConfig.setSchema(tc.getSchema());
//		newConfig.setTableName(tc.getTableName());
//		newConfig.setDomainObjectName(tc.getDomainObjectName());
//		newConfig.setAlias(tc.getAlias());
//		newConfig.setModelType(tc.getModelType());
//		newConfig.setWildcardEscapingEnabled(tc.getWildcardEscapingEnabled());
//		newConfig.setConfiguredModelType(tc.getConfiguredModelType());
//		newConfig.setDelimitIdentifiers(tc.getDelimitIdentifiers());
//		newConfig.setDomainObjectRenamingRule(tc.getDomainObjectRenamingRule());
//		newConfig.setColumnRenamingRule(tc.getColumnRenamingRule());
//		newConfig.setIsAllColumnDelimitingEnabled(tc.getIsAllColumnDelimitingEnabled());
//		newConfig.setMapperName(tc.getMapperName());
//		newConfig.setSqlProviderName(tc.getSqlProviderName());
//		newConfig.setIgnoredColumnPatterns(tc.getIgnoredColumnPatterns());
	}
}
