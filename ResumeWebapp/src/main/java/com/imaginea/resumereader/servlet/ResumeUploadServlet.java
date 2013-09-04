package com.imaginea.resumereader.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;

public class ResumeUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Logger LOGGER = Logger.getLogger(this.getClass());
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		// checking whether user sending MultiPart content or not
		if (ServletFileUpload.isMultipartContent(request)) {
			String resumeDir = "";
			try {
				// getting resume directory from property file
				resumeDir = new PropertyFileReader().getResumeDirPath();
			} catch (FileDirectoryEmptyException e1) {
				LOGGER.fatal(e1.getMessage(), e1);
				response.sendError(HttpServletResponse.SC_NOT_FOUND,
						e1.getMessage());
				return;
			}
			try { // writing to file
				if (writeToFile(resumeDir, request)) {
					out.print("Successfully uploaded");
				} else {
					response.sendError(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"something went wrong. please try again.");
				}

			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				response.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						e.getMessage());
			}
		} else {
			LOGGER.warn("It is not a multipart content. skipping upload");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"You are not trying to upload");
		}
	}

	// this function is for writing files into the disk
	private boolean writeToFile(String resumeDir, HttpServletRequest req)
			throws Exception {
		boolean status = false;
		// checking whether it ended with (/)
		resumeDir = (resumeDir.endsWith(File.separator) ? resumeDir : resumeDir
				.concat(File.separator));
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fields = upload.parseRequest(req);
		Iterator<FileItem> it = fields.iterator();
		if (it.hasNext()) {
			FileItem fileItem = it.next();
			if (!fileItem.isFormField()) {
				String fileName = fileItem.getName();
				File file = new File(resumeDir + fileName);
				int i = 1;
				// if file already exists appending suffix
				while (file.exists()) {
					int j = fileName.lastIndexOf(".");
					String newName = fileName.substring(0, j) + "_" + i++
							+ fileName.substring(j);

					file = new File(resumeDir + newName);
				}
				LOGGER.info("uploading new resume : " + fileItem.getName());
				fileItem.write(file);
				status = true;

			}
		} else {
			LOGGER.info("No file fields found");
		}
		return status;
	}


}
