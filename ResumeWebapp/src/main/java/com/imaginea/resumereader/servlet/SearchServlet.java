package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.lucene.FileInfo;
import com.imaginea.resumereader.lucene.SearchResult;
import com.imaginea.resumereader.services.ResumeSearchService;

public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PropertyFileReader properties;

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        properties = new PropertyFileReader();
        String searchKey = req.getParameter("searchKey");
        PrintWriter printWriter = res.getWriter();
        ResumeSearchService resumeSearchService = new ResumeSearchService();
        SearchResult searchResult = null;
        try {
            searchResult = resumeSearchService.search(searchKey, properties.getIndexDirPath());
        } catch (FileDirectoryEmptyException e) {
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
