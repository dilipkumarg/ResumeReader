package com.imaginea.resumereader.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	public ResumeDeleteServlet() throws FileDirectoryEmptyException,
			IOException {
		pathHelper = new FilePathHelper();

	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		PropertyFileReader prop = null;
		try {
			prop = new PropertyFileReader();
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
			JSONArray reqArray = (JSONArray) new JSONParser().parse(req
					.getParameter("filesList"));
			List<String> filesList = jsonToFullFilePathList(reqArray);
			List<String> notDeleted = deleteFiles(filesList);

			if (notDeleted.size() == 0 || filesList.size() == 0) {
				out.print("Selected Files(" + filesList.size()
						+ ") Successfully deleted");
			} else if (notDeleted.size() < filesList.size()) {
				out.print("Some of the files or not deleted");
				for (String filePath : notDeleted) {
					out.println(filePath);
				}
			} else {
				res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"some thing went wrong. please try again");
			}

		} catch (ParseException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Json");
			LOGGER.error(e.getMessage(), e);
			return;
		}
	}

	private List<String> jsonToFullFilePathList(JSONArray jArray) {
		List<String> filesFullList = new ArrayList<String>();

		for (Object file : jArray) {
			filesFullList.add(pathHelper.getCanonicalPath((String) file));
			// out.println((String) file);
		}
		return filesFullList;
	}

	private List<String> deleteFiles(List<String> fileList) {
		List<String> notDeleted = new ArrayList<String>();
		for (String filePath : fileList) {
			LOGGER.info("Deleting File:" + filePath);
			File file = new File(filePath);
			// if file not deleted adding to not Deleted list
			if (!file.delete()) {
				LOGGER.error("File Deletion failed:" + filePath);
				notDeleted.add(filePath);
			}
		}
		return notDeleted;
	}
}
