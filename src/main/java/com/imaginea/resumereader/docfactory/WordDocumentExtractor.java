package com.imaginea.resumereader.docfactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class WordDocumentExtractor implements DocumentExtractor {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public String getTextContent(String filePath) throws IOException {
		HWPFDocument doc = null;
		try {
			FileInputStream istream = new FileInputStream(filePath);
			doc = new HWPFDocument(istream);
		} catch (FileNotFoundException fne) {
			LOGGER.log(Level.SEVERE, "File Not Found Exception occured",
					fne.getMessage());
			throw new FileNotFoundException();
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE, "IOException occured", ie.getMessage());
			throw new IOException(ie.getMessage(), ie);
		}
		WordExtractor docExtract = new WordExtractor(doc);
		String[] data = docExtract.getParagraphText();
		StringBuilder textContent = new StringBuilder();

		for (int i = 0; i < data.length; i++) {
			textContent.append(data[i]);
		}
		return textContent.toString();
	}
}
