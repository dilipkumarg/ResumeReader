package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.FilePathHelper;
import com.imaginea.resumereader.helpers.ResumeMetaExtractor;

public class FileIndexer extends Indexer {
	private static final Logger LOGGER = Logger
			.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private FilePathHelper filePathHelper;
	private ResumeMetaExtractor resumeMeta;
	public FileIndexer(File indexDirFile) throws IOException,
			FileDirectoryEmptyException {
		super(indexDirFile);
		this.filePathHelper = new FilePathHelper();
		this.resumeMeta = new ResumeMetaExtractor();
	}

	public void indexFiles(List<File> filesToIndex) throws IOException {
		String canonicalFilePath, relativeFilePath, fileContent, personName;
		for (File file : filesToIndex) {
			canonicalFilePath = file.getCanonicalPath();
			relativeFilePath = filePathHelper
					.extractRelativePath(canonicalFilePath);
			LOGGER.log(Level.INFO, "Indexing File:" + relativeFilePath);
			try {
				fileContent = getTextContent(canonicalFilePath);
				personName = resumeMeta.extractPersonName(fileContent);
				this.index(fileContent, relativeFilePath,
						WordUtils.capitalizeFully(personName),
						resumeMeta.getResumeSummary(fileContent));
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
