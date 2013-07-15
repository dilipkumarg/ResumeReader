package com.imaginea.resumereader.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.ErrorCode;
import com.imaginea.resumereader.exceptions.MyPropertyFieldException;

public class PropertyFileReader {
	private final Properties properties;
	private final String FILE_NAME = "config.properties";
	private final String INDEX_DIR_PATH = "indexDir";
	private final String RESUME_DIR_PATH = "resumeDir";
	private final String LAST_TIME_STAMP = "lastTimeStamp";
	private final String TIME_STAMP_FORMAT = "MM-dd-yyyy HH:mm:ss";
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
		this.setLastTimeStamp(new Date(0));
		try {
			properties.store(new FileOutputStream(FILE_NAME),
					"Default property file");
		} catch (IOException ie) {
			this.logIOException(ie, "createDefaultPropertyFile");
			throw new IOException(ie);
		}
	}

	public void setIndexDirPath(String indexDir) throws IOException {
		properties.setProperty(INDEX_DIR_PATH, indexDir);
		try {
			properties.store(new FileOutputStream(FILE_NAME),
					"index Directory updated");
		} catch (IOException ie) {
			this.logIOException(ie, "setIndexDirPath");
			throw new IOException(ie);
		}
	}

	/**
	 * returns index directory path
	 * 
	 * @return indexDirPath
	 * @throws MyPropertyFieldException
	 */
	public String getIndexDirPath() throws MyPropertyFieldException {
		String indexDirPath = properties.getProperty(INDEX_DIR_PATH).trim();
		if (indexDirPath.isEmpty()) {
			throw new MyPropertyFieldException("Index Directory Path Empty",
					ErrorCode.INDEX_DIR_EMPTY);
		}
		return indexDirPath;
	}

	public void setResumeDirPath(String fileDir) throws IOException {
		properties.setProperty(RESUME_DIR_PATH, fileDir);
		try {
			properties.store(new FileOutputStream(FILE_NAME),
					"Resume Directory updated");
		} catch (IOException ie) {
			this.logIOException(ie, "setResumeDirPath");
			throw new IOException(ie);
		}
	}

	/**
	 * returns file directory path
	 * 
	 * @return fileDirPath
	 * @throws MyPropertyFieldException
	 */

	public String getResumeDirPath() throws MyPropertyFieldException {
		String fileDirPath = properties.getProperty(RESUME_DIR_PATH).trim();
		if (fileDirPath.isEmpty()) {
			throw new MyPropertyFieldException("File Directory Path is Empty",
					ErrorCode.RESUME_DIR_EMPTY);
		}
		return fileDirPath;
	}

	public void setLastTimeStamp(Date currTimeStamp)
			throws FileNotFoundException, IOException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_STAMP_FORMAT);
		String dateString = dateFormatter.format(currTimeStamp);
		properties.setProperty(LAST_TIME_STAMP, dateString);
		try {
			properties.store(new FileOutputStream(FILE_NAME),
					"Last TimeStamp updated");
		} catch (IOException ie) {
			this.logIOException(ie, "setLastTimeStamp");
			throw new IOException(ie);
		}
	}

	/**
	 * returns last time stamp
	 * 
	 * @return prevTimeStamp
	 * @throws ParseException
	 */
	public Date getLastTimeStamp() throws ParseException {
		String dateString = properties.getProperty(LAST_TIME_STAMP);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_STAMP_FORMAT);
		Date prevTimeStamp = null;
		try {
			prevTimeStamp = dateFormatter.parse(dateString);
		} catch (ParseException pe) {
			LOGGER.log(Level.SEVERE, "ParseException occured \n ERROR:{0}",
					new Object[] { pe.getMessage() });
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return prevTimeStamp;
	}

	private void logIOException(IOException ie, String method) {
		LOGGER.log(Level.SEVERE, "IOError occured. while writing property file"
				+ "\n\t Reported Function:{0}" + "\n\t ERROR:{1}",
				new Object[] { method, ie.getMessage() });
	}
}
