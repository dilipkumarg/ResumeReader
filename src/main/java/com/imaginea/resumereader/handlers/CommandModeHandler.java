package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.ErrorCode;
import com.imaginea.resumereader.exceptions.MyPropertyFieldException;
import com.imaginea.resumereader.services.ResumeService;

public class CommandModeHandler extends Handler {
	ResumeService resumeService;
	private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public CommandModeHandler(String[] args) throws IOException {
		super(args);
		resumeService = new ResumeService();
	}

	public void intialize() throws IOException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		String command = this.args[0];
		try {
			if (command.equalsIgnoreCase("update")) {
				this.update();
			} else if (command.equalsIgnoreCase("indexdir")) {
				this.setIndexDirPath();
			} else if (command.equalsIgnoreCase("resumedir")) {
				this.setResumeDirPath();
			} else if (command.equalsIgnoreCase("search")) {
				this.search();
			} else {
				throw new IllegalArgumentException("Command not found");
			}
		} catch (IllegalArgumentException iae) {
			LOGGER.log(Level.SEVERE,
					"IllegalArgumentException occured,\n Error:{0}",
					new Object[] { iae.getMessage() });
		}
	}

	private void update() throws IOException, ParseException {
		try {
			resumeService.updateIndex();
		} catch (MyPropertyFieldException mpe) {
			this.printPropertyExceptionHelp(mpe);
		}
	}

	private void setIndexDirPath() throws IOException {
		if (this.args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		resumeService.setIndexDirPath(this.args[1]);
	}

	private void setResumeDirPath() throws IOException {
		if (this.args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		resumeService.setResumeDirPath(this.args[1]);
	}

	private void search() throws IOException,
			org.apache.lucene.queryparser.classic.ParseException {
		if (this.args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		try {
			resumeService.search(this.args[1]);
		} catch (MyPropertyFieldException mpe) {
			this.printPropertyExceptionHelp(mpe);
		}
	}

	private void printPropertyExceptionHelp(MyPropertyFieldException mpe) {
		if (mpe.getErrorCode() == ErrorCode.RESUME_DIR_EMPTY) {
			System.out
					.println("Resume Directory path not exists in the property File"
							+ "\nplease set using \"resumedir <path>\"");
		} else if (mpe.getErrorCode() == ErrorCode.INDEX_DIR_EMPTY) {
			System.out
					.println("Index Directory path not exists in the property File"
							+ "\nplease set using \"indexdir <path>\"");
		} else {
			System.out.println(mpe.getMessage());
		}
	}
}
