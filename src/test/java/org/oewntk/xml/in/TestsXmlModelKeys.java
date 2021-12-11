/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.CoreModel;
import org.oewntk.model.LibTestModelKeys;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class TestsXmlModelKeys
{
	private static final String source = System.getProperty("SOURCE");

	// private static final String source2 = System.getProperty("SOURCE2");

	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private static CoreModel model;

	@BeforeClass
	public static void init() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		File inDir = new File(source);
		// File inDir2 = new File(source2);

		model = Factory.makeCoreModel(inDir);
		System.err.println(model.info());
		System.err.println(model.counts());
	}

	@Test
	public void testEarth()
	{
		LibTestModelKeys.testEarth(model, ps);
	}

	@Test
	public void testBaroque()
	{
		LibTestModelKeys.testBaroque(model, ps);
	}

	@Test
	public void testMobile()
	{
		LibTestModelKeys.testMobile(model, ps);
	}

	@Test
	public void testBassDeep()
	{
		LibTestModelKeys.testBassDeep(model, ps);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBassShallow()
	{
		LibTestModelKeys.testBassShallow(model, ps);
	}

	@Test
	public void testRowDeep()
	{
		LibTestModelKeys.testRowDeep(model, ps);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRowShallow()
	{
		LibTestModelKeys.testRowShallow(model, ps);
	}

	@Test
	public void testCritical()
	{
		LibTestModelKeys.testCritical(model, ps);
	}
}
