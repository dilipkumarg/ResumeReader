package com.imaginea.resumereader.indexer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.lucene.store.FSDirectory;

public class ResumeIndexer {
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
	public void indexResumes() throws IOException {
		File indexFile = new File("");
		FSDirectory indexDir = FSDirectory.open(indexFile);
	}
}
