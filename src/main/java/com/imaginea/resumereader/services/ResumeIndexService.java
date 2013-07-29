package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.lucene.FileIndexer;
import com.imaginea.resumereader.lucene.FileValidator;

public class ResumeIndexService {
	private String RESUME_CONTENT_FIELD = "content";
	private String RESUME_FILE_PATH_FIELD = "filename";

	public ResumeIndexService() throws IOException {
	}

	public int updateIndex(String indexDirPath, String resumeDirPath,
			long prevTimeStamp, Map<String, String> filePathMap)
			throws IOException, FileDirectoryEmptyException,
			IndexDirectoryEmptyException, ParseException {
		int resumeDirPathLength = resumeDirPath.length();
		if (!resumeDirPath.endsWith("/")) {
			resumeDirPathLength++;
		}
		FileValidator fileValidator = new FileValidator(new File(indexDirPath),
				RESUME_CONTENT_FIELD, RESUME_FILE_PATH_FIELD);
		List<File> filesToIndex = fileValidator.hashFiles(new File(
				resumeDirPath), prevTimeStamp, resumeDirPathLength);
		FileIndexer fileIndexer = new FileIndexer(new File(indexDirPath),
				RESUME_CONTENT_FIELD, RESUME_FILE_PATH_FIELD);
		fileIndexer.indexFiles(filesToIndex, resumeDirPathLength, filePathMap);
		return filesToIndex.size();
	}

}
