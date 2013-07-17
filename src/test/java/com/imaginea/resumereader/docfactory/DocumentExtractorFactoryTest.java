package com.imaginea.resumereader.docfactory;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.resumereader.docfactory.DocXExtractor;
import com.imaginea.resumereader.docfactory.DocumentExtractor;
import com.imaginea.resumereader.docfactory.DocumentExtractorFactory;
import com.imaginea.resumereader.docfactory.PdfDocumentExtractor;
import com.imaginea.resumereader.docfactory.WordDocumentExtractor;

public class DocumentExtractorFactoryTest {
	private DocumentExtractorFactory docFactory;

	@Before
	public void setUp() {
		docFactory = new DocumentExtractorFactory();
	}

	// here file need not to be exists.
	@Test
	public void testGetDocExtractorDoc() {
		String filePath = "/home/dilip/resume/Dilip.doc";
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		assertEquals(WordDocumentExtractor.class, docExtractor.getClass());
	}

	@Test
	public void testGetDocExtractorDocx() {
		String filePath = "/home/dilip/resume/Dilip.docx";
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		assertEquals(DocXExtractor.class, docExtractor.getClass());
	}

	@Test
	public void testGetDocExtractorPdf() {
		String filePath = "/home/dilip/resume/Dilip.pdf";
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		assertEquals(PdfDocumentExtractor.class, docExtractor.getClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetDocExtractorFailCase() {
		String filePath = "/home/dilip/resume/Dilip.p";
		docFactory.getDocExtractor(filePath);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testGetDocExtractorFailCase2() {
		String filePath = "/home/dilip/resume/";
		docFactory.getDocExtractor(filePath);
	}
}