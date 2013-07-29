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

public class ResumeSearchEngine {
	private String defaultField;
	private String fileNameField;
	private String personNameField;
	private Directory indexDirectory;
	private int maxHits;
	private IndexSearcher searcher;

	public ResumeSearchEngine(String defaultField, String fileNameField,
			String personNameField, File indexDir, int maxHits) throws IOException {
		this.defaultField = defaultField;
		this.fileNameField = fileNameField;
		this.personNameField = personNameField;
		this.indexDirectory = FSDirectory.open(indexDir);
		this.maxHits = maxHits;
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

	private List<String> extractHits(ScoreDoc[] hits) throws IOException {
		List<String> hitList = new ArrayList<String>();
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			hitList.add(d.getField(personNameField).stringValue());
		}
		return hitList;
	}
}
