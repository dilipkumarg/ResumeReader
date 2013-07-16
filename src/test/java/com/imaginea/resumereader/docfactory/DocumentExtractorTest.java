package com.imaginea.resumereader.docfactory;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.resumereader.docfactory.DocumentExtractor;
import com.imaginea.resumereader.docfactory.DocumentExtractorFactory;

public class DocumentExtractorTest {
	private DocumentExtractorFactory docFactory;

	@Before
	public void setUp() {
		docFactory = new DocumentExtractorFactory();
	}

	@Test
	public void testPdfGetTextContent() throws IOException {
		String filePath = this.getClass().getResource("/testdoc.pdf").getPath();
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		assertEquals("am in resume reader", docExtractor.getTextContent());
	}

	@Test
	public void testDocGetTextContent() throws IOException {
		String filePath = this.getClass().getResource("/testdoc.doc").getPath();
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		assertEquals("am in resume reader\r\n", docExtractor.getTextContent());
	}

	@Test
	public void testDocXGetTextContent() throws IOException {
		String filePath = this.getClass().getResource("/testdoc.docx")
				.getPath();
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		assertEquals("am in resume reader\n", docExtractor.getTextContent());
	}

	@Test
	public void testPdfGetTextContentNoFile() throws IOException {
		String filePath = "asfklj.pdf";
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		try {
			docExtractor.getTextContent();
		} catch (Exception e) {
			assertEquals(FileNotFoundException.class, e.getClass());
		}
	}

	@Test
	public void testDocGetTextContentNoFile() throws IOException {
		String filePath = "aldfja2423.doc";
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		try {
			docExtractor.getTextContent();
		} catch (Exception e) {
			assertEquals(FileNotFoundException.class, e.getClass());
		}
	}

	@Test
	public void testDocXGetTextContentNoFile() throws IOException {
		String filePath = "aslfjaslf.docx";
		DocumentExtractor docExtractor = docFactory.getDocExtractor(filePath);
		try {
			docExtractor.getTextContent();
		} catch (Exception e) {
			assertEquals(FileNotFoundException.class, e.getClass());
		}
	}

}