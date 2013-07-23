package com.imaginea.resumereader.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;

public class DocumentViewerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		//String fileName = req.getParameter("filename");
		PrintWriter printWriter = res.getWriter();

		printWriter.println(getHtml());
	}

	public String getHtml() {
		InputStream input = null;
		String htmlContent = null;
		try {
			StringWriter sw = new StringWriter();
			SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory
					.newInstance();
			TransformerHandler handler = factory.newTransformerHandler();
			handler.getTransformer().setOutputProperty(OutputKeys.METHOD,
					"html");
			handler.getTransformer()
					.setOutputProperty(OutputKeys.INDENT, "yes");
			handler.setResult(new StreamResult(sw));
			input = new FileInputStream(
					"/home/dilip/resumes_sample/Dhiraj_Kumar_D_New_Format.doc");
			DefaultDetector detector = new DefaultDetector();
			Metadata metadata = new Metadata();
			org.apache.tika.parser.Parser parser = new AutoDetectParser(
					detector);
			parser.parse(input, handler, metadata, new ParseContext());
			htmlContent = sw.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return htmlContent;
	}

}
