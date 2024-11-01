/*
 * Copyright (c) 2024. Bernard Bou.
 */

package org.oewntk.xml.`in`

/**
 * Tags and attributes in XML files
 *
 * @author Bernard Bou
 */
@Suppress("unused")
object XmlNames {

    /**
     * DC XML namespace
     */
    @Suppress("unused")
    internal const val NS_DC: String = "https://globalwordnet.github.io/schemas/dc/"

    // elements
    const val LEXICALRESOURCE_TAG: String = "LexicalResource"

    const val LEXICON_TAG: String = "Lexicon"

    const val LEXICALENTRY_TAG: String = "LexicalEntry"

    const val SYNSET_TAG: String = "Synset"

    const val DEFINITION_TAG: String = "Definition"

    const val EXAMPLE_TAG: String = "Example"

    const val USAGE_TAG: String = "Usage"

    const val SYNSETRELATION_TAG: String = "SynsetRelation"

    const val WIKIDATA_TAG: String = "WikiData"

    const val LEMMA_TAG: String = "Lemma"

    const val SENSE_TAG: String = "Sense"

    const val SENSERELATION_TAG: String = "SenseRelation"

    const val FORM_TAG: String = "Form"

    const val PRONUNCIATION_TAG: String = "Pronunciation"

    const val SYNTACTICBEHAVIOUR_TAG: String = "SyntacticBehaviour"

    // attributes
    const val ID_ATTR: String = "id"

    const val N_ATTR: String = "n"

    const val POS_ATTR: String = "partOfSpeech"

    const val WRITTENFORM_ATTR: String = "writtenForm"

    const val VARIETY_ATTR: String = "variety"

    const val SYNSET_ATTR: String = "synset"

    const val MEMBERS_ATTR: String = "members"

    const val TARGET_ATTR: String = "target"

    const val RELTYPE_ATTR: String = "relType"

    const val LEXFILE_ATTR: String = "lexfile"

    const val ADJPOSITION_ATTR: String = "adjposition"

    const val VERBFRAMES_ATTR: String = "subcat"

    const val VERBFRAME_ATTR: String = "subcategorizationFrame"

    const val EXAMPLE_SOURCE_ATTR: String = "source"

    const val ILI_ATTR: String = "ili"
}
