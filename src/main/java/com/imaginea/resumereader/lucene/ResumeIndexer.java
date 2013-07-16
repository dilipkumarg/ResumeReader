package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.imaginea.resumereader.docfactory.DocumentExtractorFactory;

public class ResumeIndexer {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final FSDirectory indexDir;
	private final File resumeDir;
	private IndexWriter indexWriter;
	private final String resumeContentField;
	private final String resumePathField;

	public ResumeIndexer(File indexDirFile, File dataDir,
			String resumeContentField, String resumePathField)
			throws IOException {
		try {
			this.indexDir = FSDirectory.open(indexDirFile);
			this.resumeDir = dataDir;
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE, "IOException occured", ie.getMessage());
			throw new IOException(ie.getMessage(), ie);
		}
		this.resumeContentField = resumeContentField;
		this.resumePathField = resumePathField;
		Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_43);
		IndexWriterConfig indexConfig = new IndexWriterConfig(
				Version.LUCENE_43, luceneAnalyzer);
		this.indexWriter = new IndexWriter(indexDir, indexConfig);
	}

	public int appendIndexDirectory(Date timeStamp) throws IOException {
		indexDirectory(resumeDir, timeStamp);
		int numIndexed = indexWriter.maxDoc();
		indexWriter.commit();
		indexWriter.close();

		return numIndexed;

	}

	private void indexDirectory(File dataDir, Date timeStamp)
			throws IOException {
		File[] files = dataDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()
					&& !file.getCanonicalPath().equalsIgnoreCase(
							indexDir.getDirectory().getCanonicalPath())) {
				// recursive calls
				indexDirectory(file, timeStamp);
			} else {
				indexFile(file, timeStamp);
			}
		}
	}

	private void indexFile(File f, Date timestamp) throws IOException {
		long millisec = f.lastModified();
		Date lastModified = new Date(millisec);
		String filePath = f.getCanonicalPath();
		LOGGER.log(Level.INFO, "Indexing File: {0}", new Object[] { filePath });
		// checking for new file
		if (!isValidFile(f) || lastModified.compareTo(timestamp) < 0) {
			LOGGER.log(Level.INFO, "Not a Valid file or no change in file ");
			return;
		}
		DocumentExtractorFactory factory = new DocumentExtractorFactory();
		try {
			String fileContent = factory.getDocExtractor(filePath)
					.getTextContent();
			indexWriter.addDocument(convertToDoc(fileContent, filePath));
		} catch (IllegalArgumentException iae) {
			LOGGER.log(Level.INFO, iae.getMessage());
		}
	}

	private Document convertToDoc(String fileContent, String filePath) {
		Document doc = new Document();
		doc.add(new TextField(this.resumeContentField, fileContent,
				Field.Store.YES));
		doc.add(new StringField(this.resumePathField, filePath, Field.Store.YES));
		return doc;
	}

	private boolean isValidFile(File f) {
		if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
			return false;
		} else {
			return true;
		}
	}

}
