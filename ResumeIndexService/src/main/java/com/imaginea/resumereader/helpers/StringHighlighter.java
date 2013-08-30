package com.imaginea.resumereader.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.imaginea.resumereader.entities.FileInfo;

public class StringHighlighter {
	private final String preTag;
	private final String postTag;

	/**
	 * An unmodifiable set containing some common English words that are not
	 * usually useful for searching.
	 */

	private final List<String> stopWords = Arrays.asList("a", "an", "and",
			"are", "as", "at", "be", "but", "by", "for", "if", "in", "into",
			"is", "it", "no", "not", "of", "on", "or", "such", "that", "the",
			"their", "then", "there", "these", "they", "this", "to", "was",
			"will", "with");
	/**
	 * maxWords is the count of total words before and after the keyword
	 */
	private final int maxWords;

	// TODO delete this function
	public static void main(String[] args) {
		StringHighlighter sh = new StringHighlighter(
				"<span class='highlight'>", "</span>", 20);
		/*
		 * List<String> keywords = new ArrayList<String>();
		 * keywords.add("java"); keywords.add("AND"); keywords.add("java");
		 */

		List<FileInfo> tmpList = new ArrayList<FileInfo>();

		for (int i = 0; i < 5; i++) {
			FileInfo tmp = new FileInfo("", "abc" + i, "", "");
			tmpList.add(tmp);
		}

		// sh.mySplitter(keywords);

		// String[] contextKeys = keywords.split("(?:[^\\s\"]+|\"[^\"]*\")+");

		// System.out.println(contextKeys[0]);
		/*
		 * keywords="\"java script\""; System.out .println(sh
		 * .highlightFragment(
		 * "chalo is that am succedded or not i think i did.. like the way we told this language requires lot of things script hello this is,  scripting languages are more oftenly used i think i succedded in capturing groups please do support me if am not succedded.. i think i did java,"
		 * , keywords));
		 */
		// System.out.println(sh.makeKeyword(keywords));
		ResumeSegregator rs = new ResumeSegregator();
		int i = 0;
		for (FileInfo tmp : rs.removeDuplicates(tmpList)) {
			System.out.println(i++ + ":" + tmp.getTitle());
		}

	}

	/**
	 * Function returns the List of sub strings from given string, splitting
	 * them by space and it ignores the spaces in quoted Strings.
	 * 
	 * @param keyword
	 * @return
	 */
	private List<String> mySplitter(String keyword) {
		List<String> words = new ArrayList<String>();
		Pattern p = Pattern.compile("(?:[^\\s\"]+|\"[^\"]*\")+");
		Matcher m = p.matcher(keyword);

		while (m.find()) {
			words.add(m.group());
		}
		return words;
	}

	private double testRegex(String input) {
		// return input.replaceAll("(?:^|\\s)(?=\\w(?=([\\s]+)\\w[\\s]))", "");
		return PersonNameMatcher.similarity("Bhavani Sankar Jagabhatula",
				"Bhavani Shankar Jagabhatula");
	}

	public StringHighlighter(String preTag, String postTag, int maxWords) {
		this.preTag = preTag;
		this.postTag = postTag;
		this.maxWords = maxWords;
	}

	/**
	 * Function returns the key in the content and surrounds with the pre and
	 * post tags
	 * 
	 * @param content
	 * @param keyword
	 * @return
	 */
	public List<String> highlightFragment(String content, String keyword) {
		Pattern p = Pattern.compile(this.makeRegEx(keyword.trim()),
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		List<String> highlightedString = new ArrayList<String>();
		// Checking if the keyword found in the content
		while (m.find()) {
			highlightedString.add(m.group().replaceAll(
					"(?i)(^|[\\s])" + keyword + "([\\s\\W]|$)",
					"$1" + this.preTag + "$2" + this.postTag + "$3"));
		}

		return highlightedString;
	}

	private String makeKeyword(List<String> words) {
		StringBuilder queryWord = new StringBuilder();

		queryWord.append("(");
		// looping through the array to add remaining elements
		for (String word : words) {
			// checking is it a stop word.
			if (!this.stopWords.contains(word.toLowerCase())) {
				queryWord.append(word.replace("\"", "").trim() + "|");
			}
		}
		// deleting last char i.e slash(|) from the string
		queryWord.deleteCharAt(queryWord.length() - 1);
		queryWord.append(")");
		return queryWord.toString();
	}

	/**
	 * Function returns the key in the content and surrounds with the pre and
	 * post tags, It will evaluates the string array from last index.
	 * 
	 * @param content
	 * @param keyword
	 * @return
	 */
	public List<String> highlightFragments(String content, String keywords) {
		List<String> words = this.mySplitter(keywords);
		return this.highlightFragment(content, this.makeKeyword(words));
	}

	/**
	 * returns the regex string for finding the keyword in the content
	 * 
	 * @param keyword
	 * @return
	 */
	private String makeRegEx(String keyword) {
		String regEx = "(^|([\\S]*[\\s\\n\\t]){1," + this.maxWords + "})"
				+ keyword + "($|[\\W]+([\\S]*([\\s\\n\\t]|$)){1,"
				+ this.maxWords + "})";
		return regEx;
	}

}
