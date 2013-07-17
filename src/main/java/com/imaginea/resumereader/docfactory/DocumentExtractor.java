package com.imaginea.resumereader.docfactory;

import java.io.File;
import java.io.IOException;

public abstract class DocumentExtractor {
	protected File docFile;

	DocumentExtractor(String filePath) {
		this.docFile = new File(filePath);
	}

	public abstract String getTextContent() throws IOException;
}
