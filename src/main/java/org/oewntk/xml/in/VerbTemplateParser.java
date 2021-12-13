/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.oewntk.model.VerbTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import static java.util.stream.Collectors.toMap;

public class VerbTemplateParser
{
	protected static final String VERBTEMPLATES_TAG = "VerbTemplates";
	protected static final String VERBTEMPLATE_TAG = "VerbTemplate";

	/**
	 * XPath for verb template elements
	 */
	private static final String VERBTEMPLATES_XPATH = String.format("/%s/%s", //
			VERBTEMPLATES_TAG, VERBTEMPLATE_TAG);

	/**
	 * W3C document
	 */
	private final Document doc;

	/**
	 * Constructor
	 *
	 * @param file XML file to be parsed
	 * @throws IOException                  io exception
	 * @throws SAXException                 sax exception
	 * @throws ParserConfigurationException parser configuration exception
	 */
	public VerbTemplateParser(final File file) throws ParserConfigurationException, IOException, SAXException
	{
		this.doc = XmlUtils.getDocument(file, false);
	}

	public Map<Integer, VerbTemplate> parse() throws XPathExpressionException
	{
		Stream<Element> stream = XmlUtils.streamOf(XmlUtils.getXPathNodeList(VERBTEMPLATES_XPATH, doc));
		assert stream != null;
		return stream.collect(toMap( //

				verbTemplateElement -> {
					String idAttr = verbTemplateElement.getAttribute(XmlNames.ID_ATTR);
					return Integer.parseInt(idAttr);
				}, //
				verbTemplateElement -> {

					String idAttr = verbTemplateElement.getAttribute(XmlNames.ID_ATTR);
					int id = Integer.parseInt(idAttr);
					String template = verbTemplateElement.getTextContent();
					return new VerbTemplate(id, template);
				}));
	}
}
