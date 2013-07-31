package com.imaginea.resumereader.helpers;

import java.io.File;
import java.io.IOException;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;

public class FilePathHelper {
	private final String resumeDirPath;

	public FilePathHelper() throws FileDirectoryEmptyException, IOException {
		String dirPath = new File(new PropertyFileReader().getResumeDirPath())
				.getCanonicalPath();
		this.resumeDirPath = (dirPath.endsWith(File.separator) ? dirPath
				: dirPath.concat(File.separator));
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
