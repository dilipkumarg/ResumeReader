package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.lucene.SearchResult;
import com.imaginea.resumereader.services.ResumeService;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		String searchKey = req.getParameter("searchKey");
		PrintWriter printWriter = res.getWriter();
		ResumeService resumeService = new ResumeService();
		SearchResult searchResult = null;
		try {
			searchResult = resumeService.search(searchKey);
		} catch (IndexDirectoryEmptyException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Index Directory Not Set");
		} catch (ParseException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"ParseException Occured <br> Error:" + e.getMessage());
		}
		printWriter.print(toJsonString(searchResult));
	}

	@SuppressWarnings("unchecked")
	private JSONObject toJsonString(SearchResult searchResult) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalHits", searchResult.getTotalHitCount());
		jsonObj.put("searchDuration", searchResult.getSearchDuration());
		jsonObj.put("searchKey", searchResult.getQuery());

		JSONArray topHits = new JSONArray();
		for (String filePath : searchResult.getTopHits()) {
			topHits.add(filePath);
		}
		jsonObj.put("topHits", topHits);

		return jsonObj;
	}
}
