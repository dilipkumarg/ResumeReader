package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONObject;

import com.imaginea.resumereader.base.ResumeIndexSearcher;
import com.imaginea.resumereader.entities.FileInfo;
import com.imaginea.resumereader.entities.SearchResult;
import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.ExcelManager;
import com.imaginea.resumereader.helpers.HitsToJson;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.helpers.ResumeSegregator;

public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger LOGGER = Logger.getLogger(this.getClass());
	private String contextKey;
	private long segTime = 0;
	private long highTime = 0;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		String searchKey = req.getParameter("searchKey");
		contextKey = req.getParameter("contextKey");
		// making context as search key if no context is present
		contextKey = (contextKey.trim().isEmpty() ? searchKey : contextKey);
		PrintWriter printWriter = res.getWriter();
		ResumeIndexSearcher resumeSearchService = new ResumeIndexSearcher();
		SearchResult searchResult = null;
		ExcelManager excelReader = new ExcelManager(
				PropertyFileReader.getInstance().getEmployeeExcelPath());
		try {
			searchResult = resumeSearchService.search(searchKey, false);
		} catch (FileDirectoryEmptyException e) {
			LOGGER.error(e.getMessage());
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Index Directory Not Set");
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"ParseException Occured <br> Error:" + e.getMessage());
		}
		// searchResult.setTopHits(removeDuplicates(searchResult.getTopHits()));
		ResumeSegregator resumeSegregator = new ResumeSegregator();
		long startTime = System.currentTimeMillis();
		resumeSegregator.compareWithEmployeeList(
				resumeSegregator.removeDuplicates(searchResult.getTopHits()),
				excelReader.read(null));
		segTime = System.currentTimeMillis() - startTime;
		printWriter.print(toJsonString(searchResult, resumeSegregator));
	}

	@SuppressWarnings("unchecked")
	private JSONObject toJsonString(SearchResult searchResult,
			ResumeSegregator resumeSegregator) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalHits", searchResult.getTopHits().size());
		jsonObj.put("searchDuration", searchResult.getSearchDuration());
		jsonObj.put("searchKey", searchResult.getQuery());
		jsonObj.put("contextKey", contextKey);
		jsonObj.put("SegregationTime", segTime);

		long startTime = System.currentTimeMillis();
		jsonObj.put("activeHits",
				hitsToJson(resumeSegregator.getActiveEmployees()));
		jsonObj.put("inActiveHits",
				hitsToJson(resumeSegregator.getInactiveEmployees()));
		jsonObj.put("probableHits",
				hitsToJson(resumeSegregator.getProbableActiveEmployess()));
		highTime = System.currentTimeMillis() - startTime;
		jsonObj.put("highlightTime", highTime);
		return jsonObj;
	}

	private JSONObject hitsToJson(List<FileInfo> hits) {
		HitsToJson hitsJson = new HitsToJson(this.contextKey, hits);
		return hitsJson.hitsToJson();
	}
}
