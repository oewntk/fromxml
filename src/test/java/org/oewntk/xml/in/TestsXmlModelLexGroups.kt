/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Lex
import org.oewntk.model.LexGroupings
import org.oewntk.model.LibTestModelLexGroups.testCICounts
import org.oewntk.model.LibTestModelLexGroups.testCICountsFromMap
import org.oewntk.model.LibTestModelLexGroups.testCIHypermap
import org.oewntk.model.LibTestModelLexGroups.testCILemmas
import org.oewntk.model.LibTestModelLexGroups.testCILexesFor
import org.oewntk.model.LibTestModelLexGroups.testCILexesFor3
import org.oewntk.model.LibTestModelLexGroups.testCIMultipleAll
import org.oewntk.xml.`in`.LibTestsXmlCommon.model

class TestsXmlModelLexGroups {

    private val lexHyperMap: Map<String, Map<String, Collection<Lex>>> by lazy { LexGroupings.hyperMapByLCLemmaByLemma(model!!) }

    @Test
    fun testCIMultipleAll() {
        testCIMultipleAll(model!!, LibTestsXmlCommon.ps)
    }

    @Test
    fun testCILemmas() {
        testCILemmas(model!!, "battle of verdun", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCICounts() {
        testCICounts(model!!, "battle of verdun", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCICountsFromMap() {
        testCICountsFromMap(model!!, "battle of verdun", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIAi() {
        testCILexesFor(model!!, "ai", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIBaroque() {
        testCILexesFor(model!!, "baroque", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIWest3() {
        testCILexesFor3(model!!, "West", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIBaroque3() {
        testCILexesFor3(model!!, "Baroque", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIAi3() {
        testCILexesFor3(model!!, "Ai", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIAbsolute3() {
        testCILexesFor3(model!!, "Absolute", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIHypermapWest() {
        testCIHypermap(lexHyperMap, "west", LibTestsXmlCommon.ps)
    }

    @Test
    fun testCIHypermapBaroque() {
        testCIHypermap(lexHyperMap, "baroque", LibTestsXmlCommon.ps)
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun init() {
            LibTestsXmlCommon.init()
            checkNotNull(model)
        }
    }
}
