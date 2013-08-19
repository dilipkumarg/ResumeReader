package com.imaginea.resumereader.helpers;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.imaginea.resumereader.entities.FileInfo;

public class ResumeSegregatorTest {

	@Test()
	public void findMaxSimilarity() throws FileNotFoundException, IOException {
		ResumeSegregator resumeSegregator = new ResumeSegregator();
		List<FileInfo> personNames = new ArrayList<FileInfo>();
		// ExcelReader excelReader = new ExcelReader(new
		// PropertyFileReader().getEmployeeExcelPath());
		personNames.add(new FileInfo(null, "Apurba Nath", null));
		personNames.add(new FileInfo(null, "Apurba nath", null));
		List<String> employeeNames = new ArrayList<String>();
		employeeNames.add("Apurba Nath");
		resumeSegregator.findMaxSimilarity(personNames, employeeNames);
		assertTrue("as", 2 == resumeSegregator.getActiveEmployees().size());
		assertTrue("as", 0 == resumeSegregator.getProbableActiveEmployess()
				.size());
		assertTrue("as", 0 == resumeSegregator.getInactiveEmployees().size());
		resumeSegregator.getActiveEmployees();
	}

}
