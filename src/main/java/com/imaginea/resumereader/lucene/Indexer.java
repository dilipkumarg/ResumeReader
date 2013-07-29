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

    public Indexer(File indexDirFile) throws IOException {
        this.indexDir = FSDirectory.open(indexDirFile);
        this.indexWriter = new IndexWriter(indexDir, new IndexWriterConfig(
                Version.LUCENE_43, new StandardAnalyzer(Version.LUCENE_43)));
    }


    public void index(String content, String title, String summary, String path) throws IOException {
        indexWriter.updateDocument(new Term(IndexFieldNames.FILE_PATH_FIELD,
                path), convertToDoc(content, title, summary, path));
    }

    protected void commitAndCloseIndexer() throws IOException {
        indexWriter.commit();
        indexWriter.close();
    }

    private Document convertToDoc(String fileContent, String filePath, String title, String summary) {
        Document doc = new Document();
        doc.add(new TextField(IndexFieldNames.CONTENT_FIELD, fileContent,
                Field.Store.YES));
        doc.add(new StringField(IndexFieldNames.FILE_PATH_FIELD, filePath,
                Field.Store.YES));
        doc.add(new StringField(IndexFieldNames.TITLE_FIELD, title, Field.Store.YES));
        doc.add(new StringField(IndexFieldNames.SUMMARY_FIELD, summary, Field.Store.YES));
        return doc;
    }

}
