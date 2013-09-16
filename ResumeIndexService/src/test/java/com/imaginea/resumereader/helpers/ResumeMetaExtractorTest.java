package com.imaginea.resumereader.helpers;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.junit.Test;

public class ResumeMetaExtractorTest {
	@Test
	public void getResumeSummaryTest() throws IOException, TikaException {
		ResumeMetaExtractor resumeMetaExtractor = ResumeMetaExtractor
				.getInstance();
		String body = "Apurba has 9+ Years of IT experience with an expertise in software design, development as well as architecture and leading of product development efforts";
		assertTrue("The similarity index should be 1",
				body.equals(resumeMetaExtractor.getResumeSummary(body)));
	}

	@Test
	public void extractPersonNameTest() throws FileNotFoundException {
		ResumeMetaExtractor resumeMetaExtractor = ResumeMetaExtractor
				.getInstance();
		String body = "Apurba Nath \n Apurba has 9+ Years of IT experience with an expertise in software design, development as well as architecture and leading of product development efforts";
		String personName = resumeMetaExtractor.extractPersonName(body);
		assertTrue("The similarity index should be 1",
				"Apurba Nath".equals(personName));
	}
}
