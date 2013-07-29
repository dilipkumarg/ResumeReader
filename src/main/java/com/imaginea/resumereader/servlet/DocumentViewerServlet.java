package com.imaginea.resumereader.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.ToHTMLContentHandler;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class DocumentViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger
			.getLogger(DocumentViewerServlet.class.getName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		String filePath = req.getParameter("filename");
		String resumePath = "";
		try {
			resumePath = getResumeCananicalPath(filePath);
		} catch (FileDirectoryEmptyException e) {
			return;
		}
		PrintWriter printWriter = res.getWriter();

		try {
			printWriter.println(getHtml(resumePath));
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, ioe.getMessage(), ioe);
			res.sendError(HttpServletResponse.SC_NOT_FOUND, ioe.getMessage());
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					ex.getMessage());
		}
	}

	public String getHtml(String filePath) throws IOException, Exception {
		InputStream inputStream = null;
		String htmlContent = null;
		try {
			inputStream = new FileInputStream(filePath);
			ToHTMLContentHandler toHTMLHandler = new ToHTMLContentHandler();
			Metadata metadata = new Metadata();
			Parser parser = new AutoDetectParser();

			parser.parse(inputStream, toHTMLHandler, metadata,
					new ParseContext());
			htmlContent = toHTMLHandler.toString();
		} finally {
			inputStream.close();
		}
		return htmlContent;
	}

	private String getResumeCananicalPath(String relPath) throws IOException,
			FileDirectoryEmptyException {
		PropertyFileReader propReader = new PropertyFileReader();
		String resumeDirPath = propReader.getResumeDirPath();
		// removing /(slash) 's
		if (resumeDirPath.endsWith(File.separator)) {
			resumeDirPath = resumeDirPath.substring(0,
					resumeDirPath.length() - 2);
		}
		if (relPath.startsWith(File.separator)) {
			relPath = relPath.substring(1);
		}
		// appending file path with resume directory
		return resumeDirPath.concat(File.separator + relPath);
	}
}
