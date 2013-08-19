package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.imaginea.resumereader.helpers.ExcelReader;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.helpers.ResumeSegregator;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		String searchKey = req.getParameter("searchKey");
		PrintWriter printWriter = res.getWriter();
		ResumeIndexSearcher resumeSearchService = new ResumeIndexSearcher();
		SearchResult searchResult = null;
		ExcelReader excelReader = new ExcelReader(
				new PropertyFileReader().getEmployeeExcelPath());
		try {
			searchResult = resumeSearchService.search(searchKey, false);
		} catch (FileDirectoryEmptyException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Index Directory Not Set");
		} catch (ParseException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"ParseException Occured <br> Error:" + e.getMessage());
		}
		// searchResult.setTopHits(removeDuplicates(searchResult.getTopHits()));
		ResumeSegregator resumeSegregator = new ResumeSegregator();
		resumeSegregator.findMaxSimilarity(searchResult.getTopHits(), excelReader.read());
		printWriter.print(toJsonString(searchResult, resumeSegregator));
	}

	@SuppressWarnings("unchecked")
	private JSONObject toJsonString(SearchResult searchResult,
			ResumeSegregator resumeSegregator) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalHits", searchResult.getTopHits().size());
		jsonObj.put("searchDuration", searchResult.getSearchDuration());
		jsonObj.put("searchKey", searchResult.getQuery());

		jsonObj.put("activeHits",
				hitsToJson(resumeSegregator.getActiveEmployees()));
		jsonObj.put("inActiveHits",
				hitsToJson(resumeSegregator.getInactiveEmployees()));
		jsonObj.put("probableHits",
				hitsToJson(resumeSegregator.getProbableActiveEmployess()));

		return jsonObj;
	}

	@SuppressWarnings("unchecked")
	private JSONArray hitsToJson(List<FileInfo> hits) {
		JSONArray hitsJSON = new JSONArray();
		for (FileInfo hit : hits) {
			JSONObject fileObj = new JSONObject();
			fileObj.put("title", hit.getTitle());
			fileObj.put("summary", hit.getSummary());
			fileObj.put("filepath", hit.getFilePath());
			hitsJSON.add(fileObj);
		}
		return hitsJSON;
	}
}
