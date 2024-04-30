/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.oewntk.model.TagCount
import org.oewntk.xml.`in`.XmlUtils.getDocument
import org.oewntk.xml.`in`.XmlUtils.getXPathNodeList
import java.io.File
import javax.xml.xpath.XPathExpressionException

/**
 * Sense-to-tag_count parser
 */
class SenseToTagCountsParser
    (file: File?) {

    /**
     * W3C document
     */
    private val doc = getDocument(file!!, false)

    /**
     * Parse
     *
     * @return collection of sensekey-tag_count pairs
     * @throws XPathExpressionException xpath expression exception
     */
    @Throws(XPathExpressionException::class)
    fun parse(): Collection<Pair<String, TagCount>> {
        val tagCountSeq = XmlUtils.sequenceOf(getXPathNodeList(SENSES_TAGCOUNTS_XPATH, doc))!!
        return tagCountSeq
            .map {
                val sensekey = it.getAttribute(SENSEKEY_ATTR)
                val senseNumAttr = it.getAttribute(SENSENUM_ATTR)
                val tagCntAttr = it.getAttribute(TAGCOUNT_ATTR)
                val tagCount = TagCount(senseNumAttr.toInt(), tagCntAttr.toInt())
                Pair<String, TagCount>(sensekey, tagCount)
            }
            .toList()
    }

    companion object {

        private const val SENSES_TAGCOUNTS_TAG = "maps"

        private const val SENSE_TAGCOUNT_TAG = "map"

        private const val SENSEKEY_ATTR = "sk"

        private const val TAGCOUNT_ATTR = "tagcount"

        private const val SENSENUM_ATTR = "sensenum"

        /**
         * XPath for sense to verb template elements
         */
        private val SENSES_TAGCOUNTS_XPATH = String.format("/%s/%s", SENSES_TAGCOUNTS_TAG, SENSE_TAGCOUNT_TAG)
    }
}
