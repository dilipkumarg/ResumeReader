package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileValidator {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	private final File indexDir;
	private static int filesHashed;
	private FileIndexer fileIndexer ;

	public FileValidator(File indexDirFile,
			String resumeContentField, String resumePathField)
			throws IOException {
		this.indexDir = indexDirFile;
		fileIndexer = new FileIndexer(indexDir, resumeContentField,
				resumePathField);
	}
	
	public int hashFiles(File dataDir, long timeStamp) throws IOException{
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
		fileIndexer.commitAndCloseIndexer();
		return filesHashed;
		
	}
	
	private void hashValidFile(File f, long timestamp) throws IOException {
		long lastModified = f.lastModified();
		String filePath = f.getCanonicalPath();
		LOGGER.log(Level.INFO, "Indexing File: {0}", new Object[] { filePath });
		// checking whether the file is not valid or old
		if (isNotValidFile(f) || (lastModified < timestamp)) {
			LOGGER.log(Level.INFO, "Not a Valid file or no change in file ");
			return;
		}
		else{
			fileIndexer.indexFile(f);
			filesHashed++;
		}
	}
	
	private boolean isNotValidFile(File f) {
		return (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) ;
	}
}
