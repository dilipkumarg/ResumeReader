package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.imaginea.resumereader.exceptions.MyPropertyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.lucene.ResumeIndexer;
import com.imaginea.resumereader.lucene.ResumeSearcheEngine;
import com.imaginea.resumereader.lucene.SearchResult;

public class ResumeService {
	private PropertyFileReader properties;
	private String INDEX_CONTENT_FIELD = "content";
	private String FILE_NAME_FIELD = "filename";

	public ResumeService() throws IOException {
		properties = new PropertyFileReader();
	}

	public void updateIndex() throws IOException, MyPropertyException,
			ParseException {
		String indexDirPath, fileDirPath;
		try {
			indexDirPath = properties.getIndexDir();
			fileDirPath = properties.getFileDir();
		} catch (MyPropertyException mpe) {
			throw new MyPropertyException(mpe.getErrorCode());
		}
		Date prevTimeStamp = properties.getLastTimeStamp();
		ResumeIndexer resumeIndexer = new ResumeIndexer(new File(indexDirPath));
		resumeIndexer
				.appendIndexDirectory(new File(fileDirPath), prevTimeStamp);
	}

	public void search(String query) throws MyPropertyException, IOException,
			org.apache.lucene.queryparser.classic.ParseException {
		String indexDirPath;
		try {
			indexDirPath = properties.getIndexDir();
		} catch (MyPropertyException mpe) {
			throw new MyPropertyException(mpe.getErrorCode());
		}
		ResumeSearcheEngine searchEngine = new ResumeSearcheEngine(
				INDEX_CONTENT_FIELD, FILE_NAME_FIELD, new File(indexDirPath),
				10);
		SearchResult searchResult = searchEngine.searchKey(query);
		System.out.println("Total Hits:" + searchResult.getTotalHitCount());
	}

	public void setIndexDirPath(String indexDirPath) throws IOException {
		properties.setIndexDir(indexDirPath);
	}

	public void setFileDirPath(String fileDirPath) throws IOException {
		properties.setFileDir(fileDirPath);
	}
}
