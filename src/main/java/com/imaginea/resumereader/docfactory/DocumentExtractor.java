package com.imaginea.resumereader.docfactory;

import java.io.IOException;

public interface DocumentExtractor {
	public abstract String getTextContent(String filePath) throws IOException;
}
