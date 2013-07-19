package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.lucene.FileValidator;
import com.imaginea.resumereader.lucene.ResumeSearchEngine;
import com.imaginea.resumereader.lucene.SearchResult;

public class ResumeService {
	private PropertyFileReader properties;
	private String RESUME_CONTENT_FIELD = "content";
	private String RESUME_FILE_PATH_FIELD = "filename";
	private Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public ResumeService() throws IOException {
		properties = new PropertyFileReader();
	}

	public int updateIndex() throws IOException, MyPropertyFieldException,
			ParseException {
		String indexDirPath, resumeDirPath;
		try {
			indexDirPath = properties.getIndexDirPath();
			resumeDirPath = properties.getResumeDirPath();
		} catch (MyPropertyFieldException mpe) {
			this.logPropertyFieldException(mpe, "updateindex");
			throw new MyPropertyFieldException(mpe.getErrorCode());
		}
		long prevTimeStamp = properties.getLastTimeStamp();
		FileValidator fileValidator = new FileValidator(new File(indexDirPath), RESUME_CONTENT_FIELD,
				RESUME_FILE_PATH_FIELD);
		int numIndexed = fileValidator.hashFiles(new File(resumeDirPath), prevTimeStamp);
		properties.setLastTimeStamp(System.currentTimeMillis());
		return numIndexed;
	}

	public SearchResult search(String query) throws MyPropertyFieldException,
			IOException, org.apache.lucene.queryparser.classic.ParseException {
		String indexDirPath;
		try {
			indexDirPath = properties.getIndexDirPath();
		} catch (MyPropertyFieldException mpe) {
			this.logPropertyFieldException(mpe, "search");
			throw new MyPropertyFieldException(mpe.getErrorCode());
		}
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

	private void logPropertyFieldException(MyPropertyFieldException mpe,
			String method) {
		LOGGER.log(Level.SEVERE, "Property Field Exception occured, "
				+ "\n\tReported Method: {0} "
				+ "\n\tError Code:{1}\n\tError:{2}",
				new Object[] { method, mpe.getErrorCode(), mpe.getMessage() });
	}
}
