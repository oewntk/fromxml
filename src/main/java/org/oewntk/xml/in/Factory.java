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
	static public Model makeModel(File file, File inDir2) throws IOException, ParserConfigurationException, SAXException
	{
		Parser parser = new Parser(file);
		CoreModel coreModel = new CoreFactory(parser).get();
		if (coreModel == null)
		{
			return null;
		}

		try
		{
			Map<String, VerbFrame> verbFramesById = parser.parseVerbFrames();
			System.err.println("[Model] verb frames");

			Map<Integer, VerbTemplate> verbTemplatesById = new VerbTemplateParser(new File(inDir2, "verbTemplates.xml")).parse();
			Map<String, int[]> senseToVerbTemplates = new SenseToVerbTemplatesParser(new File(inDir2, "senseToVerbTemplates.xml")).parse();
			System.err.println("[Model] verb templates");

			// tag counts
			Map<String, TagCount> senseToTagCounts = new SenseToTagCountsParser(new File(inDir2, "senseToTagCounts.xml")).parse();
			System.err.println("[Model] tag counts");

			return new Model(coreModel, verbFramesById, verbTemplatesById, senseToVerbTemplates, senseToTagCounts).setSources(inDir2, inDir2);
		}
		catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}

	static public Model makeModel(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		// Timing
		final long startTime = System.currentTimeMillis();

		// Heap
		boolean traceHeap = true;
		String traceHeapEnv = System.getenv("TRACEHEAP");
		if (traceHeapEnv != null)
		{
			traceHeap = Boolean.parseBoolean(traceHeapEnv);
		}
		if (traceHeap)
		{
			System.err.println(Memory.heapInfo("before maps,", Memory.Unit.M));
		}

		// Args
		File inDir = new File(args[0]);
		File inDir2 = new File(args[1]);

		// Make
		Model model = makeModel(inDir, inDir2);

		// Heap
		if (traceHeap)
		{
			System.gc();
			System.err.println(Memory.heapInfo("after maps,", Memory.Unit.M));
		}

		// Timing
		final long endTime = System.currentTimeMillis();
		System.err.println("[Time] " + (endTime - startTime) / 1000 + "s");

		return model;
	}

	static public void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException
	{
		Model model = makeModel(args);
		System.err.printf("model %s\n%s\n%s%n", model.getSource(), model.info(), model.counts());
	}

	@Override
	public Model get()
	{
		return null;
	}
}