package com.imaginea.resumereader.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class PropertiesConfigurationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		JSONObject configValues = new JSONObject();
		PropertyFileReader prop = null;
		try {
			prop = new PropertyFileReader();
		} catch (IOException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			return;
		}
		try {
			configValues.put("resumeDir", prop.getResumeDirPath());
			configValues.put("employeeListFile", prop.getEmployeeExcelPath());
		} catch (FileDirectoryEmptyException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			return;
		}
		res.getWriter().print(configValues.toJSONString());
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		PropertyFileReader prop = null;
		try {
			prop = new PropertyFileReader();
		} catch (IOException e) {
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					e.getMessage());
			return;
		}
		if (req.getParameter("securityKey").trim()
				.equals(prop.getSecurityKey())) {
			prop.setResumeDirPath(req.getParameter("resumeDir"));
			prop.setEmployeeExcelPath(req.getParameter("employeeFile"));
			res.getWriter().print("Successfully updated");
		} else {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Security Key not matched");
		}
	}
}
