package com.imaginea.resumereader.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public String extractPersonName(String text) throws FileNotFoundException {
		this.initializeDictionary();
		String personName = "PERSON_NAME";
		for (String sentence : text.split("\n")) {
			if (isValidSentence(sentence)) {
				String[] words = sentence.split("\\s+");
				StringBuilder scoreString = new StringBuilder();
				for (int i = 0; i < words.length; i++) {
					scoreString.append(getWordScore(words[i]));
				}
				String resultScoreString = getNamePatternFromScore(scoreString
						.toString());

				if (!resultScoreString.equalsIgnoreCase("")) {
					personName = getNameFromScore(scoreString.toString(),
							resultScoreString, words);
					if (!personName.isEmpty()) {
						break;
					}
				}
			}
		}

		return personName;
	}

	/**
	 * score is used to detect type of word. <br>
	 * valid english word: 0 <br>
	 * a-z : 1 <br>
	 * a-z. : 2 <br>
	 * a-z++. : 3 <br>
	 * non english word: 4
	 * 
	 * @param word
	 * @return
	 */

	private int getWordScore(String word) {
		int score = 0;
		// checking if word contains letters and only one dot.
		if (Pattern.matches("([a-zA-Z])*[\\.]?([a-zA-Z])*", word)) {
			if (word.length() == 1 && !word.contains(".")) {
				score = 1;
			} else if (word.length() > 1) {
				// filtering a. and aa. type of words and ignoring only one .
				if (word.endsWith(".")) {
					if (word.length() == 2) {
						score = 2;
					} else {
						// if the word is mr. mrs. ignoring.otherwise score is 3
						score = (isDictionaryWord(word.substring(0,
								word.length() - 2)) ? 0 : 3);
					}
				} else {
					score = (isDictionaryWord(word) ? 0 : 4);
				}
			}
		} else {
			score = 5;
		}
		return score;
	}

	private String getNamePatternFromScore(String scoreString) {
		Pattern myPattern = Pattern
				.compile("([3]|([1]|[2])+)?[0]?[4]+[0]?[4]*(([0]?)([3]|([1]|[2])+))?");
		Matcher matcher = myPattern.matcher(scoreString);
		String resultScoreString = "";
		if (matcher.find()) {
			resultScoreString = matcher.group(0);
		}
		return resultScoreString;
	}

	private String getNameFromScore(String scoreString, String resultString,
			String[] words) {
		StringBuilder nameString = new StringBuilder();
		int stringStartPos = scoreString.indexOf(resultString);
		int stringEndPos = stringStartPos + resultString.length();

		for (int i = stringStartPos; i < stringEndPos; i++) {
			nameString.append(words[i] + " ");
		}
		return nameString.toString().trim();
	}

	private boolean isDictionaryWord(String word) {
		return this.dictionary.contains(word.toLowerCase());
	}

	private boolean isValidSentence(String sentence) {
		return !sentence.trim().isEmpty()
				|| (sentence.split(" ").length > 8 ? false : true);

	}

	// lazy initialization of dictionary
	private void initializeDictionary() throws FileNotFoundException {
		// initializing dictionary if and only if dictionary is null
		if (this.dictionary == null) {
			Scanner s = new Scanner(this.getClass().getClassLoader()
					.getResourceAsStream("american-english"));
			this.dictionary = new ArrayList<String>();
			while (s.hasNext()) {
				this.dictionary.add(s.next().toLowerCase());
			}
			s.close();
			// adding tech abbrevations 
			Scanner abbrev = new Scanner(this.getClass().getClassLoader()
					.getResourceAsStream("abbrevations_tech.txt"));
			while (abbrev.hasNext()) {
				this.dictionary.add(abbrev.next().toLowerCase());
			}
			abbrev.close();
			addCustomWordsToDictionary();
		}
	}

	private void addCustomWordsToDictionary() throws FileNotFoundException {
		if (dictionary != null) {
			// add custom words to dictionary, to get effective filename
			dictionary.add("qa");
			dictionary.add("MERGEFORMAT".toLowerCase());
			initializeDictionary();
		}
	}

}
