package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class FileIndexer extends Indexer {
	private static final Logger LOGGER = Logger.getLogger(FileIndexer.class
			.getName());

	public FileIndexer(File indexDirFile, String resumeContentField,
			String resumePathField) throws IOException {
		super(indexDirFile, resumeContentField, resumePathField);
	}

	public void indexFiles(List<File> filesToIndex, int pathLength)
			throws IOException {
		String absoluteFilePath, relativeFilePath, fileContent;
		for (File file : filesToIndex) {
			absoluteFilePath = file.getCanonicalPath();
			relativeFilePath = absoluteFilePath.substring(pathLength);
			try {
				fileContent = getTextContent(absoluteFilePath);
				this.index(fileContent, relativeFilePath);
			} catch (SAXException sae) {
				LOGGER.log(Level.INFO, sae.getMessage());
			} catch (TikaException te) {
				LOGGER.log(Level.INFO, te.getMessage());
			}
		}
		this.commitAndCloseIndexer();
	}

	private String getTextContent(String filePath) throws IOException,
			SAXException, TikaException {
		InputStream inputStream = new FileInputStream(filePath);
		StringWriter stringWriter = new StringWriter();
		BodyContentHandler handler = new BodyContentHandler(stringWriter);
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser();

		parser.parse(inputStream, handler, metadata, new ParseContext());

		return stringWriter.toString();
	}

}
