/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.oewntk.model.*
import org.oewntk.xml.`in`.XmlExtractor.toSensekey
import org.oewntk.xml.`in`.XmlNames.LEXICALENTRY_TAG
import org.oewntk.xml.`in`.XmlNames.LEXICALRESOURCE_TAG
import org.oewntk.xml.`in`.XmlNames.LEXICON_TAG
import org.oewntk.xml.`in`.XmlNames.SYNSET_TAG
import org.oewntk.xml.`in`.XmlNames.SYNTACTICBEHAVIOUR_TAG
import org.oewntk.xml.`in`.XmlUtils.getDocument
import org.oewntk.xml.`in`.XmlUtils.getFirstChildElement
import org.oewntk.xml.`in`.XmlUtils.getFirstOptionalChildElement
import org.oewntk.xml.`in`.XmlUtils.getXPathNodeList
import org.w3c.dom.Element
import java.io.File
import javax.xml.xpath.XPathExpressionException

/**
 * XML parser
 *
 * @property file file
 */
open class Parser(
    val file: File,
) {

    /**
     * W3C document
     */
    private val doc = getDocument(file, false)

    /**
     * Document file
     *
     * @return document file
     */

    /**
     * Result lexes
     */
    private val lexes: MutableCollection<Lex> = ArrayList()

    /**
     * Result senses
     */
    private val senses: MutableCollection<Sense> = ArrayList()

    /**
     * Result synsets
     */
    private val synsets: MutableCollection<Synset> = ArrayList()

    /**
     * Intermediate lex id to lemma
     */
    private val lexesById: MutableMap<String, Lex> = HashMap()

    @Throws(XPathExpressionException::class)
    fun parseCoreModel(): CoreModel {
        makeLexes()
        makeSynsets()
        return CoreModel(lexes, senses, synsets)
    }

    /**
     * Make lexes
     */
    @Throws(XPathExpressionException::class)
    private fun makeLexes() {
        val lexSeq = XmlUtils.sequenceOf(getXPathNodeList(LEX_XPATH, doc))!!
        lexSeq
            .forEach {
                val lex = getLex(it)
                val lexSenses = getSenses(it, lex).toMutableList()
                lex.senses = lexSenses
                lexes.add(lex)
                senses.addAll(lexSenses)
                val lexId = it.getAttribute(XmlNames.ID_ATTR)
                lexesById[lexId] = lex
            }
    }

    /**
     * Make synsets
     */
    @Throws(XPathExpressionException::class)
    private fun makeSynsets() {
        val synsetSeq = XmlUtils.sequenceOf(getXPathNodeList(SYNSET_XPATH, doc))!!
        synsetSeq.forEach {
            val synset = getSynset(it)
            synsets.add(synset)
        }
    }

    /**
     * Make verb frames
     *
     * @return verb frames
     * @throws XPathExpressionException XPath expression exception
     */
    @Throws(XPathExpressionException::class)
    fun parseVerbFrames(): Collection<VerbFrame> {
        val verbFramesSeq = XmlUtils.sequenceOf(getXPathNodeList(VERBFRAMES_XPATH, doc))!!
        return verbFramesSeq
            .map {
                val id = it.getAttribute(XmlNames.ID_ATTR)
                val frame = it.getAttribute(XmlNames.VERBFRAME_ATTR)
                VerbFrame(id, frame)
            }
            .toList()
    }

    /**
     * Build synset
     *
     * @param synsetElement synset element
     * @return synset
     */
    private fun getSynset(synsetElement: Element): Synset {
        // id
        val synsetId = synsetElement.getAttribute(XmlNames.ID_ATTR)

        // type
        val type = synsetElement.getAttribute(XmlNames.POS_ATTR)[0]

        // members
        val memberIds = synsetElement.getAttribute(XmlNames.MEMBERS_ATTR).split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val members: Array<String> = memberIds
            .map { lexesById[it]!!.lemma }.toTypedArray()

        // lexfile
        // String domain = synsetElement.getAttributeNS(XmlNames.NS_DC, XmlNames.LEXFILE_ATTR).split("\\.")[1];
        val domain = synsetElement.getAttribute(XmlNames.LEXFILE_ATTR).split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        // definitions
        val definitionSeq = XmlUtils.sequenceOf(synsetElement.getElementsByTagName(XmlNames.DEFINITION_TAG))!!
        val definitions = definitionSeq
            .map { it.textContent!! }
            .toList()
            .toTypedArray()

        // examples
        val exampleSeq = XmlUtils.sequenceOf(synsetElement.getElementsByTagName(XmlNames.EXAMPLE_TAG))
        val examples = exampleSeq
            ?.map { it.textContent as String }
            ?.toList()
            ?.toTypedArray()

        // wikidata
        val wikidataAttr = getFirstOptionalChildElement(synsetElement, XmlNames.WIKIDATA_TAG)
        val wikidata = wikidataAttr?.textContent

        // synset relations
        val relationSeq = XmlUtils.sequenceOf(synsetElement.getElementsByTagName(XmlNames.SYNSETRELATION_TAG))
        val relations = relationSeq
            ?.map { it.getAttribute(XmlNames.RELTYPE_ATTR) to it.getAttribute(XmlNames.TARGET_ATTR) }
            ?.groupBy { it.first }
            ?.mapValues { it.value.map { it2 -> it2.second }.toMutableSet() }
            ?.toMutableMap()

        return Synset(synsetId, type, domain, members, definitions, examples, wikidata, relations)
    }

    /**
     * Build lex
     *
     * @param lexElement lex element
     * @return lex
     */
    private fun getLex(lexElement: Element): Lex {

        val lemmaElement = getFirstChildElement(lexElement, XmlNames.LEMMA_TAG)
        val lemma = lemmaElement.getAttribute(XmlNames.WRITTENFORM_ATTR)
        val code = lemmaElement.getAttribute(XmlNames.POS_ATTR)

        // morphs
        val morphSeq = XmlUtils.sequenceOf(lexElement.getElementsByTagName(XmlNames.FORM_TAG))
        val morphs = morphSeq
            ?.map { it.getAttribute(XmlNames.WRITTENFORM_ATTR) }
            ?.toList()
            ?.toTypedArray()

        // pronunciations
        val pronunciationSeq = XmlUtils.sequenceOf(lexElement.getElementsByTagName(XmlNames.PRONUNCIATION_TAG))
        val pronunciations = pronunciationSeq
            ?.map { Pronunciation(it.textContent, it.getAttribute(XmlNames.VARIETY_ATTR).ifEmpty { null }) }
            ?.toList()
            ?.toTypedArray()

        return Lex(lemma, code, null).apply {
            this.pronunciations = pronunciations
            this.forms = morphs
        }
    }

    /**
     * Build senses
     *
     * @param lexElement lex element
     * @param lex        lex
     * @return senses
     */
    private fun getSenses(lexElement: Element, lex: Lex): List<Sense> {
        val senseSeq = XmlUtils.sequenceOf(lexElement.getElementsByTagName(XmlNames.SENSE_TAG))!!
        return senseSeq.withIndex()
            .map { getSense(it.value, lex, lex.type, it.index) }
            .toList()
    }

    /**
     * Build sense
     *
     * @param senseElement sense element
     * @param lex          lex
     * @param type         synset type
     * @param index        index of sense in lex
     * @return sense
     */
    private fun getSense(senseElement: Element, lex: Lex?, type: Char, index: Int): Sense {
        // attributes
        val id = senseElement.getAttribute(XmlNames.ID_ATTR)
        val nAttr = senseElement.getAttribute(XmlNames.N_ATTR)
        val synsetId = senseElement.getAttribute(XmlNames.SYNSET_ATTR)
        val verbFramesAttr = senseElement.getAttribute(XmlNames.VERBFRAMES_ATTR)
        val adjPositionAttr = senseElement.getAttribute(XmlNames.ADJPOSITION_ATTR)

        // sensekey
        val sensekey = toSensekey(id)

        // n
        val n = if (nAttr.isEmpty()) index else nAttr.toInt()

        // relations
        val relationStream = XmlUtils.sequenceOf(senseElement.getElementsByTagName(XmlNames.SENSERELATION_TAG))
        val relations = relationStream
            ?.map { it.getAttribute(XmlNames.RELTYPE_ATTR) to toSensekey(it.getAttribute(XmlNames.TARGET_ATTR)) }
            ?.groupBy { it.first }
            ?.mapValues { it.value.map { it2 -> it2.second }.toMutableSet() }
            ?.toMutableMap()

        // verb frames
        val verbFrames = if (verbFramesAttr.isEmpty()) null else verbFramesAttr.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        // adj position
        val adjPosition = adjPositionAttr.ifEmpty { null }

        return Sense(sensekey, lex!!, type, n, synsetId, null, verbFrames, adjPosition, relations)
    }

    companion object {

        /**
         * XPath for lex elements
         */
        protected const val LEX_XPATH = "/$LEXICALRESOURCE_TAG/$LEXICON_TAG/$LEXICALENTRY_TAG"

        /**
         * XPath for synset elements
         */
        protected const val SYNSET_XPATH = "/$LEXICALRESOURCE_TAG/$LEXICON_TAG/$SYNSET_TAG"

        /**
         * XPath for verb frame elements
         */
        protected const val VERBFRAMES_XPATH = "/$LEXICALRESOURCE_TAG/$LEXICON_TAG/$SYNTACTICBEHAVIOUR_TAG"
    }
}
