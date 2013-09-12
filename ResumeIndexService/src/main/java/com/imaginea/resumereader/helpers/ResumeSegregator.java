package com.imaginea.resumereader.helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.imaginea.resumereader.entities.FileInfo;

public class ResumeSegregator {
	private List<FileInfo> activeEmployees, probableActiveEmployess,
			inactiveEmployees;

	public ResumeSegregator() {
		this.activeEmployees = new ArrayList<FileInfo>();
		this.probableActiveEmployess = new ArrayList<FileInfo>();
		this.inactiveEmployees = new ArrayList<FileInfo>();

	}

	public void compareWithEmployeeList(List<FileInfo> personNames,
			Map<Integer, String> map) throws IOException,
			FileNotFoundException {
		if (personNames != null) {
			String employee;
			for (FileInfo person : personNames) {
				double similarity = 0.0, jaro;
				String closeMatch = "";
				for (Map.Entry<Integer, String> entry : map.entrySet()) {
					employee = (entry.getValue()).toLowerCase();
					jaro = PersonNameMatcher.similarity(person.getTitle()
							.toLowerCase(), employee);
					if (jaro > similarity) {
						similarity = jaro;
						closeMatch = employee;
					}
				}
				segregate(similarity, person, closeMatch);
			}
		}
	}

	public List<FileInfo> removeDuplicates(List<FileInfo> employeeList) {
		// storing unique entries in the result List
		List<FileInfo> resultList = new ArrayList<FileInfo>();
		for (FileInfo emp1 : employeeList) {
			double similarity = 0.0, jaro = 0.0;
			for (FileInfo emp2 : resultList) {
				jaro = PersonNameMatcher.similarity(emp1.getTitle(),
						emp2.getTitle());
				if (jaro == 1.0) {
					similarity = jaro;
					break;
				} else if (jaro > similarity) {
					similarity = jaro;
				}
			}
			if (similarity >= 0.92) {
				continue;
			} else {
				resultList.add(emp1);
			}
		}
		return resultList;
	}

	private void segregate(double similarity, FileInfo employee,
			String closeMatch) {
		// attaching match details
		DecimalFormat df = new DecimalFormat("0.00");
		employee.setCloseMatch(closeMatch + " (" + df.format(similarity * 100)
				+ "%)");
		// 0.95 would confirm the right match barring few character
		// displacements/removals eg : dhurva for dhruva
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
