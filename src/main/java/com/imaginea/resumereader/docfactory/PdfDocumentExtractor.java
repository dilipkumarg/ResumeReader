package com.imaginea.resumereader.docfactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PdfDocumentExtractor implements DocumentExtractor {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public String getTextContent(String filePath) throws IOException {
		PdfReader pdfReader = null;
		try {
			pdfReader = new PdfReader(new FileInputStream(filePath));
		} catch (FileNotFoundException fne) {
			LOGGER.log(Level.SEVERE, "File Not Found Exception occured",
					fne.getMessage());
			throw new FileNotFoundException();
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE, "IOException occured", ie.getMessage());
			throw new IOException(ie.getMessage(), ie);
		}
		int pageCount = pdfReader.getNumberOfPages();
		StringBuilder textContent = new StringBuilder();

		for (int i = 1; i <= pageCount; i++) {
			textContent.append(PdfTextExtractor.getTextFromPage(pdfReader, i));
		}
		return textContent.toString();
	}

}
