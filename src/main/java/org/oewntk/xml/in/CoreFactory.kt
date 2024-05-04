/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.oewntk.model.CoreModel
import org.oewntk.model.ModelInfo
import org.xml.sax.SAXException
import java.io.File
import java.io.IOException
import java.util.function.Supplier
import javax.xml.parsers.ParserConfigurationException
import javax.xml.xpath.XPathExpressionException

/**
 * Core model factory
 *
 * @property parser parser
 */
class CoreFactory(
    private val parser: Parser,
) : Supplier<CoreModel?> {

    /**
     * Constructor
     *
     * @param file XML merged file
     * @throws IOException                  io exception
     * @throws ParserConfigurationException parser configuration exception
     * @throws SAXException                 SAX exception
     */
    constructor(file: File) : this(Parser(file))

    override fun get(): CoreModel? {
        try {
            return parser
                .parseCoreModel()
                .generateInverseRelations()
                .apply { source = parser.file }
        } catch (e: XPathExpressionException) {
            e.printStackTrace(Tracing.psErr)
            return null
        }
    }

    companion object {

        /**
         * Make core model
         *
         * @param args command-line arguments
         * @return core model
         * @throws IOException                  io exception
         * @throws ParserConfigurationException parser configuration exception
         * @throws SAXException                 SAX exception
         */
        @Throws(IOException::class, ParserConfigurationException::class, SAXException::class)
        fun makeCoreModel(args: Array<String>): CoreModel? {
            val inDir = File(args[0])
            return CoreFactory(inDir).get()
        }

        /**
         * Main
         *
         * @param args command-line arguments
         * @throws IOException                  io exception
         * @throws ParserConfigurationException parser configuration exception
         * @throws SAXException                 SAX exception
         */
        @Throws(IOException::class, ParserConfigurationException::class, SAXException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val model = makeCoreModel(args)
            Tracing.psInfo.printf("[CoreModel] %s%n%s%n%s%n", model!!.source, model.info(), ModelInfo.counts(model))
        }
    }
}