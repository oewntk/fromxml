/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import java.util.stream.Collectors
import javax.xml.xpath.XPathExpressionException

/**
 * Sense-to-verb_template parser
 */
class SenseToVerbTemplatesParser
	(file: File?) {
	/**
	 * W3C document
	 */
	protected val doc: Document = XmlUtils.getDocument(file, false)

	/**
	 * Parse
	 *
	 * @return collection of sensekey-verb_template_ids
	 * @throws XPathExpressionException xpath expression exception
	 */
	@Throws(XPathExpressionException::class)
	fun parse(): Collection<Pair<String, Array<Int>>> {
		val stream = checkNotNull(XmlUtils.streamOf(XmlUtils.getXPathNodeList(SENSES_VERBTEMPLATES_XPATH, doc)))
		return stream //
			.map { senseVerbTemplateElement: Element ->  //
				val sensekey = senseVerbTemplateElement.getAttribute(SENSEKEY_ATTR)
				val idAttrs = senseVerbTemplateElement.getAttribute(VERB_TEMPLATES_ATTR).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				val ids = idAttrs
					.map { it.toInt() }
					.toTypedArray()
				Pair(sensekey, ids)
			}
			.collect(Collectors.toList())
	}

	companion object {
		private const val SENSES_VERB_TEMPLATES_TAG = "maps"

		private const val SENSE_VERB_TEMPLATE_TAG = "map"

		private const val SENSEKEY_ATTR = "sk"

		private const val VERB_TEMPLATES_ATTR = "templates"

		/**
		 * XPath for sense to verb template elements
		 */
		private val SENSES_VERBTEMPLATES_XPATH = String.format("/%s/%s", SENSES_VERB_TEMPLATES_TAG, SENSE_VERB_TEMPLATE_TAG)
	}
}
