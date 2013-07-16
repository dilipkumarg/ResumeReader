package com.imaginea.resumereader.docfactory;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DocumentExtractorFactoryTest {

	@Test
	public void testGetDocExtractorDoc() {
		String filePath = "home/dilip/resume/Dilip.doc";
		DocumentExtractorFactory docFactory = new DocumentExtractorFactory();
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		assertEquals(WordDocumentExtractor.class, docExtractor.getClass());
	}
}
