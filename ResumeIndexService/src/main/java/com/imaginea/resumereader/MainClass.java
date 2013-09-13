package com.imaginea.resumereader;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.index.IndexNotFoundException;

import com.imaginea.resumereader.base.ResumeIndexSearcher;
import com.imaginea.resumereader.entities.FileInfo;
import com.imaginea.resumereader.entities.SearchResult;
import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.helpers.ExcelManager;
import com.imaginea.resumereader.helpers.PropertyFileReader;
import com.imaginea.resumereader.helpers.ResumeSegregator;

public class MainClass {
	public static final Logger LOGGER = Logger
			.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private PropertyFileReader properties;
	public MainClass() throws IOException {
		properties = new PropertyFileReader();
	}

	public static void main(String[] args) throws ParseException, IOException,
			FileDirectoryEmptyException,
			org.apache.lucene.queryparser.classic.ParseException {
		MainClass mainClass = new MainClass();
		try {
			mainClass.intialize(args);
		} catch (IllegalArgumentException iae) {
			LOGGER.log(
					Level.SEVERE,
					"IllegalArgumentException occured,\n Error:"
							+ iae.getMessage());
			throw iae;
		} catch (org.apache.lucene.queryparser.classic.ParseException pe) {
			LOGGER.log(Level.SEVERE,
					"ParseException occured,\n Error:" + pe.getMessage());
			throw pe;
		}
	}

	public void intialize(String[] args) throws IOException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		String command = args[0];
		try {
			if ("index".equalsIgnoreCase(command)) {
				this.index();
			} else if ("cleanandindex".equalsIgnoreCase(command)) {
				this.cleanAndIndex();
			} else if ("resumedir".equalsIgnoreCase(command)) {
				this.setResumeDirPath(args);
			} else if ("employeefile".equalsIgnoreCase(command)) {
				this.setEmployeeFilePath(args);
			} else if ("search".equalsIgnoreCase(command)) {
				this.search(args);
			} else {
				throw new IllegalArgumentException("Command not found");
			}
		} catch (FileDirectoryEmptyException fde) {
			System.out
					.println("The Resume Directory is not set \n Please set using the command 'resumedir <path>'");
			System.exit(1);
		} catch (IndexNotFoundException ide) {
			System.out.println("The files are not yet indexed");
			System.exit(1);
		}
	}

	private void cleanAndIndex() throws FileDirectoryEmptyException,
			IOException, ParseException {
		int numOfupdates = 0;
		ResumeIndexSearcher resumeIndexService = new ResumeIndexSearcher();
		numOfupdates = resumeIndexService.CleanAndIndex();
		System.out.println("Resume Index Updated successfully");
		System.out.println("Number of files added to the index are:"
				+ numOfupdates);
	}

	private void index() throws IOException, ParseException,
			FileDirectoryEmptyException {
		int numOfupdates = 0;
		ResumeIndexSearcher resumeIndexService = new ResumeIndexSearcher();
		numOfupdates = resumeIndexService.updateIndex();
		properties.setLastTimeStamp(System.currentTimeMillis());
		System.out.println("Resume Index Updated successfully");
		System.out.println("Number of new files added to the index are:"
				+ numOfupdates);
	}

	private void setResumeDirPath(String[] args) throws IOException {
		if (args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		properties.setResumeDirPath(args[1]);
	}

	private void setEmployeeFilePath(String[] args) throws IOException {
		if (args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		properties.setEmployeeExcelPath(args[1]);
	}

	private void search(String[] args) throws IOException,
			org.apache.lucene.queryparser.classic.ParseException,
			FileDirectoryEmptyException {
		if (args.length < 2) {
			throw new IllegalArgumentException(
					"Need one more parameter to perform this operation");
		}
		ResumeIndexSearcher resumeSearchService = new ResumeIndexSearcher();
		SearchResult searchResult = null;
		ExcelManager excelReader = new ExcelManager(
				new PropertyFileReader().getEmployeeExcelPath());
		searchResult = resumeSearchService.search(args[1]);
		ResumeSegregator resumeSegregator = new ResumeSegregator();
		resumeSegregator.compareWithEmployeeList(searchResult.getTopHits(),
				excelReader.read(null));
		System.out.println("Total Hits:" + searchResult.getTotalHitCount());
		System.out.println("Search Duration:"
				+ searchResult.getSearchDuration() + "ms");
		System.out.println("\n****TOP ACTIVE HITS***");
		for (FileInfo activeHit : resumeSegregator.getActiveEmployees()) {
			System.out.println(activeHit.toString());
		}
		System.out.println("\n****TOP PROBABLE ACTIVE HITS***");
		for (FileInfo activeHit : resumeSegregator.getProbableActiveEmployess()) {
			System.out.println(activeHit.toString());
		}
		System.out.println("\n****TOP INACTIVE HITS***");
		for (FileInfo inactiveHit : resumeSegregator.getInactiveEmployees()) {
			System.out.println(inactiveHit.toString());
		}
	}
}
