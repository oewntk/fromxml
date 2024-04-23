/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.LibTestModelDuplicates;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TestsXmlModelDuplicates
{
	@BeforeClass
	public static void init()
	{
		TestsXmlCommon.init();
	}

	@Test
	public void testKeyOEWN()
	{
		LibTestModelDuplicates.testDuplicatesForKeyOEWN(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test(expected = AssertionError.class)
	public void testKeyPos()
	{
		LibTestModelDuplicates.testDuplicatesForKeyPos(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testKeyIC()
	{
		LibTestModelDuplicates.testDuplicatesForKeyIC(TestsXmlCommon.model, TestsXmlCommon.ps);
	}

	@Test
	public void testKeyPWN()
	{
		LibTestModelDuplicates.testDuplicatesForKeyPWN(TestsXmlCommon.model, TestsXmlCommon.ps);
	}
}
