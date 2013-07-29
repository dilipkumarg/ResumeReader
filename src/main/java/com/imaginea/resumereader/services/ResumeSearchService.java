package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;

import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.lucene.ResumeSearchEngine;
import com.imaginea.resumereader.lucene.SearchResult;

public class ResumeSearchService {
	private String RESUME_CONTENT_FIELD = "content";
	private String RESUME_FILE_PATH_FIELD = "filename";
	private String RESUME_FILE_NAME_FIELD = "personname";

	public SearchResult search(String query, String indexDirPath)
			throws IndexDirectoryEmptyException, IOException,
			org.apache.lucene.queryparser.classic.ParseException {
		ResumeSearchEngine searchEngine = new ResumeSearchEngine(
				RESUME_CONTENT_FIELD, RESUME_FILE_PATH_FIELD,
				RESUME_FILE_NAME_FIELD, new File(indexDirPath), 10);
		SearchResult searchResult = searchEngine.searchKey(query);
		return searchResult;
	}
}
