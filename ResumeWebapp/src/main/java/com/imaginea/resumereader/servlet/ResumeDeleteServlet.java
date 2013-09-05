package com.imaginea.resumereader.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.FilePathHelper;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class ResumeDeleteServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger LOGGER = Logger.getLogger(this.getClass());
	private String indexDirPath = "";
	private FilePathHelper pathHelper;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		PropertyFileReader prop = null;
		try {
			prop = new PropertyFileReader();
			pathHelper = new FilePathHelper();
			File resumeDir = new File(prop.getResumeDirPath());
			indexDirPath = new File(prop.getIndexDirPath()).getCanonicalPath();
			Map<String, String> filesList = new HashMap<String, String>();

			mapAllFiles(resumeDir, filesList);
			// sending response as json
			res.getWriter().print(toJson(filesList).toJSONString());

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			return;
		} catch (FileDirectoryEmptyException e) {
			LOGGER.error(e.getMessage(), e);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			return;
		}

	}

	private void mapAllFiles(File file, Map<String, String> filesList)
			throws IOException {
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				if (!f.getCanonicalPath().equalsIgnoreCase(indexDirPath)) {
					mapAllFiles(f, filesList);
				}
			} else {
				String relPath = pathHelper.extractRelativePath(f
						.getCanonicalPath());
				filesList.put(relPath, f.getName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject toJson(Map<String, String> files) {
		JSONObject lFiles = new JSONObject();
		lFiles.putAll(files);
		return lFiles;
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		PrintWriter out = res.getWriter();
		try {
			JSONArray filesList = (JSONArray) new JSONParser().parse(req
					.getParameter("filesList"));

			for (Object file : filesList) {
				out.println((String) file);
			}
			
			// TODO delete files here

		} catch (ParseException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Json");
		}
	}
}
