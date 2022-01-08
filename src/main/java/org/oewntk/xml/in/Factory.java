/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.oewntk.model.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

/**
 * Model factory
 */
public class Factory implements Supplier<Model>
{
	private final File file;

	private final File inDir2;

	/**
	 * Constructor
	 *
	 * @param file   file
	 * @param inDir2 dir where extra YAML files
	 */
	public Factory(final File file, final File inDir2)
	{
		this.file = file;
		this.inDir2 = inDir2;
	}

	@Override
	public Model get()
	{
		try
		{
			Parser parser = new Parser(file);
			CoreModel coreModel = new CoreFactory(parser).get();
			if (coreModel == null)
			{
				return null;
			}
			Collection<VerbFrame> verbFrames = parser.parseVerbFrames();
			Collection<VerbTemplate> verbTemplates = new VerbTemplateParser(new File(inDir2, "verbTemplates.xml")).parse();
			Collection<Entry<String, int[]>> senseToVerbTemplates = new SenseToVerbTemplatesParser(new File(inDir2, "senseToVerbTemplates.xml")).parse();

			// tag counts
			Collection<Entry<String, TagCount>> senseToTagCounts = new SenseToTagCountsParser(new File(inDir2, "senseToTagCounts.xml")).parse();

			return new Model(coreModel, verbFrames, verbTemplates, senseToVerbTemplates, senseToTagCounts).setSources(file, inDir2);
		}
		catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e)
		{
			e.printStackTrace(Tracing.psErr);
			return null;
		}
	}

	/**
	 * Make model
	 *
	 * @param args command-line arguments
	 * @return model
	 */
	static public Model makeModel(String[] args)
	{
		File inDir = new File(args[0]);
		File inDir2 = new File(args[1]);
		return new Factory(inDir, inDir2).get();
	}

	/**
	 * Make core model
	 *
	 * @param args command-line arguments
	 */
	static public void main(String[] args)
	{
		Model model = makeModel(args);
		Tracing.psInfo.printf("[Model] %s%n%s%n%s%n", Arrays.toString(model.getSources()), model.info(), model.counts());
	}
}