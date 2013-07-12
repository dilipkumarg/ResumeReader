package com.imaginea.resumereader.docfactory;

public class DocumentExtractorFactory {

	public DocumentExtractor getDocExtractor(String filePath) {
		DocumentExtractor documentExtractor = null;

		if (filePath.endsWith("doc")) {
			documentExtractor = new WordDocumentExtractor(filePath);
		} else if (filePath.endsWith("docx")) {
			documentExtractor = new DocXExtractor(filePath);
		} else if (filePath.endsWith("pdf")) {
			documentExtractor = new PdfDocumentExtractor(filePath);
		} else {
			throw new IllegalArgumentException("Unsupported File Format Found");
		}
		return documentExtractor;
	}
}
