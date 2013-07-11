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

public class PropertyFileReader {
	private final Properties properties;
	private final String FILE_NAME = "config.properties";
	private final String INDEX_DIR = "indexDir";
	private final String FILE_DIR = "fileDir";
	private final String LAST_TIME_STAMP = "lastTimeStamp";
	private final String TIME_STAMP_FORMAT = "MM-dd-yyyy HH:mm:ss";
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	PropertyFileReader() throws IOException {
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

	private void createDefaultPropertyFile() throws FileNotFoundException,
			IOException {
		properties.setProperty(INDEX_DIR, "");
		properties.setProperty(FILE_DIR, "");
		this.setLastTimeStamp(new Date());
		try {
			properties.store(new FileOutputStream(FILE_NAME),
					"Default property file");
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE,
					"IOError occured. while writing property file\n ERROR:{0}",
					new Object[] { ie.getMessage() });
			throw new IOException(ie);
		}
	}

	public void setIndexDir(String indexDir) throws FileNotFoundException,
			IOException {
		properties.setProperty(INDEX_DIR, indexDir);
		try {
			properties.store(new FileOutputStream(FILE_NAME),
					"index Directory updated");
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE,
					"IOError occured. while writing property file\n ERROR:{0}",
					new Object[] { ie.getMessage() });
			throw new IOException(ie);
		}
	}

	/**
	 * returns index directory path
	 * 
	 * @return indexDirPath
	 * @throws IllegalArgumentException
	 */
	public String getIndexDir() {
		String indexDirPath = properties.getProperty(INDEX_DIR).trim();
		if (indexDirPath.isEmpty()) {
			LOGGER.log(Level.SEVERE,
					"Index Directory Path empty, throwing NullPointerException");
			throw new IllegalArgumentException("index Directory Path is Empty");
		}
		return indexDirPath;
	}

	public void setFileDir(String fileDir) throws FileNotFoundException,
			IOException {
		properties.setProperty(FILE_DIR, fileDir);
		try {
			properties.store(new FileOutputStream(FILE_NAME),
					"File Directory updated");
		} catch (IOException ie) {
			LOGGER.log(Level.SEVERE,
					"IOError occured. while writing property file\n ERROR:{0}",
					new Object[] { ie.getMessage() });
			throw new IOException(ie);
		}
	}

	/**
	 * returns file directory path
	 * 
	 * @return fileDirPath
	 * @throws IllegalArgumentException
	 */

	public String getFileDir() {
		String fileDirPath = properties.getProperty(FILE_DIR).trim();
		if (fileDirPath.isEmpty()) {
			LOGGER.log(Level.SEVERE,
					"File Directory Path empty, throwing NullPointerException");
			throw new IllegalArgumentException("File Directory Path is Empty");
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
			LOGGER.log(Level.SEVERE,
					"IOError occured. while writing property file\n ERROR:{0}",
					new Object[] { ie.getMessage() });
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

	public static void main(String[] args) throws IOException, ParseException {
		PropertyFileReader pr = new PropertyFileReader();
		System.out.println(pr.getFileDir());
	}
}
