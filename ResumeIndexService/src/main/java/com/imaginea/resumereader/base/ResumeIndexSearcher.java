package com.imaginea.resumereader.base;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.imaginea.resumereader.entities.SearchResult;
import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.lucene.FileIndexer;
import com.imaginea.resumereader.lucene.FileValidator;
import com.imaginea.resumereader.lucene.ResumeSearchEngine;

public class ResumeIndexSearcher {
	public ResumeIndexSearcher() {
	}

	public int updateIndex(String indexDirPath, String resumeDirPath,
			long prevTimeStamp) throws IOException,
			FileDirectoryEmptyException, ParseException {
		FileValidator fileValidator = new FileValidator(new File(indexDirPath));
		List<File> filesToIndex = fileValidator.hashFiles(new File(
				resumeDirPath), prevTimeStamp);
		FileIndexer fileIndexer = new FileIndexer(new File(indexDirPath));
		fileIndexer.indexFiles(filesToIndex);
		return filesToIndex.size();
	}

	public SearchResult search(String query, String indexDirPath)
			throws IOException,
			org.apache.lucene.queryparser.classic.ParseException {
		ResumeSearchEngine searchEngine = new ResumeSearchEngine(new File(
				indexDirPath));
		SearchResult searchResult = searchEngine.searchKey(query);
		return searchResult;
	}

}
