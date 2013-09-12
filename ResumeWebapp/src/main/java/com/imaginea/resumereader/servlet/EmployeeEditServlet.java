package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.imaginea.resumereader.helpers.ExcelManager;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class EmployeeEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger LOGGER = Logger.getLogger(this.getClass());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		PropertyFileReader prop = null;
		try {
			prop = new PropertyFileReader();
			ExcelManager excel = new ExcelManager(prop.getEmployeeExcelPath());
			res.getWriter().print(toJson(excel.read(null)).toJSONString());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			return;
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		PropertyFileReader prop = new PropertyFileReader();
		String actions[] = req.getParameterValues("employeeList");
		List<String> wordList = Arrays.asList(actions);
		ExcelManager excel = new ExcelManager(prop.getEmployeeExcelPath());
		excel.read(wordList);
	}

	@SuppressWarnings("unchecked")
	private JSONObject toJson(Map<Integer, String> files) {
		JSONObject lFiles = new JSONObject();
		lFiles.putAll(files);
		return lFiles;
	}
}
