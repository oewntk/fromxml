/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.LibTestModelQueries.testWordByType
import org.oewntk.model.LibTestModelQueries.testWordByTypeAndPronunciation
import org.oewntk.xml.`in`.LibTestsXmlCommon.model
import org.oewntk.xml.`in`.LibTestsXmlCommon.ps

class TestsXmlModelQueries {
	@Test
	fun testRowByType() {
		testWordByType(model!!, "row", ps)
	}

	@Test
	fun testRowByPos() {
		testWordByType(model!!, "row", ps)
	}

	@Test
	fun testRowByTypeAndPronunciation() {
		testWordByTypeAndPronunciation(model!!, "row", ps)
	}

	@Test
	fun testRowByPosAndPronunciation() {
		testWordByTypeAndPronunciation(model!!, "row", ps)
	}


	@Test
	fun testCriticalByType() {
		testWordByType(model!!, "critical", ps)
	}

	@Test
	fun testCriticalByPos() {
		testWordByType(model!!, "critical", ps)
	}

	@Test
	fun testBassByPos() {
		testWordByType(model!!, "bass", ps)
	}

	@Test
	fun testBaroqueByPos() {
		testWordByType(model!!, "baroque", ps)
	}

	@Test
	fun testBaroqueCSByPos() {
		testWordByType(model!!, "Baroque", ps)
	}

	@Test
	fun testGaloreByPos() {
		testWordByType(model!!, "galore", ps)
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
