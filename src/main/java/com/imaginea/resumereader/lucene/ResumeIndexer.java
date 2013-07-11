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

import com.imaginea.resumereader.factory.DocumentExtractorFactory;
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
		document.add(new TextField("content", content, Field.Store.YES));
		document.add(new StringField("filename", fileName, Field.Store.YES));
		return document;
	}

	public int appendIndexDirectory(File dataDir, Date timeStamp)
			throws IOException {
		indexDirectory(dataDir, timeStamp);

		int numIndexed = indexWriter.maxDoc();
		indexWriter.close();

		return numIndexed;

	}

	private void indexDirectory(File dataDir, Date timeStamp)
			throws IOException {

		File[] files = dataDir.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				indexDirectory(f, timeStamp);
			} else {
				indexFileWithIndexWriter(f, timeStamp);
			}
		}

	}

	private void indexFileWithIndexWriter(File f, Date timestamp)
			throws IOException {
		long millisec = f.lastModified();
		Date lastModified = new Date(millisec);
		if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()
				|| lastModified.compareTo(timestamp) < 0) {
			return;
		}
		String fullFilePath = f.getCanonicalPath();
		String fileName = fullFilePath
				.substring(fullFilePath.lastIndexOf("/") + 1);
		System.out.println("Indexing file " + fileName);
		DocumentExtractorFactory factory = new DocumentExtractorFactory();
		Document doc = new Document();
		String content = factory.getDocExtractor(fullFilePath).getTextContent();
		doc.add(new TextField("contents", content, Field.Store.YES));
		doc.add(new StringField("filename", fileName, Field.Store.YES));
		indexWriter.addDocument(doc);
	}

	public void indexResume(DocumentExtractor inputDoc) throws IOException {
		Document doc = convertToDocument(inputDoc.getTextContent(),
				inputDoc.getFileName());
		indexWriter.addDocument(doc);
		indexWriter.commit();
		indexWriter.close();
	}
}
