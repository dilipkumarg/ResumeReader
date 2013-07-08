package com.imaginea.resumereader.lucene;

import java.util.List;

public class SearchResult {
	private final List<String> topHits;
	private final int totalHitCount;
	private final long searchDuration;
	private final String query;

	public SearchResult(List<String> topHits, int totalHitCount,
			long searchDuration, String query) {
		this.topHits = topHits;
		this.totalHitCount = totalHitCount;
		this.searchDuration = searchDuration;
		this.query = query;
	}

	public List<String> getTopHits() {
		return this.topHits;
	}

	public int getTotalHitCount() {
		return this.totalHitCount;
	}

	public long searchDuration() {
		return this.searchDuration;
	}

	public String query() {
		return this.query;
	}
}
