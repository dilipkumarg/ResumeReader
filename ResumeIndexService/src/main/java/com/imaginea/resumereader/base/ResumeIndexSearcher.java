package com.imaginea.resumereader.base;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.imaginea.resumereader.entities.SearchResult;
import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.lucene.FileIndexer;
import com.imaginea.resumereader.lucene.FileValidator;
import com.imaginea.resumereader.lucene.ResumeSearchEngine;

public class ResumeIndexSearcher {
	PropertyFileReader properties;

	public ResumeIndexSearcher() throws IOException {
		properties = new PropertyFileReader();
	}

	public int updateIndex() throws IOException, FileDirectoryEmptyException,
			ParseException {
		FileValidator fileValidator = new FileValidator(new File(
				properties.getIndexDirPath()));
		// getting list of valid files for indexing
		List<File> filesToIndex = fileValidator.hashFiles(
				new File(properties.getResumeDirPath()),
				properties.getLastTimeStamp());
		// here actual indexing goes.
		FileIndexer fileIndexer = new FileIndexer(new File(
				properties.getIndexDirPath()));
		fileIndexer.indexFiles(filesToIndex);
		// updating last time stamp.
		properties.setLastTimeStamp(System.currentTimeMillis());
		return filesToIndex.size();
	}

	public SearchResult search(String query) throws IOException,
			org.apache.lucene.queryparser.classic.ParseException,
			FileDirectoryEmptyException {
		return search(query, false);
	}

	public SearchResult search(String query, Boolean allowDuplicates)
			throws IOException,
			org.apache.lucene.queryparser.classic.ParseException,
			FileDirectoryEmptyException {
		ResumeSearchEngine searchEngine = new ResumeSearchEngine(new File(
				properties.getIndexDirPath()));
		SearchResult searchResult = searchEngine.searchKey(query,
				allowDuplicates);
		return searchResult;
	}

	/**
	 * This Method deletes previous index and creates Index
	 * 
	 * @param indexDirPath
	 * @param resumeDirPath
	 * @return
	 * @throws IOException
	 * @throws FileDirectoryEmptyException
	 * @throws ParseException
	 */
	public int CleanAndIndex() throws IOException, FileDirectoryEmptyException,
			ParseException {
		this.cleanIndex(new File(properties.getIndexDirPath()));
		return this.updateIndex();
	}

	private void cleanIndex(File indexDir) throws IOException {
		File[] files = indexDir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					cleanIndex(f);
				} else {
					f.delete();
				}
			}
		}
		indexDir.delete();
	}

}
