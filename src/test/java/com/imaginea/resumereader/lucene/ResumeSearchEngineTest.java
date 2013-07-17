package com.imaginea.resumereader.lucene;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Test;

public class ResumeSearchEngineTest {

	@Test
	public void test() throws IOException, ParseException {
		String indexDir = this.getClass().getResource("/testIndex").getPath();
		ResumeSearchEngine resumeSearchEngine = new ResumeSearchEngine(
				"content", "filepath", new File(indexDir), 100);
		assertNotNull("The Object should not be null",
				resumeSearchEngine.searchKey("java"));
	}

}
