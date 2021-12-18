/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelKeys;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class TestsXmlModelKeys
{
	@BeforeClass
	public static void init() throws IOException, ParserConfigurationException, SAXException
	{
		TestsXmlCommon.init();
	}

	@Test
	public void testEarthMulti()
	{
		LibTestModelKeys.testEarthMulti(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testEarthMono()
	{
		LibTestModelKeys.testEarthMono(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testBaroqueMulti()
	{
		LibTestModelKeys.testBaroqueMulti(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testBaroqueMono()
	{
		LibTestModelKeys.testBaroqueMono(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testMobile()
	{
		LibTestModelKeys.testMobile(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testBassDeep()
	{
		LibTestModelKeys.testBassDeep(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBassShallow()
	{
		LibTestModelKeys.testBassShallow(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testRowDeep()
	{
		LibTestModelKeys.testRowDeep(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRowShallow()
	{
		LibTestModelKeys.testRowShallow(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testCriticalDeep()
	{
		LibTestModelKeys.testCriticalDeep(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testCriticalPos()
	{
		LibTestModelKeys.testCriticalPos(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testCriticalPWN()
	{
		LibTestModelKeys.testCriticalPWN(TestsXmlCommon.model, TestsXmlCommon.ps);
	}
}
