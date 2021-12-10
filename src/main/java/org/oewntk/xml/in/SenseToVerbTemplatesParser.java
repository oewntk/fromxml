/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import static java.util.stream.Collectors.toMap;

public class SenseToVerbTemplatesParser
{
	private static final String SENSES_VERBTEMPLATES_TAG = "maps";
	private static final String SENSE_VERBTEMPLATE_TAG = "map";
	private static final String SENSEKEY_ATTR = "sk";
	private static final String VERBTEMPLATES_ATTR = "templates";

	/**
	 * XPath for sense to verb template elements
	 */
	private static final String SENSES_VERBTEMPLATES_XPATH = String.format("/%s/%s", //
			SENSES_VERBTEMPLATES_TAG, SENSE_VERBTEMPLATE_TAG);

	/**
	 * W3C document
	 */
	protected final Document doc;

	/**
	 * Constructor
	 *
	 * @param file XML file to be parsed
	 * @throws IOException                  io exception
	 * @throws SAXException                 sax exception
	 * @throws ParserConfigurationException parser configuration exception
	 */
	public SenseToVerbTemplatesParser(final File file) throws ParserConfigurationException, IOException, SAXException
	{
		this.doc = XmlUtils.getDocument(file, false);
	}

	public Map<String, int[]> parse() throws XPathExpressionException
	{
		return XmlUtils.streamOf(XmlUtils.getXPathNodeList(SENSES_VERBTEMPLATES_XPATH, doc)) //
				.collect(toMap( //
						senseVerbTemplateElement -> senseVerbTemplateElement.getAttribute(SENSEKEY_ATTR), //
						senseVerbTemplateElement -> {

							String idAttr = senseVerbTemplateElement.getAttribute(VERBTEMPLATES_ATTR);
							String[] idAttrs = idAttr.split(",");
							return Arrays.stream(idAttrs) //
									.mapToInt(Integer::parseInt) //
									.toArray();
						}));
	}
}
