/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.oewntk.model.VerbTemplate
import org.oewntk.xml.`in`.XmlUtils.getDocument
import org.oewntk.xml.`in`.XmlUtils.getXPathNodeList
import java.io.File
import javax.xml.xpath.XPathExpressionException

/**
 * Verb template parser
 */
class VerbTemplateParser(
    file: File,
) {

    /**
     * W3C document
     */
    private val doc = getDocument(file, false)

    /**
     * Parse
     *
     * @return collection of verb templates
     * @throws XPathExpressionException xpath expression exception
     */
    @Throws(XPathExpressionException::class)
    fun parse(): Collection<VerbTemplate> {
        val verbTemplatesSeq = XmlUtils.sequenceOf(getXPathNodeList(VERBTEMPLATES_XPATH, doc))!!
        return verbTemplatesSeq
            .map {
                val idAttr = it.getAttribute(XmlNames.ID_ATTR)
                val id = idAttr.toInt()
                val template = it.textContent
                VerbTemplate(id, template)
            }
            .toList()
    }

    companion object {

        private const val VERB_TEMPLATES_TAG: String = "VerbTemplates"

        private const val VERB_TEMPLATE_TAG: String = "VerbTemplate"

        /**
         * XPath for verb template elements
         */
        private const val VERBTEMPLATES_XPATH = "/${VERB_TEMPLATES_TAG}/${VERB_TEMPLATE_TAG}"
    }
}
