package com.imaginea.resumereader.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imaginea.resumereader.helpers.ExcelManager;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class ExcelWriterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		ExcelManager excelManager = null;
		PropertyFileReader prop = null;
		prop = new PropertyFileReader();
		if (req.getHeader("accessKey").equals(prop.getSecurityKey())) {
			try {
				excelManager = new ExcelManager(prop.getEmployeeExcelPath());
			} catch (IOException e) {
				res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
				return;
			}
			excelManager.write(req.getParameter("empName"),
					Integer.parseInt(req.getParameter("empId")));
			res.getWriter().print("Employee Successfully added");
		} else {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Security Key not matched");
		}
	}
}
