package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;

import org.apache.lucene.index.IndexNotFoundException;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.lucene.SearchResult;
import com.imaginea.resumereader.services.ResumeSearchService;
import com.imaginea.resumereader.services.ResumeIndexService;
import com.imaginea.resumereader.servlet.JettyServer;

public class CommandModeHandler extends Handler {
	private PropertyFileReader properties;

	public CommandModeHandler(String[] args) throws IOException {
		super(args);
		properties = new PropertyFileReader();
	}

	public void intialize() throws Exception {
		String command = this.args[0];
		JettyServer jServer = new JettyServer();
		try {
			if ("update".equalsIgnoreCase(command)) {
				this.update();
			} else if ("indexdir".equalsIgnoreCase(command)) {
				this.setIndexDirPath();
			} else if ("resumedir".equalsIgnoreCase(command)) {
				this.setResumeDirPath();
			} else if ("search".equalsIgnoreCase(command)) {
				this.search();
			} else if ("start".equalsIgnoreCase(command)) {
				jServer.start();
			} else if ("stop".equalsIgnoreCase(command)) {
				jServer.stop();
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
			System.out.println("The files are not yet indexed");
			System.exit(1);
		}
	}

	private void update() throws IOException, ParseException,
			FileDirectoryEmptyException, IndexDirectoryEmptyException {
		int numOfupdates = 0;
		ResumeIndexService resumeIndexService = new ResumeIndexService();
		numOfupdates = resumeIndexService.updateIndex(properties.getIndexDirPath(), properties.getResumeDirPath(), properties.getLastTimeStamp());
		properties.setLastTimeStamp(System.currentTimeMillis());
		System.out.println("Resume Index Updated successfully");
		System.out.println("Number of new files added to the index are:"
				+ numOfupdates);
	}

	private void setIndexDirPath() throws IOException {
		if (this.args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		properties.setIndexDirPath(this.args[1]);
	}

	private void setResumeDirPath() throws IOException {
		if (this.args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		properties.setResumeDirPath(this.args[1]);
	}

	private void search() throws IOException,
			org.apache.lucene.queryparser.classic.ParseException,
			FileDirectoryEmptyException, IndexDirectoryEmptyException {
		if (this.args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		ResumeSearchService resumeSearchService = new ResumeSearchService();
		SearchResult searchResult = null;
		searchResult = resumeSearchService.search(this.args[1], properties.getIndexDirPath());
		System.out.println("Total Hits:" + searchResult.getTotalHitCount());
		System.out.println("Search Duration:"
				+ searchResult.getSearchDuration() + "ms");
		System.out.println("\n****TOP HITS***");
		for (String Hit : searchResult.getTopHits()) {
			System.out.println(Hit);
		}
	}

}
