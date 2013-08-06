package com.imaginea.resumereader.helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.imaginea.resumereader.entities.FileInfo;

public class ResumeSegregator {
	private List<FileInfo> activeEmployees, probableActiveEmployess,
			inactiveEmployees;

	public ResumeSegregator() {
		this.activeEmployees = new ArrayList<FileInfo>();
		this.probableActiveEmployess = new ArrayList<FileInfo>();
		this.inactiveEmployees = new ArrayList<FileInfo>();

	}

	public void findMaxSimilarity(List<FileInfo> personNames) throws IOException {
		PersonNameMatcher nameMatcher = new PersonNameMatcher();
		String a;
		FileInfo employee;
		List<String> employeeNames = new ArrayList<String>();
		ExcelReader excelReader = new ExcelReader();
		excelReader.read(employeeNames);
		Iterator<FileInfo> personIterator = personNames.iterator();
		while (personIterator.hasNext()) {
			Iterator<String> employeeIterator = employeeNames.iterator();
			double similarity = 0.0, jaro;
			employee = personIterator.next();
			a = employee.getTitle();
			while (employeeIterator.hasNext()) {
				String string = employeeIterator.next();
				jaro = nameMatcher.compare(a, string);
				if (jaro > similarity) {
					similarity = jaro;
				}
			}
			segregate(similarity, employee);
		}
	}

	private void segregate(double similarity, FileInfo employee){
		if (similarity >= 0.95) {
			activeEmployees.add(employee);
		} else if (similarity >= 0.85 && similarity < 0.95) {
			probableActiveEmployess.add(employee);
		} else {
			inactiveEmployees.add(employee);
		}
		
	}
	public List<FileInfo> getActiveEmployees() {
		return activeEmployees;
	}

	public List<FileInfo> getProbableActiveEmployess() {
		return probableActiveEmployess;
	}

	public List<FileInfo> getInactiveEmployees() {
		return inactiveEmployees;
	}
}
