package com.imaginea.resumereader.docfactory;

import java.io.FileInputStream;
import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PdfDocumentExtractor implements DocumentExtractor {

	public String getTextContent(String filePath) throws IOException {
		PdfReader pdfReader = null;
			pdfReader = new PdfReader(new FileInputStream(filePath));
		int pageCount = pdfReader.getNumberOfPages();
		StringBuilder textContent = new StringBuilder();

		for (int i = 1; i <= pageCount; i++) {
			textContent.append(PdfTextExtractor.getTextFromPage(pdfReader, i));
		}
		return textContent.toString();
	}

}
