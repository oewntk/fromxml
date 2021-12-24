/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.Assert;
import org.oewntk.model.CoreModel;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.xml.parsers.ParserConfigurationException;

public class TestsXmlCommon
{
	private static final String source = System.getProperty("SOURCE");

	static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	static CoreModel model = null;

	public static void init() throws IOException, ParserConfigurationException, SAXException
	{
		if (model == null)
		{
			if (source == null)
			{
				Tracing.psErr.println("Define XML source file dir with -DSOURCE=path%n");
				Tracing.psErr.println("When running Maven tests, define the oewn.xml file in a xml directory that is child to the project directory.");
				Assert.fail();
			}
			File file = new File(source);
			Tracing.psInfo.printf("source=%s%n", file.getAbsolutePath());
			if (!file.exists())
			{
				Tracing.psErr.println("Define YAML source dir that exists");
				Assert.fail();
			}

			model = new CoreFactory(file).get();
		}
		ps.println(model.info());
		ps.println(model.counts());
	}
}
