package com.imaginea.resumereader.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.tika.exception.TikaException;

public class ResumeMetaExtractor {
	private List<String> dictionary = null;

	public ResumeMetaExtractor() {

	}

	public String getResumeSummary(String body) throws IOException,
			TikaException {
		String lineSeparator = System.getProperty("line.separator");
		String[] paragraphs = body.split(lineSeparator);
		for (int i = 0; i < paragraphs.length; i++) {
			// check for REPLACEMENT CHARACTER(\uFFFD) which is used to replace
			// an incoming character whose value is unknown or unrepresentable
			// in Unicode
			if ((paragraphs[i].split(" ").length > 5
					&& !paragraphs[i].contains("\uFFFD") && paragraphs[i]
					.trim().length() != 1) || paragraphs[i].contains("years")) {
				return paragraphs[i];
			}
		}
		return "";
	}

	public String getPersonName(String body) throws IOException, TikaException {
		int i = 0;
		this.initializeDictionary();
		StringBuffer Name = new StringBuffer();
		for (String retval : body.split("\n")) {
			if (!(retval.isEmpty() || retval.trim().equals("") || retval.trim()
					.equals("\n")) && ++i <= 3 && retval.split(" ").length < 7) {
				for (String word : retval.split(" ")) {
					if (word.endsWith(".")) {
						word = word.substring(0, word.length() - 1);
					}
					word = word.replaceAll("[^a-zA-Z]", "");
					if (!this.dictionary.contains(word.toLowerCase()))
						Name.append(word.trim() + " ");
				}
			}
		}
		return Name.toString().trim();
	}

	// lazy initialization of dictionary
	private void initializeDictionary() throws FileNotFoundException {
		// initializing dictionary if and only if dictionary is null
		if (this.dictionary == null) {
			Scanner s = new Scanner(this.getClass().getClassLoader()
					.getResourceAsStream("american-english"));
			this.dictionary = new ArrayList<String>();
			while (s.hasNext()) {
				this.dictionary.add(s.next());
			}
			s.close();
			addCustomWordsToDictinary();
		}
	}

	private void addCustomWordsToDictinary() throws FileNotFoundException {
		if (dictionary != null) {
			// add custom words to dictionary, to get effective filename
			dictionary.add("QA");
		} else {
			initializeDictionary();
		}
	}

}
