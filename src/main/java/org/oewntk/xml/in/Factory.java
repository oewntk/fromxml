/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.oewntk.model.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Supplier;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class Factory implements Supplier<Model>
{
	private final File file;

	private final File inDir2;

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
			Map<String, VerbFrame> verbFramesById = parser.parseVerbFrames();
			Map<Integer, VerbTemplate> verbTemplatesById = new VerbTemplateParser(new File(inDir2, "verbTemplates.xml")).parse();
			Map<String, int[]> senseToVerbTemplates = new SenseToVerbTemplatesParser(new File(inDir2, "senseToVerbTemplates.xml")).parse();

			// tag counts
			Map<String, TagCount> senseToTagCounts = new SenseToTagCountsParser(new File(inDir2, "senseToTagCounts.xml")).parse();

			return new Model(coreModel, verbFramesById, verbTemplatesById, senseToVerbTemplates, senseToTagCounts).setSources(inDir2, inDir2);
		}
		catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	static public Model makeModel(String[] args)
	{
		File inDir = new File(args[0]);
		File inDir2 = new File(args[1]);
		return new Factory(inDir, inDir2).get();
	}

	static public void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException
	{
		Model model = makeModel(args);
		System.out.printf("model %s\n%s\n%s%n", model.getSource(), model.info(), model.counts());
	}
}