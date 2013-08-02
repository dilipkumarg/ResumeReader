package com.imaginea.resumereader.entities;

import java.util.List;

public class SearchResult {
	private List<FileInfo> topHits;
	private final int totalHitCount;
	private final long searchDuration;
	private final String query;

	public SearchResult(List<FileInfo> topHits, int totalHitCount,
			long searchDuration, String query) {
		this.topHits = topHits;
		this.totalHitCount = totalHitCount;
		this.searchDuration = searchDuration;
		this.query = query;
	}

	public List<FileInfo> getTopHits() {
		return this.topHits;
	}

	public int getTotalHitCount() {
		return this.totalHitCount;
	}

	public long getSearchDuration() {
		return this.searchDuration;
	}

	public String getQuery() {
		return this.query;
	}

	public void setTopHits(List<FileInfo> topHits) {
		this.topHits = topHits;
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Query: " + query);
		str.append("\nTotal Hits: " + totalHitCount);
		str.append("\nSearch Duration: " + searchDuration);
		str.append("\n*****TOP HITS*****");

		for (FileInfo fileInfo : topHits) {
			str.append("\n" + fileInfo.toString());
		}
		return str.toString();
	}
}