package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.lucene.FileIndexer;
import com.imaginea.resumereader.lucene.FileValidator;
import com.imaginea.resumereader.lucene.Indexer;
import com.imaginea.resumereader.lucene.ResumeSearchEngine;
import com.imaginea.resumereader.lucene.SearchResult;

public class ResumeService {
	private PropertyFileReader properties;
	private String RESUME_CONTENT_FIELD = "content";
	private String RESUME_FILE_PATH_FIELD = "filename";

	public ResumeService() throws IOException {
		properties = new PropertyFileReader();
	}

	public int updateIndex() throws IOException, FileDirectoryEmptyException,
			IndexDirectoryEmptyException, ParseException {
		String indexDirPath, resumeDirPath;
		indexDirPath = properties.getIndexDirPath();
		resumeDirPath = properties.getResumeDirPath();
		long prevTimeStamp = properties.getLastTimeStamp();
		FileValidator fileValidator = new FileValidator(new File(indexDirPath),
				RESUME_CONTENT_FIELD, RESUME_FILE_PATH_FIELD);
		List<File> filesToIndex = fileValidator.hashFiles(new File(resumeDirPath),
				prevTimeStamp);
		Indexer indexer = new FileIndexer(new File(indexDirPath), RESUME_CONTENT_FIELD,
				RESUME_FILE_PATH_FIELD); 
		properties.setLastTimeStamp(System.currentTimeMillis());
		return filesToIndex.size();
	}

	public SearchResult search(String query)
			throws IndexDirectoryEmptyException, IOException, org.apache.lucene.queryparser.classic.ParseException{
		String indexDirPath;
		indexDirPath = properties.getIndexDirPath();
		ResumeSearchEngine searchEngine = new ResumeSearchEngine(
				RESUME_CONTENT_FIELD, RESUME_FILE_PATH_FIELD, new File(
						indexDirPath), 10);
		SearchResult searchResult = searchEngine.searchKey(query);
		return searchResult;
	}

	public void setIndexDirPath(String indexDirPath) throws IOException {
		properties.setIndexDirPath(indexDirPath);
	}

	public void setResumeDirPath(String fileDirPath) throws IOException {
		properties.setResumeDirPath(fileDirPath);
	}

}
