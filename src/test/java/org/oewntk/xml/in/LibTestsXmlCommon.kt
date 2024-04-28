/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.xml.`in`

import org.junit.Assert
import org.oewntk.model.CoreModel
import java.io.File
import java.io.PrintStream

object LibTestsXmlCommon {

	private val source: String? = System.getProperty("SOURCE")

	@JvmField
	val ps: PrintStream = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

	@JvmField
	var model: CoreModel? = null

	@JvmStatic
	fun init() {
		if (model == null) {
			if (source == null) {
				Tracing.psErr.println("Define XML source file dir with -DSOURCE=path")
				Tracing.psErr.println("When running Maven tests, define the oewn.xml file in a xml directory that is child to the project directory.")
				Assert.fail()
			}
			val file = File(source!!)
			Tracing.psInfo.printf("source=%s%n", file.absolutePath)
			if (!file.exists()) {
				Tracing.psErr.println("Define XML source dir that exists")
				Assert.fail()
			}

			model = CoreFactory(file).get()
		}
		checkNotNull(model)
		ps.println(model!!.info())
		ps.println(model!!.counts())
	}
}
