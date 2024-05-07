/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.ModelInfo
import org.oewntk.xml.`in`.LibTestsXmlCommon.model
import org.oewntk.xml.`in`.LibTestsXmlCommon.ps

class TestsXmlModel {

    @Test
    fun testModelInfo() {
        val info = model!!.info()
        val counts = ModelInfo.counts(model!!)
        ps.println("$info\n$counts")
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
