package com.imaginea.resumereader.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.imaginea.resumereader.entities.FileInfo;

public class HitsToJson {
	JSONObject hitsJSON = new JSONObject();
	StringHighlighter sh = new StringHighlighter("<span class='highlight'>",
			"</span>", 20);
	String contextKey;
	List<FileInfo> hits;

	class ForkInHitsToJson extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		static final int FILE_COUNT_THRESHOLD = 1;
		final int start, length;

		ForkInHitsToJson(int start, int length) {
			this.start = start;
			this.length = length;
		}

		@Override
		protected void compute() {
			if ((this.length) <= FILE_COUNT_THRESHOLD) {
				processHits();
			} else {
				int split = this.length / 2;
				/*
				 * List<FileInfo> part1 = splitList(hits, 0, center);
				 * List<FileInfo> part2 = splitList(hits, center, hits.size());
				 */
				invokeAll(
						new ForkInHitsToJson(this.start, split),
						new ForkInHitsToJson(start + split, this.length - split));
			}
		}

		@SuppressWarnings("unchecked")
		protected void processHits() {
			for (int i = this.start; i < this.length + start; i++) {
				JSONObject fileObj = new JSONObject();
				JSONArray fragments = new JSONArray();
				fileObj.put("filepath", hits.get(i).getFilePath());
				fileObj.put("closematch", hits.get(i).getCloseMatch());
				// highlighting the context and putting in array
				for (String highFragment : sh.highlightFragments(hits.get(i)
						.getContent(), contextKey)) {
					fragments.add(highFragment);
				}
				fileObj.put("summary", fragments);
				hitsJSON.put(hits.get(i).getTitle(), fileObj);
			}
		}

		protected <T> List<T> splitList(List<T> array, int start, int end) {
			List<T> part = new ArrayList<T>();
			for (int i = start; i < end; i++) {
				part.add(array.get(i));
			}
			return part;
		}
	}

	public HitsToJson(String contextKey, List<FileInfo> hits) {
		this.contextKey = contextKey;
		this.hits = hits;
	}

	public JSONObject hitsToJson() {
		ForkInHitsToJson process = new ForkInHitsToJson(0, hits.size());
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(process);
		return this.hitsJSON;
	}

}
