/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.Key;
import org.oewntk.model.LibTestModel;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

public class TestsXmlModelWords
{
	private static final boolean peekTestWords = false;

	private static final Set<String> testWords = Set.of("baroque", "Baroque", "bass", "row");

	@BeforeClass
	public static void init() throws IOException, ParserConfigurationException, SAXException
	{
		TestsXmlCommon.init();
	}

	@Test
	public void testScanLexesForTestWords()
	{
		LibTestModel.testScanLexesForTestWords(TestsXmlCommon.model, Key.W_P_A::of_t, LibTestModel::makeIndexMap, testWords, peekTestWords, TestsXmlCommon.ps);
	}

	@Test
	public void testScanLexesForTestWordsSorted()
	{
		LibTestModel.testScanLexesForTestWords(TestsXmlCommon.model, Key.W_P_A::of_t, LibTestModel::makeSortedIndexMap, testWords, peekTestWords, TestsXmlCommon.ps);
	}

	@Test
	public void testBass()
	{
		LibTestModel.testWord("bass", TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testRow()
	{
		LibTestModel.testWord("row", TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testBaroque()
	{
		LibTestModel.testWords(TestsXmlCommon.model, TestsXmlCommon.ps, "baroque", "Baroque");
	}

	@Test
	public void testEarth()
	{
		LibTestModel.testWords(TestsXmlCommon.model, TestsXmlCommon.ps, "earth", "Earth");
	}

	@Test
	public void testCritical()
	{
		LibTestModel.testWord("critical", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testHollywood()
	{
		LibTestModel.testWord("Hollywood", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testVictorian()
	{
		LibTestModel.testWord("Victorian", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testAllied()
	{
		LibTestModel.testWord("allied", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testAlliedUpper()
	{
		LibTestModel.testWord("Allied", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testAbsent()
	{
		LibTestModel.testWord("absent", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testApocryphal()
	{
		LibTestModel.testWord("apocryphal", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testUsed()
	{
		LibTestModel.testWord("used", 'a', TestsXmlCommon.model, TestsXmlCommon.ps);
	}
}
