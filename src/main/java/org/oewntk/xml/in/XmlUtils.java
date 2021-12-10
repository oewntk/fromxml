package org.oewntk.xml.in;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * XML utilities
 *
 * @author Bernard Bou
 */
class XmlUtils
{
	private XmlUtils()
	{
	}

	/**
	 * Build W3C Document from file
	 *
	 * @param file       file
	 * @param withSchema whether to validate document when building it (long)
	 * @return W3C Document
	 * @throws SAXException                 sax
	 * @throws ParserConfigurationException parser configuration
	 * @throws IOException                  io
	 */
	static Document getDocument(File file, @SuppressWarnings("SameParameterValue") boolean withSchema) throws SAXException, ParserConfigurationException, IOException
	{
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		builderFactory.setNamespaceAware(true);

		// for DTD-based
		builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		builderFactory.setValidating(withSchema);
		if (withSchema)
		{
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sf.newSchema(new File("schema.xsd"));
			builderFactory.setSchema(schema);
		}

		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		Document doc = builder.parse(file);
		doc.getDocumentElement().normalize();
		System.err.println("Document " + file);
		return doc;
	}

	/**
	 * Get first child element
	 *
	 * @param element parent element
	 * @param tag     child tag
	 * @return first child element having 'tag' tag
	 */
	static Element getFirstChildElement(Element element, String tag)
	{
		NodeList nodeList = element.getElementsByTagName(tag);
		if (nodeList.getLength() >= 1)
		{
			Node node = nodeList.item(0);
			assert node.getNodeType() == Node.ELEMENT_NODE;
			return (Element) node;
		}
		throw new IllegalArgumentException("Element " + element.getAttribute("id") + " has no child with tag " + tag);
	}

	/**
	 * Get first child element
	 *
	 * @param element parent element
	 * @param tag     child tag
	 * @return first child element having 'tag' tag or null if there is none
	 */
	// @Nullable
	static Element getFirstOptionalChildElement(Element element, String tag)
	{
		NodeList nodeList = element.getElementsByTagName(tag);
		if (nodeList.getLength() >= 1)
		{
			Node node = nodeList.item(0);
			assert node.getNodeType() == Node.ELEMENT_NODE;
			return (Element) node;
		}
		return null;
	}

	/**
	 * Get parent element
	 *
	 * @param element child element
	 * @return parent element
	 */
	// @Nullable
	static Element getParentElement(Element element)
	{
		Node lexEntryNode = element.getParentNode();
		assert lexEntryNode.getNodeType() == Node.ELEMENT_NODE;
		return (Element) lexEntryNode;
	}

	/**
	 * Get node list satisfying XPath expression
	 *
	 * @param expr XPath expression
	 * @param doc  W3C Document
	 * @return node list satisfying XPath expression
	 * @throws XPathExpressionException xpath
	 */
	static NodeList getXPathNodeList(String expr, Document doc) throws XPathExpressionException
	{
		return (NodeList) XPathFactory.newInstance().newXPath().compile(expr).evaluate(doc, XPathConstants.NODESET);
	}

	/**
	 * Get element stream
	 *
	 * @param nodeList nodelist
	 * @return element iterable
	 */
	static Stream<Element> streamOf(final NodeList nodeList)
	{
		if (nodeList.getLength() == 0)
		{
			return null;
		}
		return IntStream.range(0, nodeList.getLength()).mapToObj(nodeList::item).map(n -> (Element) n);
	}
}
