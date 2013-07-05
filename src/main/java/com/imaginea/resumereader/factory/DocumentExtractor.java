package com.imaginea.resumereader.factory;

import java.io.IOException;

public abstract class DocumentExtractor {
	protected String filePath;

	DocumentExtractor(String filePath) {
		this.filePath = filePath;
	}

	public abstract String readDocument() throws IOException;
}
