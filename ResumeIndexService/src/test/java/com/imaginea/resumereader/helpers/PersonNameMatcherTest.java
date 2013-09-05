package com.imaginea.resumereader.helpers;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersonNameMatcherTest {

	@Test
	public void similarity() {
		assertTrue("The similarity index should be 1",
				1 == PersonNameMatcher.similarity("pramati", "pramati"));
	}
}
