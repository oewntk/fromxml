/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.xml.in;

import org.junit.Test;

import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestSensekeyEscaping
{
	static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private static final String[] tested = { //
			"a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i-pl-j__1:23:45::", //
			"a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i__1:23:45::a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i-pl-j", //
			"oewn-a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i__1:23:45::a-ap-b-lb-c-rb-d-sl-e-cm-f-ex-g-cl-h-sp-i-pl-j", //
	};
	private static final String[] expected = { //
			"a'b(c)d/e,f!g:h_i+j%1:23:45::", //
			"a'b(c)d/e,f!g:h_i%1:23:45::a'b(c)d/e,f!g:h_i+j", //
			"a'b(c)d/e,f!g:h_i%1:23:45::a'b(c)d/e,f!g:h_i+j", //
	};

	@Test
	public void testKey()
	{
		int i = 0;
		for (String id : tested)
		{
			String sk = XmlExtractor.toSensekey(id);
			assertEquals(expected[i++], sk);
			ps.printf("%s -> %s%n", id, sk);
		}
	}
}
