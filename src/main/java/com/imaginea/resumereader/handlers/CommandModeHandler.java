package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;
import com.imaginea.resumereader.services.ResumeService;

public class CommandModeHandler implements Handler {
	ResumeService resumeService;
	private Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public CommandModeHandler() throws IOException {
		resumeService = new ResumeService();
	}

	public void intialize(String[] args) throws IOException,
			MyPropertyFieldException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		String command = args[0];
		if (command.equalsIgnoreCase("update")) {
			resumeService.updateIndex();
		} else {
			// calling function because other than update we need one more
			// parameter
			try {
				doOtherTasks(args);
			} catch (IllegalArgumentException iae) {
				LOGGER.log(Level.SEVERE,
						"Illegal argument exception occured,\n ERROR: {0}",
						new Object[] { iae.getMessage() });
			}
		}
	}

	private void doOtherTasks(String[] args) throws IOException,
			MyPropertyFieldException,
			org.apache.lucene.queryparser.classic.ParseException {
		String command = args[0];
		if (command.equalsIgnoreCase("indexdir")) {
			if (args.length < 2) {
				throw new IllegalArgumentException(
						"Need one more parameter to perform this operation");
			}
			resumeService.setIndexDirPath(args[1]);
		} else if (command.equalsIgnoreCase("filedir")) {
			if (args.length < 2) {
				throw new IllegalArgumentException(
						"Need one more parameter to perform this operation");
			}
			resumeService.setFileDirPath(args[1]);
		} else if (command.equalsIgnoreCase("search")) {
			if (args.length < 2) {
				throw new IllegalArgumentException(
						"Need one more parameter to perform this operation");
			}
			resumeService.search(args[1]);
		} else {
			throw new IllegalArgumentException("Command not found");
		}
	}
}
