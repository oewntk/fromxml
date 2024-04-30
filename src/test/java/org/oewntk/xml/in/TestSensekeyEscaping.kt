/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.junit.Assert
import org.junit.Test
import org.oewntk.xml.`in`.XmlExtractor.toSensekey
import java.io.PrintStream

class TestSensekeyEscaping {

    @Test
    fun testKey() {
        for ((i, id) in tested.withIndex()) {
            val sk = toSensekey(id)
            Assert.assertEquals(expected[i], sk)
            ps.printf("%s -> %s%n", id, sk)
        }
    }

    companion object {

        val ps: PrintStream = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

        private val tested = arrayOf(
            "a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i-pl-j__1:23:45::",  
            "a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i__1:23:45::a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i-pl-j",  
            "oewn-a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i__1:23:45::a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i-pl-j",  
        )
        private val expected = arrayOf(
            "a'b(c)d/e,f!g:h_i+j%1:23:45::",  
            "a'b(c)d/e,f!g:h_i%1:23:45::a'b(c)d/e,f!g:h_i+j",  
            "a'b(c)d/e,f!g:h_i%1:23:45::a'b(c)d/e,f!g:h_i+j",  
        )
    }
}
