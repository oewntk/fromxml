/*
 * Copyright (c) 2024. Bernard Bou.
 */

package org.oewntk.xml.`in`

import org.w3c.dom.Element

/**
 * Extract information from attributes in XML files or retrieve it
 *
 * @author Bernard Bou
 */
object XmlExtractor {

    /**
     * Get sensekey from element
     *
     * @param senseElement sense element
     * @return sensekey
     */
    private fun getSenseKey(senseElement: Element): String {
        val id = senseElement.getAttribute(XmlNames.ID_ATTR)
        return toSensekey(id)
    }

    /**
     * Get lexid from element
     *
     * @param senseElement sense element
     * @return lexid
     */
    fun getLexid(senseElement: Element): Int {
        val id = senseElement.getAttribute(XmlNames.ID_ATTR)
        val sk = id.substring("oewn-".length)
        var b = sk.indexOf("__")
        b += 2 + 5
        val lexid = sk.substring(b, b + 2)
        return lexid.toInt()
    }

    /**
     * Get adj position from element
     *
     * @param senseElement sense element
     * @return adj position
     */
    fun getAdjPosition(senseElement: Element): String {
        return senseElement.getAttribute(XmlNames.ADJPOSITION_ATTR)
    }

    /**
     * Get verb frames from element
     *
     * @param senseElement sense element
     * @return verb frame list of numeric ids
     */
    fun getVerbFrames(senseElement: Element): String {
        return senseElement.getAttribute(XmlNames.VERBFRAMES_ATTR)
    }

    /**
     * Get rank of sense in synset
     *
     * @param senseElement sense element
     * @param synsetsById  synsets mapped by id, for resolution
     * @return rank of this sense in synset
     */
    fun getRank(senseElement: Element, synsetsById: Map<String, Element>): Int {
        val lexElement = XmlUtils.getParentElement(senseElement)
        val lexId = lexElement.getAttribute(XmlNames.ID_ATTR)
        val synsetId = senseElement.getAttribute(XmlNames.SYNSET_ATTR)
        val synsetElement = synsetsById[synsetId]
        val membersAttr = synsetElement!!.getAttribute(XmlNames.MEMBERS_ATTR)
        val members = membersAttr.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for ((i, member) in members.withIndex()) {
            if (lexId == member) {
                return i
            }
        }
        throw RuntimeException("[E] member attr not found $lexId")
    }

    /**
     * Get tag count of sense
     *
     * @param senseElement        sense element
     * @param tagCountsBySensekey tag counts mapped by sensekey
     * @return tag count
     */
    fun getTagCount(senseElement: Element, tagCountsBySensekey: Map<String, Int>): Int {
        val sensekey = getSenseKey(senseElement)
        return tagCountsBySensekey[sensekey] ?: 0
    }

    /**
     * Get verb templates for this sense
     *
     * @param senseElement          sense element
     * @param templateIdsBySensekey template ids by sensekey
     * @return verb template list string
     */
    fun getVerbTemplates(senseElement: Element, templateIdsBySensekey: Map<String, IntArray>): String {
        val sensekey = getSenseKey(senseElement)
        return templateIdsBySensekey[sensekey]?.joinToString(" ") ?: ""
    }

    private const val PREFIX = "oewn-"

    private const val PREFIX_LENGTH = PREFIX.length

    /**
     * Convert id to sensekey by unescaping some character sequences
     * -ap- apostrophe in "L'Enfant"
     * -sl- slash in "I/O device"
     * -cm- comma in "Prince William, duke of Cumberland"
     * -ex- exclamation mark in "Yahoo!"
     * -cl- colon on "Capital: critique of political economy"
     * -pl- plus sign in "LGBT+"
     * -sp- space in "accustomed to"
     * -lb- left bracket
     * -rb- right bracket
     *
     * @param id XML id
     * @return sensekey
     */
    fun toSensekey(id: String): String {
        val sk = if (id.startsWith(PREFIX)) id.substring(PREFIX_LENGTH) else id
        val b = sk.indexOf("__")

        val lemma = sk.substring(0, b)
            .replace("-ap-", "'")
            .replace("-lb-", "(")
            .replace("-rb-", ")")
            .replace("-sl-", "/")
            .replace("-cm-", ",")
            .replace("-ex-", "!")
            .replace("-cl-", ":")
            .replace("-pl-", "+")
            .replace("-sp-", "_")

        val tail = sk.substring(b + 2)
            .replace(".", ":")
            .replace("-ap-", "'")
            .replace("-lb-", "(")
            .replace("-rb-", ")")
            .replace("-sl-", "/")
            .replace("-cm-", ",")
            .replace("-ex-", "!")
            .replace("-cl-", ":")
            .replace("-pl-", "+")
            .replace("-sp-", "_")

        return "$lemma%$tail"
    }
}
