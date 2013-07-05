package com.imaginea.poi.sample;

import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocReader {

	public static void main(String arg[]) throws Exception {
		XWPFDocument docx = new XWPFDocument(new FileInputStream(
				"/home/dilip/Downloads/BusInformationCenter.docx"));
		XWPFWordExtractor we = new XWPFWordExtractor(docx);
		
		System.out.println(we.getText());
	}
}
