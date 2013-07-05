package com.imaginea.resumereader.factory;

public class DocumentExtractorFactory {

	public DocumentExtractor getDocExtractor(String filePath) {
		DocumentExtractor documentExtractor = null;

		if (filePath.endsWith("doc")) {
			documentExtractor = new WordDocumentExtractor(filePath);
		} else if (filePath.endsWith("docx")) {
			documentExtractor = new DocXExtractor(filePath);
		} else if (filePath.endsWith("pdf")) {
			documentExtractor = new PdfDocumentExtractor(filePath);
		}
		return documentExtractor;
	}
}
