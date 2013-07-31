package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.FilePathHelper;

public class FileValidator {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final File indexDir;
	private List<File> listOfFiles;
	String[] suffix = { "docx", "doc", "pdf" };
	private FilePathHelper filePathHelper;

	public FileValidator(File indexDirFile) throws IOException,
			FileDirectoryEmptyException {
		this.indexDir = indexDirFile;
		this.listOfFiles = new ArrayList<File>();
		this.filePathHelper = new FilePathHelper();
	}

	public List<File> hashFiles(File dataDir, long timeStamp)
			throws IOException {
		if (!dataDir.exists()) {
			LOGGER.log(Level.SEVERE,
					"The File Direcory Path is incorrect, choose a correct path");
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
		String relativeFilePath = filePathHelper.extractRelativePath(f
				.getCanonicalPath());
		// checking whether the file is not valid or old or format not supported
		if (!isFileSupported(f)) {
			LOGGER.log(Level.INFO, "Unsupported File Format Found:"
					+ relativeFilePath);
		} else if (isNotValidFile(f) || (lastModified < timestamp)) {
			LOGGER.log(Level.INFO, "Not a Valid file or no change in file:"
					+ relativeFilePath);
		} else {
			this.listOfFiles.add(f);
		}
	}

	private boolean isFileSupported(File f) {
		boolean extnMatch = false;
		for (String s : suffix) {
			if (f.getName().toLowerCase().endsWith(s)) {
				extnMatch = true;
				break;
			}
		}
		return extnMatch;
	}

	private boolean isNotValidFile(File f) {
		return (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists());
	}
}
