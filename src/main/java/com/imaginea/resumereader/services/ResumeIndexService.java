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
	public ResumeIndexService() throws IOException {
	}

	public int updateIndex(String indexDirPath, String resumeDirPath,
			long prevTimeStamp) throws IOException,
			FileDirectoryEmptyException, IndexDirectoryEmptyException,
			ParseException {
		int resumeDirPathLength = resumeDirPath.length();
		if (!resumeDirPath.endsWith("/")) {
			resumeDirPathLength++;
		}
		FileValidator fileValidator = new FileValidator(new File(indexDirPath));
		List<File> filesToIndex = fileValidator.hashFiles(new File(
				resumeDirPath), prevTimeStamp, resumeDirPathLength);
		FileIndexer fileIndexer = new FileIndexer(new File(indexDirPath));
		fileIndexer.indexFiles(filesToIndex, resumeDirPathLength);
		return filesToIndex.size();
	}

}
