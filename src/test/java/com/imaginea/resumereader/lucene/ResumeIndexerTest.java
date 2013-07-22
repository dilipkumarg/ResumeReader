package com.imaginea.resumereader.lucene;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;

public class ResumeIndexerTest {
	@Test
	public void testFilesIndexed() throws IOException, FileDirectoryEmptyException {
		String indexDir = this.getClass().getResource("/testIndex").getPath();
		String resumeDir = this.getClass().getResource("/testResumes")
				.getPath();
		Indexer indexer = new Indexer(new File(indexDir), new File(
				resumeDir), "content", "filepath");
		assertTrue(0 <= indexer.appendIndex(new Date(0)));
	}

}
