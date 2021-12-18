/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelQueries;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class TestsXmlModelQueries
{
	@BeforeClass
	public static void init() throws IOException, ParserConfigurationException, SAXException
	{
		TestsXmlCommon.init();
	}

	@Test
	public void testRowByType()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "row", TestsXmlCommon.ps);
	}

	@Test
	public void testRowByPos()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "row", TestsXmlCommon.ps);
	}

	@Test
	public void testRowByTypeAndPronunciation()
	{
		LibTestModelQueries.testWordByTypeAndPronunciation(TestsXmlCommon.model, "row", TestsXmlCommon.ps);
	}

	@Test
	public void testRowByPosAndPronunciation()
	{
		LibTestModelQueries.testWordByTypeAndPronunciation(TestsXmlCommon.model, "row", TestsXmlCommon.ps);
	}


	@Test
	public void testCriticalByType()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "critical", TestsXmlCommon.ps);
	}

	@Test
	public void testCriticalByPos()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "critical", TestsXmlCommon.ps);
	}

	@Test
	public void testBassByPos()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "bass", TestsXmlCommon.ps);
	}

	@Test
	public void testBaroqueByPos()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "baroque", TestsXmlCommon.ps);
	}

	@Test
	public void testBaroqueCSByPos()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "Baroque", TestsXmlCommon.ps);
	}

	@Test
	public void testGaloreByPos()
	{
		LibTestModelQueries.testWordByType(TestsXmlCommon.model, "galore", TestsXmlCommon.ps);
	}
}
