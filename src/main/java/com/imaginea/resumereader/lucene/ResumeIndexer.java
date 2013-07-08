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
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.imaginea.resumereader.factory.DocumentExtractor;

public class ResumeIndexer {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final FSDirectory indexDir;
	private final Analyzer luceneAnalyzer;
	private final IndexWriterConfig indexConfig;
	private IndexWriter indexWriter;

	public ResumeIndexer(File indexDirFile) throws IOException {
		try {
			this.indexDir = FSDirectory.open(indexDirFile);
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE, "IOException occured", ie.getMessage());
			throw new IOException(ie.getMessage(), ie);
		}
		this.luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_43);
		this.indexConfig = new IndexWriterConfig(Version.LUCENE_43,
				luceneAnalyzer);
		this.indexWriter = new IndexWriter(indexDir, indexConfig);
	}

	private Document convertToDocument(String content, String fileName) {
		Document document = new Document();
		document.add(new StringField("content", content, Field.Store.YES));
		document.add(new StringField("filename", fileName, Field.Store.YES));
		return document;
	}

	public void indexResume(DocumentExtractor inputDoc) throws IOException {
		Document doc = convertToDocument(inputDoc.getTextContent(),
				inputDoc.getFileName());
		indexWriter.addDocument(doc);
		indexWriter.commit();
		indexWriter.close();
	}
}
