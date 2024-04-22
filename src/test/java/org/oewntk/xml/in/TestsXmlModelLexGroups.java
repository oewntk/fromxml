/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelLexGroups;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TestsXmlModelLexGroups
{
	@BeforeClass
	public static void init() throws IOException, ParserConfigurationException, SAXException
	{
		TestsXmlCommon.init();
	}

	@Test
	public void testCIMultipleAll()
	{
		LibTestModelLexGroups.testCIMultipleAll(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testCILemmas()
	{
		LibTestModelLexGroups.testCILemmas(TestsXmlCommon.model, "battle of verdun", TestsXmlCommon.ps);
	}

	@Test
	public void testCICounts()
	{
		LibTestModelLexGroups.testCICounts(TestsXmlCommon.model, "battle of verdun", TestsXmlCommon.ps);
	}

	@Test
	public void testCICountsFromMap()
	{
		LibTestModelLexGroups.testCICountsFromMap(TestsXmlCommon.model, "battle of verdun", TestsXmlCommon.ps);
	}

	@Test
	public void testCIHypermapWest()
	{
		LibTestModelLexGroups.testCIHypermap(TestsXmlCommon.model, "west", TestsXmlCommon.ps);
	}

	@Test
	public void testCIHypermapBaroque()
	{
		LibTestModelLexGroups.testCIHypermap(TestsXmlCommon.model, "baroque", TestsXmlCommon.ps);
	}

	@Test
	public void testCIAi()
	{
		LibTestModelLexGroups.testCILexesFor(TestsXmlCommon.model, "ai", TestsXmlCommon.ps);
	}

	@Test
	public void testCIBaroque()
	{
		LibTestModelLexGroups.testCILexesFor(TestsXmlCommon.model, "baroque", TestsXmlCommon.ps);
	}

	@Test
	public void testCIWest3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsXmlCommon.model, "West", TestsXmlCommon.ps);
	}

	@Test
	public void testCIBaroque3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsXmlCommon.model, "Baroque", TestsXmlCommon.ps);
	}

	@Test
	public void testCIAi3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsXmlCommon.model, "Ai", TestsXmlCommon.ps);
	}

	@Test
	public void testCIAbsolute3()
	{
		LibTestModelLexGroups.testCILexesFor3(TestsXmlCommon.model, "Absolute", TestsXmlCommon.ps);
	}
}
