package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.POIXMLException;

import com.imaginea.resumereader.docfactory.DocumentExtractorFactory;

public class FileIndexer extends Indexer {
	private DocumentExtractorFactory factory;

	private static final Logger LOGGER = Logger.getLogger(FileIndexer.class
			.getName());

	public FileIndexer(File indexDirFile, String resumeContentField,
			String resumePathField) throws IOException {
		super(indexDirFile, resumeContentField, resumePathField);
		this.factory = new DocumentExtractorFactory();
	}

	public void indexFiles(List<File> filesToIndex) throws IOException {
		String filePath, fileContent;
		for (File file : filesToIndex) {
			filePath = file.getCanonicalPath();

			fileContent = factory.getDocExtractor(filePath).getTextContent(
					filePath);
			this.index(fileContent, filePath);
			try {
				fileContent = factory.getDocExtractor(filePath).getTextContent(
						filePath);
				this.index(fileContent, filePath);
			} catch (IOException e) {
				LOGGER.log(Level.INFO, "Invalid file name for the file "
						+ filePath);
			} catch (POIXMLException e) {
				LOGGER.log(Level.INFO, "Invalid file name for the file "
						+ filePath);
			} catch (IllegalArgumentException e) {
				LOGGER.log(Level.INFO, "file format are not supported for "
						+ filePath);
			}
		}
		this.commitAndCloseIndexer();
	}

}
