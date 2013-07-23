package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;

import org.apache.lucene.index.IndexNotFoundException;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.lucene.SearchResult;
import com.imaginea.resumereader.services.ResumeService;

public class CommandModeHandler extends Handler {
	ResumeService resumeService;

	public CommandModeHandler(String[] args) throws IOException {
		super(args);
		resumeService = new ResumeService();
	}

	public void intialize() throws IOException, ParseException,
			FileDirectoryEmptyException, IndexDirectoryEmptyException,
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
		} catch (FileDirectoryEmptyException fde) {
			System.out
					.println("The Resume Directory is not set \n Please set using the command 'resumedir <path>'");
			System.exit(1);
		} catch (IndexDirectoryEmptyException ide) {
			System.out
					.println("The Index Directory is not set. Please set it using the command 'indexdir <path>'");
			System.exit(1);
		} catch (IndexNotFoundException ide) {
			System.out
					.println("The files are not yet indexed");
			System.exit(1);
		}
	}

	private void update() throws IOException, ParseException,
			FileDirectoryEmptyException, IndexDirectoryEmptyException {
		int numOfupdates = 0;
		numOfupdates = resumeService.updateIndex();
		System.out.println("Resume Index Updated successfully");
		System.out.println("Number of new files added to the index are:"
				+ numOfupdates);
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
			org.apache.lucene.queryparser.classic.ParseException,
			FileDirectoryEmptyException, IndexDirectoryEmptyException {
		if (this.args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		SearchResult searchResult = null;
		searchResult = resumeService.search(this.args[1]);
		System.out.println("Total Hits:" + searchResult.getTotalHitCount());
		System.out.println("Search Duration:"
				+ searchResult.getSearchDuration() + "ms");
		System.out.println("\n****TOP HITS***");
		for (String Hit : searchResult.getTopHits()) {
			System.out.println(Hit);
		}
	}

}
