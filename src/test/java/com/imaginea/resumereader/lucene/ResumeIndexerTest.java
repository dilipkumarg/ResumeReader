package com.imaginea.resumereader.lucene;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;

public class ResumeIndexerTest {
	@Test
	public void testFilesIndexed() throws IOException, MyPropertyFieldException {
		String indexDir = this.getClass().getResource("/testIndex").getPath();
		String resumeDir = this.getClass().getResource("/testResumes")
				.getPath();
		FileIndexer indexer = new FileIndexer(new File(indexDir), new File(
				resumeDir), "content", "filepath");
		assertTrue(0 <= indexer.appendIndex(new Date(0)));
	}

}
