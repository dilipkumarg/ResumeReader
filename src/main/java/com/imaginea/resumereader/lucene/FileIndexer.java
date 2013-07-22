package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.imaginea.resumereader.docfactory.DocumentExtractorFactory;

public class FileIndexer extends Indexer {
	private DocumentExtractorFactory factory;
	public FileIndexer(File indexDirFile, String resumeContentField,
			String resumePathField) throws IOException {
		super(indexDirFile, resumeContentField, resumePathField);
		this.factory = new DocumentExtractorFactory();
	}
	
	public void indexFiles(List<File> filesToIndex) throws IOException {
		String filePath, fileContent;
		for (File file : filesToIndex) {
			filePath = file.getCanonicalPath();
			fileContent = factory.getDocExtractor(filePath)
					.getTextContent();
			this.index(fileContent, filePath);
		}
		this.commitAndCloseIndexer();
	}
	
}
