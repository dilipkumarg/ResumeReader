package com.imaginea.resumereader;

import java.io.File;
import java.io.IOException;

import com.imaginea.resumereader.factory.DocumentExtractor;
import com.imaginea.resumereader.factory.DocumentExtractorFactory;
import com.imaginea.resumereader.lucene.ResumeIndexer;

public class ResumeService {
	public static void main(String[] args) throws IOException {
		ResumeService rs = new ResumeService();
		rs.addDoc();
	}

	public void addDoc() throws IOException {
		String filePath =  "/home/dilip/resume/Dilip.docx";
		DocumentExtractorFactory docFactory = new DocumentExtractorFactory();
		DocumentExtractor doc = docFactory.getDocExtractor(filePath);

		ResumeIndexer resumeIndexer = new ResumeIndexer(new File(
				"/home/dilip/resume/index"));
		resumeIndexer.indexResume(doc);
	}

	public void searchDoc() {

	}
}
