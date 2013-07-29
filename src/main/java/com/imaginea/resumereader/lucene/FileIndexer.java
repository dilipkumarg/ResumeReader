package com.imaginea.resumereader.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class FileIndexer extends Indexer {
	private static final Logger LOGGER = Logger.getLogger(FileIndexer.class
			.getName());
	private List<String> dictionary;
	public FileIndexer(File indexDirFile, String resumeContentField,
			String resumePathField, String resumeNameField) throws IOException {
		super(indexDirFile, resumeContentField, resumePathField, resumeNameField);
		dictionary = this.getDictionary();
	}

	public void indexFiles(List<File> filesToIndex, int pathLength) throws IOException {
		String absoluteFilePath, relativeFilePath, fileContent, name;
		for (File file : filesToIndex) {
			absoluteFilePath = file.getCanonicalPath();
			relativeFilePath = absoluteFilePath.substring(pathLength);
			try {
				fileContent = getTextContent(absoluteFilePath);
				name = getPersonName(fileContent, dictionary);
				this.index(fileContent, relativeFilePath, name);
			} catch (SAXException sae) {
				LOGGER.log(Level.INFO, sae.getMessage());
			} catch (TikaException te) {
				LOGGER.log(Level.INFO, te.getMessage());
			}
		}
		this.commitAndCloseIndexer();
	}

	private String getTextContent(String filePath) throws IOException,
			SAXException, TikaException {
		InputStream inputStream = new FileInputStream(filePath);
		StringWriter stringWriter = new StringWriter();
		BodyContentHandler handler = new BodyContentHandler(stringWriter);
		Metadata metadata = new Metadata();
		Parser parser = new AutoDetectParser();

		parser.parse(inputStream, handler, metadata, new ParseContext());

		return stringWriter.toString();
	}
	private String getPersonName(String body, List<String> dictionary)
			throws IOException, TikaException {
		int i = 0;
		StringBuffer Name = new StringBuffer();
		for (String retval : body.split("\n")) {
			if (!(retval.isEmpty() || retval.trim().equals("") || retval.trim()
					.equals("\n")) && ++i <= 3 && retval.split(" ").length < 5) {
				for (String word : retval.split(" ")) {
					if(word.endsWith(".")){
						word = word.substring(0,word.length()-1);
					}
					word = word.replaceAll("[^a-zA-Z]", "");
					if (!dictionary.contains(word.toLowerCase()))
						Name.append(word.trim()+" ");
				}
			}
		}
		return Name.toString().trim();
	}

	private List<String> getDictionary() throws FileNotFoundException {
		Scanner s = new Scanner(new File("/usr/share/dict/american-english"));
		List<String> dictionary = new ArrayList<String>();
		while (s.hasNext()) {
			dictionary.add(s.next());
		}
		s.close();
		return dictionary;
	}
}
