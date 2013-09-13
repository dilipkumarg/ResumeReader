package com.imaginea.resumereader.helpers;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.imaginea.resumereader.entities.FileInfo;

public class ResumeSegregatorTest {

	@Test()
	public void findMaxSimilarity() throws FileNotFoundException, IOException {
		ResumeSegregator resumeSegregator = new ResumeSegregator();
		List<FileInfo> personNames = new ArrayList<FileInfo>();
		// ExcelReader excelReader = new ExcelReader(new
		// PropertyFileReader().getEmployeeExcelPath());
		personNames.add(new FileInfo(null, "Apurba Nath", null, null));
		personNames.add(new FileInfo(null, "Apurba nath", null, null));
		Map<Integer, String> employeeNames = new TreeMap<Integer, String>();
		employeeNames.put(11425, "Apurba Nath");
		resumeSegregator.compareWithEmployeeList(personNames, employeeNames);
		assertTrue("as", 2 == resumeSegregator.getActiveEmployees().size());
		assertTrue("as", 0 == resumeSegregator.getProbableActiveEmployess()
				.size());
		assertTrue("as", 0 == resumeSegregator.getInactiveEmployees().size());
		resumeSegregator.getActiveEmployees();
	}

}
