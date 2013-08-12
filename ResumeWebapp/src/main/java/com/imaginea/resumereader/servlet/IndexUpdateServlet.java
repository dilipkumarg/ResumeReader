package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imaginea.resumereader.base.ResumeIndexSearcher;
import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;

public class IndexUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		boolean cleanUpdate = Boolean.parseBoolean(req
				.getParameter("cleanUpdate"));
		ResumeIndexSearcher resumeIndexer = null;
		try {
			resumeIndexer = new ResumeIndexSearcher();
		} catch (IOException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
		}
		int numOfUpdated = 0;
		if (cleanUpdate) {
			// performing clean & index
			try {
				numOfUpdated = resumeIndexer.CleanAndIndex();
			} catch (IOException | FileDirectoryEmptyException | ParseException e) {
				res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
			}
		} else {
			// performing only updating index
			try {
				numOfUpdated = resumeIndexer.updateIndex();
			} catch (IOException | FileDirectoryEmptyException | ParseException e) {
				res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
			}
		}
		res.getWriter().print(
				"Number of Files added to the index: " + numOfUpdated);
	}
}
