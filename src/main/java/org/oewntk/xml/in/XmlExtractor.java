package org.oewntk.xml.in;

import java.util.Arrays;
import java.util.Map;

import org.w3c.dom.Element;

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

	static String getSensekey(Element senseElement)
	{
		String id = senseElement.getAttribute(XmlNames.ID_ATTR);
		return toSensekey(id);

		// return senseElement.getAttribute(XmlNames.SENSEKEY_ATTR);
	}

	static int getLexid(Element senseElement)
	{
		String id = senseElement.getAttribute(XmlNames.ID_ATTR);
		String sk = id.substring("oewn-".length());
		int b = sk.indexOf("__");
		b += 2 + 5;
		String lexid = sk.substring(b, b + 2);
		return Integer.parseInt(lexid);

		// return Integer.parseInt(senseElement.getAttribute(XmlNames.LEXID_ATTR));
	}

	static String getAdjPosition(Element senseElement)
	{
		return senseElement.getAttribute(XmlNames.ADJPOSITION_ATTR);
	}

	static String getVerbFrames(Element senseElement)
	{
		return senseElement.getAttribute(XmlNames.VERBFRAMES_ATTR);
	}

	static int getOrder(Element senseElement, Map<String, Element> synsetsById)
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

	static int getTagCount(Element senseElement, Map<String, Integer> map)
	{
		String sensekey = XmlExtractor.getSensekey(senseElement);
		Integer tagCount = map.get(sensekey);
		if (tagCount == null)
		{
			return 0;
		}
		return tagCount;
	}

	static String getVerbTemplates(Element senseElement, Map<String, int[]> map)
	{
		String sensekey = XmlExtractor.getSensekey(senseElement);
		int[] templateIds = map.get(sensekey);
		if (templateIds == null)
		{
			return "";
		}
		return Arrays.stream(templateIds).mapToObj(Integer::toString).collect(joining(" "));
	}

	static private final String PREFIX = "oewn-";

	static private final int PREFIX_LENGTH = PREFIX.length();

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
				.replace("-sp-", "_");

		return lemma + '%' + tail;
	}

	static public void main(String[] args)
	{
		for (String id : new String[] { //
				"a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i__1:23:45::",  //
				"a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i__1:23:45::a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i", //
				"oewn-a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i__1:23:45::a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i", //
		})
		{
			System.out.printf("%s -> %s%n", id, toSensekey(id));
		}
	}
}
