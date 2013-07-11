package com.imaginea.resumereader;

import java.io.IOException;

import com.imaginea.resumereader.exceptions.MyPropertyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class ResumeService {
	private PropertyFileReader properties;

	public ResumeService() throws IOException {
		properties = new PropertyFileReader();
	}

	public void updateIndex() throws IOException, MyPropertyException {
		// ResumeIndexer resumeIndexer = new ResumeIndexer(new File(
		// "/home/dilip/resume/index"));
		String indexDirPath, fileDirPath;
		try {
			indexDirPath = properties.getIndexDir();
			fileDirPath = properties.getFileDir();
		} catch (MyPropertyException mpe) {
			throw new MyPropertyException(mpe.getErrorCode());
		}
	}

	public void search(String query) {

	}

	public void setIndexDirPath(String indexDirPath) throws IOException {
		properties.setIndexDir(indexDirPath);
	}

	public void setFileDirPath(String fileDirPath) throws IOException {
		properties.setFileDir(fileDirPath);
	}
}
