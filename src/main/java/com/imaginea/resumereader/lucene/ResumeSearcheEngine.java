package com.imaginea.resumereader.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.apache.lucene.util.Version;

public class ResumeSearcheEngine {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private String defaultField;
	private String fileNameField;
	private Directory indexDirectory;
	private int maxHits;
	private IndexSearcher searcher;

	public ResumeSearcheEngine(String defaultField, String fileNameField,
			Directory indexDirectory, int maxHits) {
		this.defaultField = defaultField;
		this.fileNameField = fileNameField;
		this.indexDirectory = indexDirectory;
		this.maxHits = maxHits;
	}

	public SearchResult search(String queryString) throws IOException,
			ParseException {
		long startTime = System.currentTimeMillis();
		IndexSearcher searcher = null;

		IndexReader ir = DirectoryReader.open(indexDirectory);
		searcher = new IndexSearcher(ir);
		QueryParser queryParser = new QueryParser(Version.LUCENE_43,
				defaultField, new StandardAnalyzer(Version.LUCENE_43));
		Query query;
		try {
			query = queryParser.parse(queryString);
		} catch (ParseException pe) {
			LOGGER.log(Level.SEVERE, "Parse error occured:", pe.getMessage());
			throw new ParseException(pe.getMessage());
		}

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
			hitList.add(d.getField(fileNameField).toString());
		}
		return hitList;
	}
}
