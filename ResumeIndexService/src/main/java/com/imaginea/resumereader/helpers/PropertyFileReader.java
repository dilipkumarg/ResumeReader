package com.imaginea.resumereader.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;

public class PropertyFileReader {
	private final Properties propResumeDir, propTimeStamp;
	private final String FILE_NAME = "config.properties";
	private final String TIME_STAMP_NAME = "timestamp.properties";
	private final String RESUME_DIR_PATH = "resumeDir";
	private final String LAST_TIME_STAMP = "lastTimeStamp";
	private final String EMPLOYEE_EXCEL = "EmployeeExcel";
	private final String SECURITY_KEY = "securityKey";
	private final Logger LOGGER = Logger.getLogger(this.getClass());
	private static PropertyFileReader instance = null;

	private PropertyFileReader() throws IOException {
		propResumeDir = new Properties();
		propTimeStamp = new Properties();
		try {
			propResumeDir.load(new FileInputStream(FILE_NAME));
		} catch (FileNotFoundException fne) {
			LOGGER.log(
					Level.INFO,
					"Properties File not exists, creating new propResumeDir file with default values");
			this.setResumeDirPath("Resumes");
			this.setEmployeeExcelPath("Resumes/Book1.xlsx");
			this.setSecurityKey("pramati123");
		}
	}

	public static PropertyFileReader getInstance() throws IOException {
		if (instance == null) {
			instance = new PropertyFileReader();
		}
		return instance;
	}

	/**
	 * returns index directory path
	 * 
	 * @return indexDirPath
	 * @throws FileDirectoryEmptyException
	 */
	public String getIndexDirPath() throws FileDirectoryEmptyException {
		return getResumeDirPath() + File.separator + ".index";
	}

	public void setResumeDirPath(String fileDir) throws IOException {
		propResumeDir.setProperty(RESUME_DIR_PATH, fileDir);
		propResumeDir.store(new FileOutputStream(FILE_NAME),
				"Resume Directory updated");
	}

	public void setEmployeeExcelPath(String employeeExcelFile)
			throws IOException {
		if (employeeExcelFile.endsWith("xls")
				|| employeeExcelFile.endsWith("xlsx")) {
			propResumeDir.setProperty(EMPLOYEE_EXCEL, employeeExcelFile);
			propResumeDir.store(new FileOutputStream(FILE_NAME),
					"Excel File Path updated");
		} else {
			throw new IllegalArgumentException(
					"Invalid File Format for EmployeeList File. supported Formats(xls/xlsx)");
		}
	}

	/**
	 * returns file directory path
	 * 
	 * @return fileDirPath
	 * @throws FileDirectoryEmptyException
	 */

	public String getResumeDirPath() throws FileDirectoryEmptyException {
		String fileDirPath = propResumeDir.getProperty(RESUME_DIR_PATH).trim();
		if (fileDirPath.isEmpty()) {
			throw new FileDirectoryEmptyException(
					"File Directory Path is Empty");
		}
		return fileDirPath;
	}

	public String getEmployeeExcelPath() throws FileNotFoundException {
		String employeeExcelPath = propResumeDir.getProperty(EMPLOYEE_EXCEL);
		if (employeeExcelPath != null && employeeExcelPath.isEmpty()) {
			throw new FileNotFoundException("Employee List File Path is Empty");
		}
		return (employeeExcelPath != null ? employeeExcelPath.trim()
				: employeeExcelPath);
	}

	private String getTImeStampFilePath() throws FileDirectoryEmptyException {
		return getIndexDirPath() + File.separator + TIME_STAMP_NAME;
	}

	public void setLastTimeStamp(long millSeconds) throws IOException,
			FileDirectoryEmptyException {
		File indexDirectory = new File(getIndexDirPath());
		if (!indexDirectory.exists()) {
			indexDirectory.mkdirs();
		}
		propTimeStamp.setProperty(LAST_TIME_STAMP, Long.toString(millSeconds));
		propTimeStamp.store(new FileOutputStream(getTImeStampFilePath()),
				"Last TimeStamp updated");

	}

	/**
	 * returns last time stamp
	 * 
	 * @return prevTimeStamp
	 * @throws ParseException
	 */
	public long getLastTimeStamp() throws FileDirectoryEmptyException,
			IOException {
		try {
			propTimeStamp
					.load(new FileInputStream(this.getTImeStampFilePath()));
		} catch (FileNotFoundException fne) {
			LOGGER.log(
					Level.INFO,
					"Time Stamp File not exists on the current resume directory. Creating default time stamp file");
			this.setLastTimeStamp(0);
		}
		String dateString = propTimeStamp.getProperty(LAST_TIME_STAMP);
		return Long.parseLong(dateString, 10);
	}

	public String getSecurityKey() {
		String key = propResumeDir.getProperty(SECURITY_KEY);
		return ((key == null || key.trim().isEmpty()) ? "pramati123" : key);
	}

	public void setSecurityKey(String key) throws FileNotFoundException,
			IOException {
		propResumeDir.setProperty(SECURITY_KEY, key);
		propResumeDir.store(new FileOutputStream(FILE_NAME),
				"Security Key Updated");
	}
}
