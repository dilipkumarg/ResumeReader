package com.imaginea.resumereader.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;

public class PropertyFileReader {
	private final Properties properties;
	private final String FILE_NAME = "config.properties";
	private final String INDEX_DIR_PATH = "indexDir";
	private final String RESUME_DIR_PATH = "resumeDir";
	private final String LAST_TIME_STAMP = "lastTimeStamp";
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public PropertyFileReader() throws IOException {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(FILE_NAME));
		} catch (FileNotFoundException fne) {
			LOGGER.log(Level.WARNING,
					"Properties File not exists, creating new properties file with default values");
			createDefaultPropertyFile();
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE,
					"IOError occured. while reading property file\n ERROR:{0}",
					new Object[] { ie.getMessage() });
			throw new IOException(ie);
		}
	}

	private void createDefaultPropertyFile() throws IOException {
		properties.setProperty(INDEX_DIR_PATH, "");
		properties.setProperty(RESUME_DIR_PATH, "");
		this.setLastTimeStamp(0);
		properties.store(new FileOutputStream(FILE_NAME),
				"Default property file");
	}

	private void setIndexDirPath(String indexDir) throws IOException {
		properties.setProperty(INDEX_DIR_PATH, indexDir);
		properties.store(new FileOutputStream(FILE_NAME),
				"index Directory updated");
	}

	/**
	 * returns index directory path
	 * 
	 * @return indexDirPath
	 * @throws FileDirectoryEmptyException
	 */
	public String getIndexDirPath() throws IndexDirectoryEmptyException {
		String indexDirPath = properties.getProperty(INDEX_DIR_PATH).trim();
		if (indexDirPath.isEmpty()) {
			throw new IndexDirectoryEmptyException("Index Directory Path Empty");
		}
		return indexDirPath;
	}

	public void setResumeDirPath(String fileDir) throws IOException {
		properties.setProperty(RESUME_DIR_PATH, fileDir);
		if(fileDir.endsWith("/")){
			setIndexDirPath(fileDir+".IndexDir/");
		}else {
			setIndexDirPath(fileDir+"/.IndexDir/");
		}
		properties.store(new FileOutputStream(FILE_NAME),
				"Resume Directory updated");
	}

	/**
	 * returns file directory path
	 * 
	 * @return fileDirPath
	 * @throws FileDirectoryEmptyException
	 */

	public String getResumeDirPath() throws FileDirectoryEmptyException {
		String fileDirPath = properties.getProperty(RESUME_DIR_PATH).trim();
		if (fileDirPath.isEmpty()) {
			throw new FileDirectoryEmptyException(
					"File Directory Path is Empty");
		}
		return fileDirPath;
	}

	public void setLastTimeStamp(long millSeconds)
			throws FileNotFoundException, IOException {
		properties.setProperty(LAST_TIME_STAMP, Long.toString(millSeconds));
		properties.store(new FileOutputStream(FILE_NAME),
				"Last TimeStamp updated");

	}

	/**
	 * returns last time stamp
	 * 
	 * @return prevTimeStamp
	 * @throws ParseException
	 */
	public long getLastTimeStamp() throws ParseException {
		String dateString = properties.getProperty(LAST_TIME_STAMP);
		long prevTimeStamp;
		prevTimeStamp = Long.parseLong(dateString, 10);
		return prevTimeStamp;
	}

	public void setToDefaultProperties() throws IOException {
		this.createDefaultPropertyFile();
	}
}
