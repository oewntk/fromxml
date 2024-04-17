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

/**
 * Core model factory
 */
public class CoreFactory implements Supplier<CoreModel>
{
	private final Parser parser;

	/**
	 * Constructor
	 *
	 * @param file XML merged file
	 * @throws IOException                  io exception
	 * @throws ParserConfigurationException parser configuration exception
	 * @throws SAXException                 SAX exception
	 */
	public CoreFactory(final File file) throws IOException, ParserConfigurationException, SAXException
	{
		this(new Parser(file));
	}

	/**
	 * Constructor
	 *
	 * @param parser parser
	 */
	public CoreFactory(final Parser parser)
	{
		this.parser = parser;
	}

	@Override
	public CoreModel get()
	{
		try
		{
			return parser //
					.parseCoreModel() //
					.generateInverseRelations() //
					.setModelSource(parser.getFile());
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace(Tracing.psErr);
			return null;
		}
	}

	/**
	 * Make core model
	 *
	 * @param args command-line arguments
	 * @return core model
	 * @throws IOException                  io exception
	 * @throws ParserConfigurationException parser configuration exception
	 * @throws SAXException                 SAX exception
	 */
	static public CoreModel makeCoreModel(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		File inDir = new File(args[0]);
		return new CoreFactory(inDir).get();
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws IOException                  io exception
	 * @throws ParserConfigurationException parser configuration exception
	 * @throws SAXException                 SAX exception
	 */
	static public void main(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		CoreModel model = makeCoreModel(args);
		Tracing.psInfo.printf("[CoreModel] %s%n%s%n%s%n", model.getSource(), model.info(), model.counts());
	}
}