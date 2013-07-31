package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.imaginea.resumereader.entities.FileInfo;
import com.imaginea.resumereader.entities.SearchResult;

public class ResumeSearchEngine {
	private String defaultField;
	private String fileNameField;
	private String personNameField;
	private String summaryField;
	private Directory indexDirectory;
	private int maxHits;
	private IndexSearcher searcher;

	public ResumeSearchEngine(File indexDir) throws IOException {
		this.defaultField = IndexFieldNames.CONTENT_FIELD;
		this.fileNameField = IndexFieldNames.FILE_PATH_FIELD;
		this.personNameField = IndexFieldNames.TITLE_FIELD;
		this.summaryField = IndexFieldNames.SUMMARY_FIELD;
		this.indexDirectory = FSDirectory.open(indexDir);
		this.maxHits = 100;
	}

	public SearchResult searchKey(String queryString) throws IOException,
			ParseException {
		long startTime = System.currentTimeMillis();
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		searcher = new IndexSearcher(indexReader);
		Query query;
		query = new QueryParser(Version.LUCENE_43, defaultField,
				new StandardAnalyzer(Version.LUCENE_43)).parse(queryString);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				this.maxHits, true);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		long endTime = System.currentTimeMillis();
		return new SearchResult(extractHits(hits), collector.getTotalHits(),
				endTime - startTime, queryString);
	}

	public SearchResult searchKey(String queryString, int maxHits)
			throws IOException, ParseException {
		this.maxHits = maxHits;
		return searchKey(queryString);
	}

	private List<FileInfo> extractHits(ScoreDoc[] hits) throws IOException {
		List<FileInfo> hitList = new ArrayList<FileInfo>();
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			hitList.add(new FileInfo(d.getField(this.fileNameField)
					.stringValue(), d.getField(this.personNameField)
					.stringValue(), d.getField(this.summaryField).stringValue()));
		}
		return hitList;
	}
}
