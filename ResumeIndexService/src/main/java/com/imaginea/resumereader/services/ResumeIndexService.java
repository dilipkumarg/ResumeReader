package com.imaginea.resumereader.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.lucene.FileIndexer;
import com.imaginea.resumereader.lucene.FileValidator;

public class ResumeIndexService {
	public ResumeIndexService() {
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

}
