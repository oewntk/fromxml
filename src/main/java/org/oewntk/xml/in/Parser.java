/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.oewntk.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import static java.util.stream.Collectors.*;

public class Parser
{
	/**
	 * XPath for lex elements
	 */
	protected static final String LEX_XPATH = String.format("/%s/%s/%s", //
			XmlNames.LEXICALRESOURCE_TAG, XmlNames.LEXICON_TAG, XmlNames.LEXICALENTRY_TAG);

	/**
	 * XPath for synset elements
	 */
	protected static final String SYNSET_XPATH = String.format("/%s/%s/%s", //
			XmlNames.LEXICALRESOURCE_TAG, XmlNames.LEXICON_TAG, XmlNames.SYNSET_TAG);

	/**
	 * XPath for verb frame elements
	 */
	protected static final String VERBFRAMES_XPATH = String.format("/%s/%s/%s", //
			XmlNames.LEXICALRESOURCE_TAG, XmlNames.LEXICON_TAG, XmlNames.SYNTACTICBEHAVIOUR_TAG);

	/**
	 * W3C document
	 */
	private final Document doc;

	/**
	 * File
	 */
	private final File file;

	/**
	 * Result lexes
	 */
	private final Collection<Lex> lexes = new ArrayList<>();

	/**
	 * Result senses
	 */
	private final Collection<Sense> senses = new ArrayList<>();

	/**
	 * Result synsets
	 */
	private final Collection<Synset> synsets = new ArrayList<>();

	/**
	 * Intermediate lex id to lemma
	 */
	private final Map<String, Lex> lexesById = new HashMap<>();

	/**
	 * Constructor
	 *
	 * @param file XML file to be parsed
	 * @throws IOException                  io exception
	 * @throws SAXException                 sax exception
	 * @throws ParserConfigurationException parser configuration exception
	 */
	public Parser(final File file) throws IOException, SAXException, ParserConfigurationException
	{
		this.file = file;
		this.doc = XmlUtils.getDocument(file, false);
	}

	/**
	 * Document file
	 *
	 * @return document file
	 */
	public File getFile()
	{
		return this.file;
	}

	public CoreModel parseCoreModel() throws XPathExpressionException
	{
		makeLexes();
		makeSynsets();
		return new CoreModel(lexes, senses, synsets);
	}

	/**
	 * Make lexes
	 */
	private void makeLexes() throws XPathExpressionException
	{
		Stream<Element> stream = XmlUtils.streamOf(XmlUtils.getXPathNodeList(LEX_XPATH, doc));
		assert stream != null;
		stream.forEach(lexElement -> {

			Lex lex = getLex(lexElement);
			Sense[] lexSenses = getSenses(lexElement, lex);
			lex.setSenses(lexSenses);
			lexes.add(lex);
			senses.addAll(Arrays.asList(lexSenses));

			String lexId = lexElement.getAttribute(XmlNames.ID_ATTR);
			lexesById.put(lexId, lex);
		});
	}

	/**
	 * Make synsets
	 */
	private void makeSynsets() throws XPathExpressionException
	{
		Stream<Element> stream = XmlUtils.streamOf(XmlUtils.getXPathNodeList(SYNSET_XPATH, doc));
		assert stream != null;
		stream.forEach(synsetElement -> {

			Synset synset = getSynset(synsetElement);
			synsets.add(synset);
		});
	}

	/**
	 * Make verb frames
	 *
	 * @return verb frames
	 * @throws XPathExpressionException XPath expression exception
	 */
	public Collection<VerbFrame> parseVerbFrames() throws XPathExpressionException
	{
		Stream<Element> stream = XmlUtils.streamOf(XmlUtils.getXPathNodeList(VERBFRAMES_XPATH, doc));
		assert stream != null;
		return stream //
				.map(verbFrameElement -> {

					String id = verbFrameElement.getAttribute(XmlNames.ID_ATTR);
					String frame = verbFrameElement.getAttribute(XmlNames.VERBFRAME_ATTR);
					return new VerbFrame(id, frame);
				}) //
				.collect(toList());
	}

