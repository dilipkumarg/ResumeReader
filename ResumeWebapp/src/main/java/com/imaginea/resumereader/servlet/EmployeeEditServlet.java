package com.imaginea.resumereader.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
		PrintWriter out = res.getWriter();
		if (req.getHeader("accessKey").equals(prop.getSecurityKey())) {
			try {
				JSONArray reqArray = (JSONArray) new JSONParser().parse(req
						.getParameter("employeeList"));
				List<String> employeeList = jsonToFullFilePathList(reqArray);
				ExcelManager excel = new ExcelManager(
						prop.getEmployeeExcelPath());
				excel.delete(employeeList);
				out.print("Selected Employees(" + employeeList.size()
						+ ") Successfully deleted");
			} catch (ParseException e) {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Invalid Json");
				LOGGER.error(e.getMessage(), e);
				return;
			}
		} else {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Security Key Not Matched");
		}

	}

	private List<String> jsonToFullFilePathList(JSONArray jArray) {
		List<String> filesFullList = new ArrayList<String>();

		for (Object employee : jArray) {
			filesFullList.add(employee.toString());
			// out.println((String) file);
		}
		return filesFullList;
	}

	@SuppressWarnings("unchecked")
	private JSONObject toJson(Map<Integer, String> files) {
		JSONObject lFiles = new JSONObject();
		lFiles.putAll(files);
		return lFiles;
	}
}
