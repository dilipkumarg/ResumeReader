package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileValidator {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final File indexDir;
	private List<File> listOfFiles;

	public FileValidator(File indexDirFile, String resumeContentField,
			String resumePathField) throws IOException {
		this.indexDir = indexDirFile;
		this.listOfFiles = new ArrayList<File>();
	}

	public List<File> hashFiles(File dataDir, long timeStamp)
			throws IOException {
		if(!dataDir.exists()){
			LOGGER.log(Level.SEVERE, "The File Direcory Path is incorrect, choose a correct path");
			System.exit(1);
		}
		File[] files = dataDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()
					&& !file.getCanonicalPath().equalsIgnoreCase(
							indexDir.getCanonicalPath())) {
				// recursive calls
				hashFiles(file, timeStamp);
			} else {
				hashValidFile(file, timeStamp);
			}
		}
		return this.listOfFiles;
	}

	private void hashValidFile(File f, long timestamp) throws IOException {
		long lastModified = f.lastModified();
		String filePath = f.getCanonicalPath();
		LOGGER.log(Level.INFO, "Indexing File: " + filePath);
		// checking whether the file is not valid or old
		if (isNotValidFile(f) || (lastModified < timestamp)) {
			LOGGER.log(Level.INFO, "Not a Valid file or no change in file ");
			return;
		} else {
			this.listOfFiles.add(f);
		}
	}

	private boolean isNotValidFile(File f) {
		return (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists());
	}
}
