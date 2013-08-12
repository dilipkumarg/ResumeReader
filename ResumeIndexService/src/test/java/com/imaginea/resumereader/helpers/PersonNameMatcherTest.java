package com.imaginea.resumereader.helpers;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersonNameMatcherTest {

	@Test
	public void similarity() {
		PersonNameMatcher personNameMatcher = new PersonNameMatcher();
		assertTrue("The similarity index should be 1",
				1 == personNameMatcher.compare("pramati", "pramati"));
	}
}
