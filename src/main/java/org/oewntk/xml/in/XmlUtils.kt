package org.oewntk.xml.`in`

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.validation.SchemaFactory
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathExpressionException
import javax.xml.xpath.XPathFactory

/**
 * XML utilities
 *
 * @author Bernard Bou
 */
internal object XmlUtils {

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
	@JvmStatic
	@Throws(SAXException::class, ParserConfigurationException::class, IOException::class)
	fun getDocument(file: File, withSchema: Boolean): Document {
		val builderFactory = DocumentBuilderFactory.newInstance()
		builderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
		builderFactory.isNamespaceAware = true

		// for DTD-based
		builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
		builderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)

		builderFactory.isValidating = withSchema
		if (withSchema) {
			val sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
			val schema = sf.newSchema(File("schema.xsd"))
			builderFactory.schema = schema
		}

		val builder = builderFactory.newDocumentBuilder()
		val doc = builder.parse(file)
		doc.documentElement.normalize()
		Tracing.psInfo.println("[Document] $file")
		return doc
	}

	/**
	 * Get first child element
	 *
	 * @param element parent element
	 * @param tag     child tag
	 * @return first child element having 'tag' tag
	 */
	@JvmStatic
	fun getFirstChildElement(element: Element, tag: String): Element {
		val nodeList = element.getElementsByTagName(tag)
		if (nodeList.length >= 1) {
			val node = nodeList.item(0)
			assert(node.nodeType == Node.ELEMENT_NODE)
			return node as Element
		}
		throw IllegalArgumentException("Element " + element.getAttribute("id") + " has no child with tag " + tag)
	}

	/**
	 * Get first child element
	 *
	 * @param element parent element
	 * @param tag     child tag
	 * @return first child element having 'tag' tag or null if there is none
	 */
	// @Nullable
	@JvmStatic
	fun getFirstOptionalChildElement(element: Element, tag: String?): Element? {
		val nodeList = element.getElementsByTagName(tag)
		if (nodeList.length >= 1) {
			val node = nodeList.item(0)
			assert(node.nodeType == Node.ELEMENT_NODE)
			return node as Element
		}
		return null
	}

	/**
	 * Get parent element
	 *
	 * @param element child element
	 * @return parent element
	 */
	// @Nullable
	fun getParentElement(element: Element): Element {
		val lexEntryNode = element.parentNode
		assert(lexEntryNode.nodeType == Node.ELEMENT_NODE)
		return lexEntryNode as Element
	}

	/**
	 * Get node list satisfying XPath expression
	 *
	 * @param expr XPath expression
	 * @param doc  W3C Document
	 * @return node list satisfying XPath expression
	 * @throws XPathExpressionException xpath
	 */
	@JvmStatic
	@Throws(XPathExpressionException::class)
	fun getXPathNodeList(expr: String, doc: Document): NodeList {
		return XPathFactory.newInstance().newXPath().compile(expr).evaluate(doc, XPathConstants.NODESET) as NodeList
	}

	/**
	 * Get element sequence
	 *
	 * @param nodeList node list
	 * @return element iterable
	 */
	@JvmStatic
	fun sequenceOf(nodeList: NodeList): Sequence<Element>? {
		if (nodeList.length == 0) {
			return null
		}
		return (0 until nodeList.length)
			.asSequence()
			.map { nodeList.item(it) }
			.filter { it is Element }
			.map { it as Element }
	}
}
