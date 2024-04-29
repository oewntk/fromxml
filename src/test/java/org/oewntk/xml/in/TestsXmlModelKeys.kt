/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.xml.`in`

import junit.framework.TestCase.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestModelKeys.testBaroqueMono
import org.oewntk.model.LibTestModelKeys.testBaroqueMulti
import org.oewntk.model.LibTestModelKeys.testBassDeep
import org.oewntk.model.LibTestModelKeys.testBassShallow
import org.oewntk.model.LibTestModelKeys.testCriticalMono
import org.oewntk.model.LibTestModelKeys.testCriticalMulti
import org.oewntk.model.LibTestModelKeys.testEarthMono
import org.oewntk.model.LibTestModelKeys.testEarthMulti
import org.oewntk.model.LibTestModelKeys.testMobile
import org.oewntk.model.LibTestModelKeys.testMobileNoPronunciation
import org.oewntk.model.LibTestModelKeys.testRowDeep
import org.oewntk.model.LibTestModelKeys.testRowShallow
import org.oewntk.xml.`in`.LibTestsXmlCommon.model

class TestsXmlModelKeys {

    @Test
    fun testMobile() {
        val r = testMobile(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(1, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(2, r[4].toLong())
        assertEquals(5, r.size.toLong())
    }

    @Test
    fun testMobileNoPronunciation() {
        val r = testMobileNoPronunciation(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testEarthMulti() {
        val r = testEarthMulti(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testEarthMono() {
        val r = testEarthMono(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(1, r[2].toLong())
        assertEquals(1, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testBaroqueMulti() {
        val r = testBaroqueMulti(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(2, r[3].toLong())
        assertEquals(1, r[4].toLong())
        assertEquals(1, r[5].toLong())
        assertEquals(2, r[6].toLong())
        assertEquals(2, r[7].toLong())
        assertEquals(0, r[8].toLong())
        assertEquals(0, r[9].toLong())
        assertEquals(0, r[10].toLong())
        assertEquals(0, r[11].toLong())
        assertEquals(0, r[12].toLong())
        assertEquals(0, r[13].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(16, r.size.toLong())
    }

    @Test
    fun testBaroqueMono() {
        val r = testBaroqueMono(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(1, r[2].toLong())
        assertEquals(1, r[3].toLong())
        assertEquals(1, r[4].toLong())
        assertEquals(1, r[5].toLong())
        assertEquals(1, r[6].toLong())
        assertEquals(1, r[7].toLong())
        assertEquals(0, r[8].toLong())
        assertEquals(0, r[9].toLong())
        assertEquals(0, r[10].toLong())
        assertEquals(0, r[11].toLong())
        assertEquals(0, r[12].toLong())
        assertEquals(0, r[13].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(0, r[14].toLong())
        assertEquals(16, r.size.toLong())
    }

    @Test
    fun testCriticalMulti() {
        val r = testCriticalMulti(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(0, r[2].toLong())
        assertEquals(0, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testCriticalMono() {
        val r = testCriticalMono(model!!, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(0, r[2].toLong())
        assertEquals(0, r[3].toLong())
        assertEquals(4, r.size.toLong())
    }

    @Test
    fun testBassDeep() {
        val r = testBassDeep(model, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
    }

    @Test
    fun testBassShallow() {
        val r = testBassShallow(model, LibTestsXmlCommon.ps)
        assertEquals(0, r[0].toLong())
        assertEquals(0, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
    }

    @Test
    fun testRowDeep() {
        val r = testRowDeep(model, LibTestsXmlCommon.ps)
        assertEquals(1, r[0].toLong())
        assertEquals(1, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
    }

    @Test
    fun testRowShallow() {
        val r = testRowShallow(model, LibTestsXmlCommon.ps)
        assertEquals(0, r[0].toLong())
        assertEquals(0, r[1].toLong())
        assertEquals(2, r[2].toLong())
        assertEquals(3, r.size.toLong())
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
