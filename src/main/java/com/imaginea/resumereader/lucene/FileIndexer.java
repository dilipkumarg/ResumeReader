package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
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
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.imaginea.resumereader.docfactory.DocumentExtractorFactory;

public class FileIndexer {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final FSDirectory indexDir;
	private IndexWriter indexWriter;
	private final String resumeContentField;
	private final String resumePathField;
	private DocumentExtractorFactory factory;

	public FileIndexer(File indexDirFile, String resumeContentField,
			String resumePathField) throws IOException {
		try {
			this.indexDir = FSDirectory.open(indexDirFile);
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
		this.factory = new DocumentExtractorFactory();
	}

	public void indexFile(File f) throws IOException {
		String filePath = f.getCanonicalPath();
		try {
			String fileContent = factory.getDocExtractor(filePath)
					.getTextContent(filePath);
			indexWriter.updateDocument(new Term(resumePathField, filePath),
					convertToDoc(fileContent, filePath));
		} catch (IllegalArgumentException iae) {
			LOGGER.log(Level.INFO, iae.getMessage());
		}
	}

	public void commitAndCloseIndexer() throws IOException {
		indexWriter.commit();
		indexWriter.close();
	}

	private Document convertToDoc(String fileContent, String filePath) {
		Document doc = new Document();
		doc.add(new TextField(this.resumeContentField, fileContent,
				Field.Store.YES));
		doc.add(new StringField(this.resumePathField, filePath, Field.Store.YES));
		return doc;
	}

}