	/**
	 * Build synset
	 *
	 * @param synsetElement synset element
	 * @return synset
	 */
	protected Synset getSynset(Element synsetElement)
	{
		// id
		String synsetId = synsetElement.getAttribute(XmlNames.ID_ATTR);

		// type
		char type = synsetElement.getAttribute(XmlNames.POS_ATTR).charAt(0);

		// members
		String[] memberIds = synsetElement.getAttribute(XmlNames.MEMBERS_ATTR).split("\\s+");
		String[] members = Arrays.stream(memberIds).map(li -> lexesById.get(li).getLemma()).toArray(String[]::new);

		// lexfile
		String domain = synsetElement.getAttributeNS(XmlNames.NS_DC, XmlNames.LEXFILE_ATTR).split("\\.")[1];

		// definitions
		Stream<Element> definitionStream = XmlUtils.streamOf(synsetElement.getElementsByTagName(XmlNames.DEFINITION_TAG));
		assert definitionStream != null;
		String[] definitions = definitionStream.map(Node::getTextContent).toArray(String[]::new);

		// examples
		Stream<Element> exampleStream = XmlUtils.streamOf(synsetElement.getElementsByTagName(XmlNames.EXAMPLE_TAG));
		String[] examples = null;
		if (exampleStream != null)
		{
			examples = exampleStream.map(Node::getTextContent).toArray(String[]::new);
		}

		// wikidata
		Element wikidataAttr = XmlUtils.getFirstOptionalChildElement(synsetElement, XmlNames.WIKIDATA_TAG);
		String wikidata = wikidataAttr == null ? null : wikidataAttr.getTextContent();

		// synset relations
		Map<String, Set<String>> relations = null;
		Stream<Element> relationStream = XmlUtils.streamOf(synsetElement.getElementsByTagName(XmlNames.SYNSETRELATION_TAG));
		if (relationStream != null)
		{
			relations = relationStream //
					.map(e -> new SimpleEntry<>(e.getAttribute(XmlNames.RELTYPE_ATTR), e.getAttribute(XmlNames.TARGET_ATTR))).collect(groupingBy(SimpleEntry::getKey, mapping(SimpleEntry::getValue, toSet())));
		}

		return new Synset(synsetId, type, domain, members, definitions, examples, wikidata, relations);
	}

	/**
	 * Build lex
	 *
	 * @param lexElement lex element
	 * @return lex
	 */
	protected Lex getLex(Element lexElement)
	{
		//String id = lexElement.getAttribute(XmlNames.ID_ATTR);
		Element lemmaElement = XmlUtils.getFirstChildElement(lexElement, XmlNames.LEMMA_TAG);
		String lemma = lemmaElement.getAttribute(XmlNames.WRITTENFORM_ATTR);
		String code = lemmaElement.getAttribute(XmlNames.POS_ATTR);

		// morphs
		Stream<Element> morphStream = XmlUtils.streamOf(lexElement.getElementsByTagName(XmlNames.FORM_TAG));
		String[] morphs = null;
		if (morphStream != null)
		{
			morphs = morphStream //
					.map(e -> e.getAttribute(XmlNames.WRITTENFORM_ATTR)) //
					.toArray(String[]::new);
		}

		// pronunciations
		Stream<Element> pronunciationStream = XmlUtils.streamOf(lexElement.getElementsByTagName(XmlNames.PRONUNCIATION_TAG));
		Pronunciation[] pronunciations = null;
		if (pronunciationStream != null)
		{
			pronunciations = pronunciationStream //
					.map(e -> new Pronunciation(e.getTextContent(), e.getAttribute(XmlNames.VARIETY_ATTR).isEmpty() ? null : e.getAttribute(XmlNames.VARIETY_ATTR))) //
					.toArray(Pronunciation[]::new);
		}

		return new Lex(lemma, code, null).setPronunciations(pronunciations).setForms(morphs);
	}

	/**
	 * Build senses
	 *
	 * @param lexElement lex element
	 * @param lex        lex
	 * @return senses
	 */
	private Sense[] getSenses(final Element lexElement, final Lex lex)
	{
		Stream<Element> senseStream = XmlUtils.streamOf(lexElement.getElementsByTagName(XmlNames.SENSE_TAG));
		assert senseStream != null;
		final int[] i = {-1};
		return senseStream //
				.peek(s -> ++i[0]) //
				.map(e -> getSense(e, lex, lex.getType(), i[0])) //
				.toArray(Sense[]::new);
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
	protected Sense getSense(Element senseElement, Lex lex, char type, int index)
	{
		// attributes
		String id = senseElement.getAttribute(XmlNames.ID_ATTR);
		String nAttr = senseElement.getAttribute(XmlNames.N_ATTR);
		String synsetId = senseElement.getAttribute(XmlNames.SYNSET_ATTR);
		String verbFramesAttr = senseElement.getAttribute(XmlNames.VERBFRAMES_ATTR);
		String adjPositionAttr = senseElement.getAttribute(XmlNames.ADJPOSITION_ATTR);

		// sensekey
		String sensekey = XmlExtractor.toSensekey(id);

		// n
		int n = nAttr.isEmpty() ? index : Integer.parseInt(nAttr);

		// relations
		Map<String, Set<String>> relations = null;
		Stream<Element> relationStream = XmlUtils.streamOf(senseElement.getElementsByTagName(XmlNames.SENSERELATION_TAG));
		if (relationStream != null)
		{
			relations = relationStream //
					.map(e -> new SimpleEntry<>(e.getAttribute(XmlNames.RELTYPE_ATTR), XmlExtractor.toSensekey(e.getAttribute(XmlNames.TARGET_ATTR)))).collect(groupingBy(SimpleEntry::getKey, mapping(SimpleEntry::getValue, toSet())));
		}

		// verb frames
		String[] verbFrames = verbFramesAttr.isEmpty() ? null : verbFramesAttr.split("\\s");

		// adj position
		String adjPosition = adjPositionAttr.isEmpty() ? null : adjPositionAttr;

		return new Sense(sensekey, lex, type, n, synsetId, null, verbFrames, adjPosition, relations);
	}
}
