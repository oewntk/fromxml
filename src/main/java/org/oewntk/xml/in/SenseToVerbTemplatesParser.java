/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import static java.util.stream.Collectors.toList;

public class SenseToVerbTemplatesParser
{
	private static final String SENSES_VERB_TEMPLATES_TAG = "maps";
	private static final String SENSE_VERB_TEMPLATE_TAG = "map";
	private static final String SENSEKEY_ATTR = "sk";
	private static final String VERB_TEMPLATES_ATTR = "templates";

	/**
	 * XPath for sense to verb template elements
	 */
	private static final String SENSES_VERBTEMPLATES_XPATH = String.format("/%s/%s", //
			SENSES_VERB_TEMPLATES_TAG, SENSE_VERB_TEMPLATE_TAG);

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

	public Collection<Entry<String, int[]>> parse() throws XPathExpressionException
	{
		Stream<Element> stream = XmlUtils.streamOf(XmlUtils.getXPathNodeList(SENSES_VERBTEMPLATES_XPATH, doc));
		assert stream != null;
		return stream //
				.map(senseVerbTemplateElement -> { //

					var sensekey = senseVerbTemplateElement.getAttribute(SENSEKEY_ATTR);
					var idAttrs = senseVerbTemplateElement.getAttribute(VERB_TEMPLATES_ATTR).split(",");
					var ids = Arrays.stream(idAttrs) //
							.mapToInt(Integer::parseInt) //
							.toArray();
					return new SimpleEntry<>(sensekey, ids);
				}) //
				.collect(toList());
	}
}
