/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.oewntk.model.TagCount;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import static java.util.stream.Collectors.toMap;

public class SenseToTagCountsParser
{
	private static final String SENSES_TAGCOUNTS_TAG = "maps";
	private static final String SENSE_TAGCOUNT_TAG = "map";
	private static final String SENSEKEY_ATTR = "sk";
	private static final String TAGCOUNT_ATTR = "tagcount";
	private static final String SENSENUM_ATTR = "sensenum";

	/**
	 * XPath for sense to verb template elements
	 */
	private static final String SENSES_TAGCOUNTS_XPATH = String.format("/%s/%s", //
			SENSES_TAGCOUNTS_TAG, SENSE_TAGCOUNT_TAG);

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
	public SenseToTagCountsParser(final File file) throws ParserConfigurationException, IOException, SAXException
	{
		this.doc = XmlUtils.getDocument(file, false);
	}

	public Map<String, TagCount> parse() throws XPathExpressionException
	{
		return XmlUtils.streamOf(XmlUtils.getXPathNodeList(SENSES_TAGCOUNTS_XPATH, doc)) //
				.collect(toMap( //
						senseTagCountElement -> senseTagCountElement.getAttribute(SENSEKEY_ATTR), //
						senseTagCountElement -> {

							String senseNumAttr = senseTagCountElement.getAttribute(SENSENUM_ATTR);
							String tagCntAttr = senseTagCountElement.getAttribute(TAGCOUNT_ATTR);
							return new TagCount(Integer.parseInt(senseNumAttr), Integer.parseInt(tagCntAttr));
						}));
	}
}
