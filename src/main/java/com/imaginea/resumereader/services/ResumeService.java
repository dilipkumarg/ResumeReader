package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.lucene.ResumeIndexer;
import com.imaginea.resumereader.lucene.ResumeSearcheEngine;
import com.imaginea.resumereader.lucene.SearchResult;

public class ResumeService {
	private PropertyFileReader properties;
	private String RESUME_CONTENT_FIELD = "content";
	private String RESUME_FILE_PATH_FIELD = "filename";
	private Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public ResumeService() throws IOException {
		properties = new PropertyFileReader();
	}

	public void updateIndex() throws IOException, MyPropertyFieldException,
			ParseException {
		String indexDirPath, resumeDirPath;
		try {
			indexDirPath = properties.getIndexDirPath();
			resumeDirPath = properties.getResumeDirPath();
		} catch (MyPropertyFieldException mpe) {
			LOGGER.log(
					Level.SEVERE,
					"Property Field Exception occured, \nError Code:{0}\n Error:{1}",
					new Object[] { mpe.getErrorCode(), mpe.getMessage() });
			throw new MyPropertyFieldException(mpe.getErrorCode());
		}
		Date prevTimeStamp = properties.getLastTimeStamp();
		ResumeIndexer resumeIndexer = new ResumeIndexer(new File(indexDirPath),
				new File(resumeDirPath), RESUME_CONTENT_FIELD,
				RESUME_FILE_PATH_FIELD);
		resumeIndexer.appendIndexDirectory(prevTimeStamp);
		properties.setLastTimeStamp(new Date());
	}

	public void search(String query) throws MyPropertyFieldException,
			IOException, org.apache.lucene.queryparser.classic.ParseException {
		String indexDirPath;
		try {
			indexDirPath = properties.getIndexDirPath();
		} catch (MyPropertyFieldException mpe) {
			LOGGER.log(
					Level.SEVERE,
					"Property Field Exception occured, \nError Code:{0}\n Error:{1}",
					new Object[] { mpe.getErrorCode(), mpe.getMessage() });
			throw new MyPropertyFieldException(mpe.getErrorCode());
		}
		ResumeSearcheEngine searchEngine = new ResumeSearcheEngine(
				RESUME_CONTENT_FIELD, RESUME_FILE_PATH_FIELD, new File(
						indexDirPath), 10);
		SearchResult searchResult = searchEngine.searchKey(query);
		System.out.println("Total Hits:" + searchResult.getTotalHitCount());
	}

	public void setIndexDirPath(String indexDirPath) throws IOException {
		properties.setIndexDirPath(indexDirPath);
	}

	public void setResumeDirPath(String fileDirPath) throws IOException {
		properties.setResumeDirPath(fileDirPath);
	}
}
