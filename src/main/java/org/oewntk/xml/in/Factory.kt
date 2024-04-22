/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.oewntk.model.Model
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.function.Supplier
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPathExpressionException

/**
 * Model factory
 *
 * @property file   file
 * @property inDir2 dir where extra YAML files
 */
class Factory(
	private val file: File,
	private val inDir2: File
) : Supplier<Model?> {

	override fun get(): Model? {
		try {
			val parser = Parser(file)
			val coreModel = CoreFactory(parser).get() ?: return null
			val verbFrames = parser.parseVerbFrames()
			val verbTemplates = VerbTemplateParser(File(inDir2, "verbTemplates.xml")).parse()
			val senseToVerbTemplates = SenseToVerbTemplatesParser(File(inDir2, "senseToVerbTemplates.xml")).parse()

			// tag counts
			val senseToTagCounts = SenseToTagCountsParser(File(inDir2, "senseToTagCounts.xml")).parse()

			return Model(coreModel, verbFrames, verbTemplates, senseToVerbTemplates, senseToTagCounts).setSources(file, inDir2)
		} catch (e: IOException) {
			e.printStackTrace(Tracing.psErr)
			return null
		} catch (e: ParserConfigurationException) {
			e.printStackTrace(Tracing.psErr)
			return null
		} catch (e: SAXException) {
			e.printStackTrace(Tracing.psErr)
			return null
		} catch (e: XPathExpressionException) {
			e.printStackTrace(Tracing.psErr)
			return null
		}
	}

	companion object {

		/**
		 * Make model
		 *
		 * @param args command-line arguments
		 * @return model
		 */
		private fun makeModel(args: Array<String>): Model? {
			val inDir = File(args[0])
			val inDir2 = File(args[1])
			return Factory(inDir, inDir2).get()
		}

		/**
		 * Make core model
		 *
		 * @param args command-line arguments
		 */
		@JvmStatic
		fun main(args: Array<String>) {
			val model = makeModel(args)
			Tracing.psInfo.printf("[Model] %s%n%s%n%s%n", model!!.sources.contentToString(), model.info(), model.counts())
		}
	}
}