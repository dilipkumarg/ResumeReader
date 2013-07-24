package com.imaginea.resumereader.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;

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
			input = new FileInputStream(filePath);
			DefaultDetector detector = new DefaultDetector();
			Metadata metadata = new Metadata();
			org.apache.tika.parser.Parser parser = new AutoDetectParser(
					detector);
			parser.parse(input, handler, metadata, new ParseContext());
			htmlContent = cleanHTML(sw.toString());
		} finally {
			input.close();
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

	private String cleanHTML(String htmlContent) {
		final HtmlCleaner cleaner = new HtmlCleaner();
		final TagNode rootTagNode = cleaner.clean(htmlContent);

		// setting up properties for the serializer
		final CleanerProperties cleanerProperties = cleaner.getProperties();
		cleanerProperties.setOmitXmlDeclaration(true);

		// use the getAsString method on an XmlSerializer class
		final XmlSerializer xmlSerializer = new PrettyXmlSerializer(
				cleanerProperties);
		final String html = xmlSerializer.getAsString(rootTagNode);
		return html;
	}

}
