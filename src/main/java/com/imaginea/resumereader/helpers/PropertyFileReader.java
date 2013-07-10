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
					"IOError occured. while reading property file",
					ie.getMessage());
			throw new IOException(ie);
		}
	}

	private void createDefaultPropertyFile() throws FileNotFoundException,
			IOException {
		properties.setProperty(INDEX_DIR, "default");
		properties.setProperty(FILE_DIR, "default");
		properties.setProperty(LAST_TIME_STAMP, "default");
		properties.store(new FileOutputStream(FILE_NAME),
				"Default property file");
	}

	public void setIndexDir(String indexDir) throws FileNotFoundException,
			IOException {
		properties.setProperty(INDEX_DIR, indexDir);
		properties.store(new FileOutputStream(FILE_NAME),
				"index Directory updated");
	}

	public String getIndexDir() {
		return properties.getProperty(INDEX_DIR);
	}

	public void setFileDir(String fileDir) throws FileNotFoundException,
			IOException {
		properties.setProperty(FILE_DIR, fileDir);
		properties.store(new FileOutputStream(FILE_NAME),
				"File Directory updated");
	}

	public String getFileDir() {
		return properties.getProperty(FILE_DIR);
	}

	public void setLastTimeStamp(Date currTimeStamp)
			throws FileNotFoundException, IOException {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_STAMP_FORMAT);
		String dateString = dateFormatter.format(currTimeStamp);
		properties.setProperty(LAST_TIME_STAMP, dateString);
		properties.store(new FileOutputStream(FILE_NAME),
				"Last TimeStamp updated");
	}

	public Date getLastTimeStamp() throws ParseException {
		String dateString = properties.getProperty(LAST_TIME_STAMP);
		SimpleDateFormat dateFormatter = new SimpleDateFormat(TIME_STAMP_FORMAT);
		Date prevTimeStamp = dateFormatter.parse(dateString);
		return prevTimeStamp;
	}

	public static void main(String[] args) throws IOException, ParseException {
		PropertyFileReader pr = new PropertyFileReader();
		pr.getLastTimeStamp();
	}
}
