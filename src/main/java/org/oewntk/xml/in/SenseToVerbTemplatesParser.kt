/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.w3c.dom.Document
import java.io.File
import javax.xml.xpath.XPathExpressionException

/**
 * Sense-to-verb_template parser
 *
 * @param file file
 */
class SenseToVerbTemplatesParser(
    file: File,
) {

    /**
     * W3C document
     */
    private val doc: Document = XmlUtils.getDocument(file, false)

    /**
     * Parse
     *
     * @return collection of sensekey-verb_template_ids
     * @throws XPathExpressionException xpath expression exception
     */
    @Throws(XPathExpressionException::class)
    fun parse(): Collection<Pair<String, Array<Int>>> {
        val verbTemplatesSeq = XmlUtils.sequenceOf(XmlUtils.getXPathNodeList(SENSES_VERBTEMPLATES_XPATH, doc))!!
        return verbTemplatesSeq
            .map {
                val sensekey = it.getAttribute(SENSEKEY_ATTR)
                val idAttrs = it.getAttribute(VERB_TEMPLATES_ATTR).split(",".toRegex()).dropLastWhile { it2 -> it2.isEmpty() }.toTypedArray()
                val ids = idAttrs
                    .map { it2 -> it2.toInt() }
                    .toTypedArray()
                Pair(sensekey, ids)
            }
            .toList()
    }

    companion object {

        private const val SENSES_VERB_TEMPLATES_TAG = "maps"

        private const val SENSE_VERB_TEMPLATE_TAG = "map"

        private const val SENSEKEY_ATTR = "sk"

        private const val VERB_TEMPLATES_ATTR = "templates"

        /**
         * XPath for sense to verb template elements
         */
        private const val SENSES_VERBTEMPLATES_XPATH = "/${SENSES_VERB_TEMPLATES_TAG}/${SENSE_VERB_TEMPLATE_TAG}"
    }
}
