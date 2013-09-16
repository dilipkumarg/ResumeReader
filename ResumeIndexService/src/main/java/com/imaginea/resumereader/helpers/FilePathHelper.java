package com.imaginea.resumereader.helpers;

import java.io.File;
import java.io.IOException;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;

public class FilePathHelper {
	private final String resumeDirPath;
	private static FilePathHelper instance = null;

	private FilePathHelper() throws FileDirectoryEmptyException, IOException {
		String dirPath = new File(PropertyFileReader.getInstance()
				.getResumeDirPath()).getCanonicalPath();
		// for windows environments we need to add one more (\) to escape.
		this.resumeDirPath = ((dirPath.endsWith(File.separator) ? dirPath
				: dirPath.concat(File.separator))).replace("\\", "\\\\");
	}

	public static FilePathHelper getInstance()
			throws FileDirectoryEmptyException, IOException {
		if (instance == null) {
			instance = new FilePathHelper();
		}
		return instance;
	}

	public String extractRelativePath(String filePath) {
		return filePath.replaceFirst(this.resumeDirPath, "").trim();
	}

	public String getCanonicalPath(String relPath) {
		String canonicalPath = this.resumeDirPath;
		if (relPath.startsWith(File.separator)) {
			relPath = relPath.substring(1);
		}
		return canonicalPath.concat(relPath);
	}
}
