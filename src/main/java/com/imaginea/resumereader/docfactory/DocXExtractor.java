package com.imaginea.resumereader.docfactory;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocXExtractor implements DocumentExtractor {

	public String getTextContent(String filePath) throws IOException {
		XWPFDocument docx = null;
		docx = new XWPFDocument(new FileInputStream(filePath));
		XWPFWordExtractor we = new XWPFWordExtractor(docx);
		return we.getText();
	}

}
