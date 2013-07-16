package com.imaginea.resumereader.lucene;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class ResumeSearchEngineTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws IOException, MyPropertyFieldException, ParseException {
		PropertyFileReader propertyFileReader = new PropertyFileReader();
		ResumeSearchEngine resumeSearchEngine = new ResumeSearchEngine("defaultField", "fileNameField",
				new File(propertyFileReader.getIndexDirPath()), 100);
		assertNotNull("The Object should not be null", resumeSearchEngine.searchKey("java"));
	}

}
