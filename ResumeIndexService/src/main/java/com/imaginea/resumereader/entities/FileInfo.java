package com.imaginea.resumereader.entities;

/**
 * Created with IntelliJ IDEA. User: Dilip Kumar Date: 30/7/13 Time: 1:25 AM To
 * change this template use File | Settings | File Templates.
 */
public class FileInfo {
	private final String filePath;
	private final String title;
	private String closeMatch;
	private final String summary;
	private final String content;

	public FileInfo(String filePath, String title, String summary,
			String content) {
		this.filePath = filePath;
		this.title = title;
		this.summary = summary;
		this.content = content;
		this.closeMatch = "";
	}

	public String getFilePath() {
		return this.filePath;
	}

	public String getTitle() {
		return this.title;
	}

	public String getSummary() {
		return this.summary;
	}

	public String toString() {
		return "Title:" + this.title + "\n Summary: " + this.summary
				+ "\n File Path: " + this.filePath + "\n\n";
	}

	public String getContent() {
		return this.content;
	}

	public String getCloseMatch() {
		return closeMatch;
	}

	public void setCloseMatch(String closeMatch) {
		this.closeMatch = closeMatch;
	}
}
