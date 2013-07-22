package com.imaginea.resumereader.docfactory;

public class DocumentExtractorFactory {
	private WordDocumentExtractor wordDocumentExtractor;
	private DocXExtractor docXExtractor;
	private PdfDocumentExtractor pdfDocumentExtractor;

	public DocumentExtractorFactory() {
		this.wordDocumentExtractor = new WordDocumentExtractor();
		this.pdfDocumentExtractor = new PdfDocumentExtractor();
		this.docXExtractor = new DocXExtractor();
	}

	public DocumentExtractor getDocExtractor(String filePath) {
		DocumentExtractor documentExtractor = null;

		if (filePath.toLowerCase().endsWith("doc")) {
			documentExtractor = this.wordDocumentExtractor;
		} else if (filePath.toLowerCase().endsWith("docx")) {
			documentExtractor = this.docXExtractor;
		} else if (filePath.toLowerCase().endsWith("pdf")) {
			documentExtractor = this.pdfDocumentExtractor;
		} else {
			throw new IllegalArgumentException("Unsupported File Format Found");
		}
		return documentExtractor;
	}
}
