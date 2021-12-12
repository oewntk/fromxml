/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.oewntk.model.CoreModel;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

public class CoreFactory implements Supplier<CoreModel>
{
	private final Parser parser;

	public CoreFactory(final File file) throws IOException, ParserConfigurationException, SAXException
	{
		this(new Parser(file));
	}

	public CoreFactory(final Parser parser)
	{
		this.parser = parser;
	}

	@Override
	public CoreModel get()
	{
		try
		{
			CoreModel model = parser.parseCoreModel();
			return model.setSource(parser.getFile());
		}
		catch (XPathExpressionException e)
		{
			System.err.println(e.getMessage());
			return null;
		}
	}

	static public CoreModel makeCoreModel(String[] args) throws IOException, ParserConfigurationException, SAXException
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

		// Make
		CoreModel model = new CoreFactory(inDir).get();

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
		CoreModel model = makeCoreModel(args);
		System.err.printf("model %s\n%s\n%s%n", model.getSource(), model.info(), model.counts());
	}
}