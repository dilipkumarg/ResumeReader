package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.imaginea.resumereader.base.ResumeIndexSearcher;
import com.imaginea.resumereader.entities.FileInfo;
import com.imaginea.resumereader.entities.SearchResult;
import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PropertyFileReader properties;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		properties = new PropertyFileReader();
		String searchKey = req.getParameter("searchKey");
		PrintWriter printWriter = res.getWriter();
		ResumeIndexSearcher resumeSearchService = new ResumeIndexSearcher();
		SearchResult searchResult = null;
		try {
			searchResult = resumeSearchService.search(searchKey,
					properties.getIndexDirPath());
		} catch (FileDirectoryEmptyException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Index Directory Not Set");
		} catch (ParseException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"ParseException Occured <br> Error:" + e.getMessage());
		}
		searchResult.setTopHits(removeDuplicates(searchResult.getTopHits()));
		printWriter.print(toJsonString(searchResult));
	}

	private List<FileInfo> removeDuplicates(List<FileInfo> listOfFiles) {
		List<String> names = new ArrayList<String>();
		for (FileInfo fileInfo : listOfFiles) {
			if (names.contains(fileInfo.getTitle())) {
				listOfFiles.remove(fileInfo);
			} else {
				names.add(fileInfo.getTitle());
			}
		}
		return listOfFiles;
	}

	@SuppressWarnings("unchecked")
	private JSONObject toJsonString(SearchResult searchResult) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalHits", searchResult.getTotalHitCount());
		jsonObj.put("uniqueResults", searchResult.getTopHits().size());
		jsonObj.put("searchDuration", searchResult.getSearchDuration());
		jsonObj.put("searchKey", searchResult.getQuery());

		JSONArray topHits = new JSONArray();
		for (FileInfo hit : searchResult.getTopHits()) {
			JSONObject fileObj = new JSONObject();
			fileObj.put("title", hit.getTitle());
			fileObj.put("summary", hit.getSummary());
			fileObj.put("filepath", hit.getFilePath());
			topHits.add(fileObj);
		}
		jsonObj.put("topHits", topHits);
		return jsonObj;
	}
}
