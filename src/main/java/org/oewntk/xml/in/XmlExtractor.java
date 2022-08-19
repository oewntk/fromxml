package org.oewntk.xml.in;

import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * Extract information from attributes in XML files or retrieve it
 *
 * @author Bernard Bou
 */
public class XmlExtractor
{
	private XmlExtractor()
	{
	}

	/**
	 * Get sensekey from element
	 *
	 * @param senseElement sense element
	 * @return sensekey
	 */
	static String getSensekey(final Element senseElement)
	{
		String id = senseElement.getAttribute(XmlNames.ID_ATTR);
		return toSensekey(id);
	}

	/**
	 * Get lexid from element
	 *
	 * @param senseElement sense element
	 * @return lexid
	 */
	static int getLexid(Element senseElement)
	{
		String id = senseElement.getAttribute(XmlNames.ID_ATTR);
		String sk = id.substring("oewn-".length());
		int b = sk.indexOf("__");
		b += 2 + 5;
		String lexid = sk.substring(b, b + 2);
		return Integer.parseInt(lexid);
	}

	/**
	 * Get adj position from element
	 *
	 * @param senseElement sense element
	 * @return adj position
	 */
	static String getAdjPosition(final Element senseElement)
	{
		return senseElement.getAttribute(XmlNames.ADJPOSITION_ATTR);
	}

	/**
	 * Get verb frames from element
	 *
	 * @param senseElement sense element
	 * @return verb frame list of numeric ids
	 */
	static String getVerbFrames(final Element senseElement)
	{
		return senseElement.getAttribute(XmlNames.VERBFRAMES_ATTR);
	}

	/**
	 * Get rank of sense in synset
	 *
	 * @param senseElement sense element
	 * @param synsetsById  synsets mapped by id, for resolution
	 * @return rank of this sense in synset
	 */
	static int getRank(final Element senseElement, final Map<String, Element> synsetsById)
	{
		Element lexElement = XmlUtils.getParentElement(senseElement);
		String lexId = lexElement.getAttribute(XmlNames.ID_ATTR);
		String synsetId = senseElement.getAttribute(XmlNames.SYNSET_ATTR);
		Element synsetElement = synsetsById.get(synsetId);
		String membersAttr = synsetElement.getAttribute(XmlNames.MEMBERS_ATTR);
		String[] members = membersAttr.split("\\s+");
		int i = 0;
		for (String member : members)
		{
			if (lexId.equals(member))
			{
				return i;
			}
			i++;
		}
		throw new RuntimeException("[E] member attr not found " + lexId);
	}

	/**
	 * Get tag count of sense
	 *
	 * @param senseElement        sense element
	 * @param tagCountsBySensekey tag counts mapped by sensekey
	 * @return tag count
	 */
	static int getTagCount(Element senseElement, Map<String, Integer> tagCountsBySensekey)
	{
		String sensekey = XmlExtractor.getSensekey(senseElement);
		Integer tagCount = tagCountsBySensekey.get(sensekey);
		if (tagCount == null)
		{
			return 0;
		}
		return tagCount;
	}

	/**
	 * Get verb templates for this sense
	 *
	 * @param senseElement          sense element
	 * @param templateIdsBySensekey template ids by sensekey
	 * @return verb template list string
	 */
	static String getVerbTemplates(Element senseElement, Map<String, int[]> templateIdsBySensekey)
	{
		String sensekey = XmlExtractor.getSensekey(senseElement);
		int[] templateIds = templateIdsBySensekey.get(sensekey);
		if (templateIds == null)
		{
			return "";
		}
		return Arrays.stream(templateIds).mapToObj(Integer::toString).collect(joining(" "));
	}

	static private final String PREFIX = "oewn-";

	static private final int PREFIX_LENGTH = PREFIX.length();

	/**
	 * Convert id to sensekey by unescaping some character sequences
	 *
	 * @param id XML id
	 * @return sensekey
	 */
	static String toSensekey(String id)
	{
		String sk = id.startsWith(PREFIX) ? id.substring(PREFIX_LENGTH) : id;
		int b = sk.indexOf("__");

		String lemma = sk.substring(0, b) //
				.replace("-ap-", "'") //
				.replace("-lb-", "(") //
				.replace("-rb-", ")") //
				.replace("-sl-", "/") //
				.replace("-cm-", ",") //
				.replace("-ex-", "!") //
				.replace("-cl-", ":") //
				.replace("-pl-", "+") //
				.replace("-sp-", "_");

		String tail = sk.substring(b + 2) //
				.replace(".", ":") //
				.replace("-ap-", "'") //
				.replace("-lb-", "(") //
				.replace("-rb-", ")") //
				.replace("-sl-", "/") //
				.replace("-cm-", ",") //
				.replace("-ex-", "!") //
				.replace("-cl-", ":") //
				.replace("-pl-", "+") //
				.replace("-sp-", "_");

		return lemma + '%' + tail;
	}
}
