package com.imaginea.poi.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
public class PdfReaderSample {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		PdfReader pdfReader = new PdfReader(new FileInputStream(
				"/home/dilip/Downloads/BusInformationCenter.pdf"));
		int pageCount = pdfReader.getNumberOfPages();
		System.out.println("Number of pages : " + pageCount);
		String str = PdfTextExtractor.getTextFromPage(pdfReader, 1);
		System.out.println(str);
	}
}
