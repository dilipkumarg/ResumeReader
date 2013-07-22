package com.imaginea.resumereader.docfactory;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class WordDocumentExtractor implements DocumentExtractor {

	public String getTextContent(String filePath) throws IOException {
		HWPFDocument doc = null;
		FileInputStream istream = new FileInputStream(filePath);
		doc = new HWPFDocument(istream);
		WordExtractor docExtract = new WordExtractor(doc);
		String[] data = docExtract.getParagraphText();
		StringBuilder textContent = new StringBuilder();

		for (int i = 0; i < data.length; i++) {
			textContent.append(data[i]);
		}
		return textContent.toString();
	}
}
