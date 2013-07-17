package com.imaginea.resumereader.helpers;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.resumereader.exceptions.ErrorCode;
import com.imaginea.resumereader.exceptions.MyPropertyFieldException;

public class PropertyFileReaderTest {
	private PropertyFileReader propReader;

	public PropertyFileReaderTest() throws IOException {
		propReader = new PropertyFileReader();
	}

	@Before
	public void setUp() throws IOException {
		propReader.setToDefaultProperties();
	}

	@Test
	public void testGetIndexDirDefaultValue() {
		try {
			propReader.getIndexDirPath();
		} catch (Exception e) {
			assertEquals(MyPropertyFieldException.class, e.getClass());
			MyPropertyFieldException mpe = (MyPropertyFieldException) e;
			assertEquals(ErrorCode.INDEX_DIR_EMPTY, mpe.getErrorCode());
		}
	}

	@Test
	public void testGetResumeDirDefaultValue() {
		try {
			propReader.getResumeDirPath();
		} catch (Exception e) {
			assertEquals(MyPropertyFieldException.class, e.getClass());
			MyPropertyFieldException mpe = (MyPropertyFieldException) e;
			assertEquals(ErrorCode.RESUME_DIR_EMPTY, mpe.getErrorCode());
		}
	}

	@Test
	public void testGetLastTimeDefaultValue() throws ParseException {
		Date date = propReader.getLastTimeStamp();
		long expectedTime = 0;
		assertEquals(expectedTime, date.getTime());
	}

	@Test
	public void testSetIndexDir() throws IOException, MyPropertyFieldException {
		String indexDir = "/home/dilip/resume/index";
		propReader.setIndexDirPath(indexDir);
		assertEquals(indexDir, propReader.getIndexDirPath());
	}

	@Test
	public void testSetResumeDir() throws IOException, MyPropertyFieldException {
		String resumeDir = "/home/dilip/resume/index";
		propReader.setIndexDirPath(resumeDir);
		assertEquals(resumeDir, propReader.getIndexDirPath());
	}

	@Test
	public void testSetLastTimeStamp() throws FileNotFoundException,
			IOException, ParseException {
		long time = 10000;
		Date date = new Date(time);
		propReader.setLastTimeStamp(date);
		assertEquals(time, propReader.getLastTimeStamp().getTime());
	}

}
