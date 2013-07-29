package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;

import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.lucene.ResumeSearchEngine;
import com.imaginea.resumereader.lucene.SearchResult;

public class ResumeSearchService {

	public SearchResult search(String query, String indexDirPath)
			throws IOException,
			org.apache.lucene.queryparser.classic.ParseException {
		ResumeSearchEngine searchEngine = new ResumeSearchEngine(new File(
				indexDirPath));
		SearchResult searchResult = searchEngine.searchKey(query);
		return searchResult;
	}
}
