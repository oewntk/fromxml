/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyIC
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyOEWN
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyPWN
import org.oewntk.model.LibTestModelDuplicates.testDuplicatesForKeyPos
import org.oewntk.xml.`in`.LibTestsXmlCommon.model
import org.oewntk.xml.`in`.LibTestsXmlCommon.ps

class TestsXmlModelDuplicates {

	@Test
	fun testKeyOEWN() {
		testDuplicatesForKeyOEWN(model!!, ps)
	}

	@Test
	fun testKeyPos() {
		testDuplicatesForKeyPos(model!!, ps)
	}

	@Test
	fun testKeyIC() {
		testDuplicatesForKeyIC(model!!, ps)
	}

	@Test
	fun testKeyPWN() {
		testDuplicatesForKeyPWN(model!!, ps)
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
