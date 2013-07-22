package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;

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

public class Indexer {
	private final FSDirectory indexDir;
	private IndexWriter indexWriter;
	private final String contentField;
	private final String pathField;

	public Indexer(File indexDirFile, String resumeContentField,
			String resumePathField) throws IOException {
		this.indexDir = FSDirectory.open(indexDirFile);
		this.contentField = resumeContentField;
		this.pathField = resumePathField;
		this.indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(
				Version.LUCENE_43, new StandardAnalyzer(Version.LUCENE_43)));
	}

	protected void index(String content, String path) throws IOException {
		indexWriter.updateDocument(new Term(pathField, path),
				convertToDoc(content, path));
	}

	protected void commitAndCloseIndexer() throws IOException {
		indexWriter.commit();
		indexWriter.close();
	}

	private Document convertToDoc(String fileContent, String filePath) {
		Document doc = new Document();
		doc.add(new TextField(this.contentField, fileContent,
				Field.Store.YES));
		doc.add(new StringField(this.pathField, filePath, Field.Store.YES));
		return doc;
	}

}
