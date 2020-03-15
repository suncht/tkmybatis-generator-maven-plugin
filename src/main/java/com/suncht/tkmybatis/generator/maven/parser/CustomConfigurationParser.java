package com.suncht.tkmybatis.generator.maven.parser;

import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.*;
import org.mybatis.generator.exception.XMLParserException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * 自定义的config解析
 *
 * @author sunchangtan
 */
public class CustomConfigurationParser extends ConfigurationParser {
	private List<String> warnings;
	private List<String> parseErrors;
	private Properties extraProperties;

	public CustomConfigurationParser(List<String> warnings) {
		this(null, warnings);
	}

	public CustomConfigurationParser(Properties extraProperties, List<String> warnings) {
		super(extraProperties, warnings);
		this.extraProperties = extraProperties;

		if (warnings == null) {
			this.warnings = new ArrayList<>();
		} else {
			this.warnings = warnings;
		}

		parseErrors = new ArrayList<>();
	}

	@Override
	public Configuration parseConfiguration(Reader reader) throws IOException, XMLParserException {
		InputSource is = new InputSource(reader);
		return parseConfiguration(is);
	}

	@Override
	public Configuration parseConfiguration(InputStream inputStream) throws IOException, XMLParserException {
		InputSource is = new InputSource(inputStream);
		return parseConfiguration(is);
	}

	private Configuration parseConfiguration(InputSource inputSource)
			throws IOException, XMLParserException {
		parseErrors.clear();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new ParserEntityResolver());

			ParserErrorHandler handler = new ParserErrorHandler(warnings,
					parseErrors);
			builder.setErrorHandler(handler);

			Document document = null;
			try {
				document = builder.parse(inputSource);
			} catch (SAXParseException e) {
				throw new XMLParserException(parseErrors);
			} catch (SAXException e) {
				if (e.getException() == null) {
					parseErrors.add(e.getMessage());
				} else {
					parseErrors.add(e.getException().getMessage());
				}
			}

			if (parseErrors.size() > 0) {
				throw new XMLParserException(parseErrors);
			}

			Configuration config;
			Element rootNode = document.getDocumentElement();
			DocumentType docType = document.getDoctype();
			if (rootNode.getNodeType() == Node.ELEMENT_NODE
					&& docType.getPublicId().equals(
					XmlConstants.IBATOR_CONFIG_PUBLIC_ID)) {
				config = parseIbatorConfiguration(rootNode);
			} else if (rootNode.getNodeType() == Node.ELEMENT_NODE
					&& docType.getPublicId().equals(
					XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID)) {
				config = parseMyBatisGeneratorConfiguration(rootNode);
			} else {
				throw new XMLParserException(getString("RuntimeError.5")); //$NON-NLS-1$
			}

			if (parseErrors.size() > 0) {
				throw new XMLParserException(parseErrors);
			}

			return config;
		} catch (ParserConfigurationException e) {
			parseErrors.add(e.getMessage());
			throw new XMLParserException(parseErrors);
		}
	}

	private Configuration parseIbatorConfiguration(Element rootNode)
			throws XMLParserException {
		IbatorConfigurationParser parser = new IbatorConfigurationParser(
				extraProperties);
		return parser.parseIbatorConfiguration(rootNode);
	}

	private Configuration parseMyBatisGeneratorConfiguration(Element rootNode)
			throws XMLParserException {
		CustomMyBatisGeneratorConfigurationParser parser = new CustomMyBatisGeneratorConfigurationParser(
				extraProperties);
		return parser.parseConfiguration(rootNode);
	}
}
