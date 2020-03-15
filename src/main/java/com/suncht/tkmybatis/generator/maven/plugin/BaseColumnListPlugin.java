package com.suncht.tkmybatis.generator.maven.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 让自动生成的Base_Column_List支持 alias 和 as
 *
 * @author sunchangtan
 */
public class BaseColumnListPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        //定位 Base_Column_List 在哪个位置
        int baseColumnListIndex = -1;
        int blobColumnListIndex = -1;
        List<Element> elements = document.getRootElement().getElements();
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            if (element instanceof TextElement) {
                continue;
            }
            XmlElement xmlElement = (XmlElement) element;
            if (matchXmlId(xmlElement, "Base_Column_List")) {
                baseColumnListIndex = i;
            }
            if (matchXmlId(xmlElement, "Blob_Column_List")) {
                blobColumnListIndex = i;
            }
        }
        if (baseColumnListIndex > 0) {
            //Base_Column_List_Alias
            //Base_Column_List_Alias_Column_Prefix
            List<XmlElement> tempList = new ArrayList<>();
            tempList.add(makeElements(introspectedTable, "Base_Column_List_Alias", false));
            tempList.add(makeElements(introspectedTable, "Base_Column_List_Alias_Column_Prefix", true));
            elements.addAll(baseColumnListIndex + 1, tempList);
        }
        if (blobColumnListIndex > 0) {
            //Blob_Column_List_Alias
            //Blob_Column_List_Alias_Column_Prefix
            blobColumnListIndex = baseColumnListIndex > 0 ? blobColumnListIndex + 2 : blobColumnListIndex;
            List<XmlElement> tempList = new ArrayList<>();
            tempList.add(makeElements(introspectedTable, "Blob_Column_List_Alias", false));
            tempList.add(makeElements(introspectedTable, "Blob_Column_List_Alias_Column_Prefix", true));
            elements.addAll(blobColumnListIndex + 1, tempList);
        }

        return true;
    }

    private boolean matchXmlId(XmlElement element, String idValue) {
        for (Attribute attribute : element.getAttributes()) {
            boolean match = "id".equals(attribute.getName()) && idValue.equals(attribute.getValue());
            if (match) {
                return true;
            }
        }
        return false;
    }

    private XmlElement makeElements(IntrospectedTable introspectedTable, String id, boolean hasColumnPrefix) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", id));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        Iterator iter = introspectedTable.getNonBLOBColumns().iterator();

        while (iter.hasNext()) {
            String column = MyBatis3FormattingUtilities.getSelectListPhrase((IntrospectedColumn) iter.next());
            column = "${alias}." + column + (hasColumnPrefix ? " as ${columnPrefix}" + column : "");

            sb.append(column);
            if (iter.hasNext()) {
                sb.append(", ");
            }

            if (sb.length() > 80) {
                answer.addElement(new TextElement(sb.toString()));
                sb.setLength(0);
            }
        }

        if (sb.length() > 0) {
            answer.addElement(new TextElement(sb.toString()));
        }

        return answer;
    }
}
