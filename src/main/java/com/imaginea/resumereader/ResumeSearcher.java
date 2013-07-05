package com.imaginea.resumereader;

import java.io.IOException;

import com.imaginea.resumereader.factory.DocumentExtractor;
import com.imaginea.resumereader.factory.DocumentExtractorFactory;

public class ResumeSearcher {
	public static void main(String[] args) throws IOException {
		String filePath = "/home/dilip/resume/Dilip.pdf";
		DocumentExtractorFactory docType = new DocumentExtractorFactory();
		DocumentExtractor doc = docType.getDocExtractor(filePath);
		System.out.println(doc.readDocument());
	}
}
