package com.imaginea.resumereader.lucene;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class ResumeIndexerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFilesIndexed() throws IOException, MyPropertyFieldException {
		PropertyFileReader propertyFileReader = new PropertyFileReader();
		ResumeIndexer indexer = new ResumeIndexer(new File(propertyFileReader.getIndexDirPath()), new File(propertyFileReader.getResumeDirPath()), "resumeContentField", "resumePathField");
		assertEquals("0 Files must be indexed", 0, indexer.appendIndexDirectory(new Date(System.currentTimeMillis())));
	}

}
