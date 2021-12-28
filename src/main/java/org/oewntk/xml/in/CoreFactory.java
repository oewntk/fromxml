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
			return parser //
					.parseCoreModel() //
					.generateInverseRelations() //
					.setSource(parser.getFile());
		}
		catch (XPathExpressionException e)
		{
			e.printStackTrace(Tracing.psErr);
			return null;
		}
	}

	static public CoreModel makeCoreModel(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		File inDir = new File(args[0]);
		return new CoreFactory(inDir).get();
	}

	static public void main(String[] args) throws IOException, ParserConfigurationException, SAXException
	{
		CoreModel model = makeCoreModel(args);
		Tracing.psInfo.printf("[CoreModel] %s%n%s%n%s%n", model.getSource(), model.info(), model.counts());
	}
}