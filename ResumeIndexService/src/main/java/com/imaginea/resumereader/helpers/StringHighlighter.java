package com.imaginea.resumereader.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHighlighter {
	private final String preTag;
	private final String postTag;
	/**
	 * maxWords is the count of total words before and after the keyword
	 */
	private final int maxWords;

	public static void main(String[] args) {
		StringHighlighter sh = new StringHighlighter(
				"<span class='highlight'>", "</span>", 20);
		System.out
				.println(sh
						.getHighlightedString(
								"this is a java file you cannot edit so try to edit in the resume first befor start to edit this file",
								"java"));
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
	public String getHighlightedString(String content, String keyword) {
		Pattern p = Pattern.compile(this.getRegEx(keyword),
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(content);
		String highlightedString = "";
		// Checking if the keyword found in the content
		if (m.find()) {
			highlightedString = m.group().replaceAll("(?i)" + keyword,
					this.preTag + "$0" + this.postTag);
		}

		return highlightedString;
	}

	/**
	 * returns the regex string for finding the keyword in the content
	 * 
	 * @param keyword
	 * @return
	 */
	private String getRegEx(String keyword) {
		String regExStr = "([\\w`~!@#$%^&*_\\-\\(\\)=+'\";:.,></?]*[\\s])"
				+ "{0," + this.maxWords + "}" + keyword
				+ "([\\w`~!@#$%^&*_\\-\\(\\)=+'\";:.,></?]*[\\s])" + "{0,"
				+ this.maxWords + "}";
		return regExStr;
	}

}
