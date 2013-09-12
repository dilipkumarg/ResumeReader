package com.imaginea.resumereader.helpers;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

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
			Map<String, Object[]> map) throws IOException {
		// calling worker class
		ForkJoinComapareWithEmployee process = new ForkJoinComapareWithEmployee(
				personNames, map);
		ForkJoinPool pool = new ForkJoinPool();
		pool.invoke(process);
	}

	/**
	 * This class uses fork-join framework for doing parallel processing. And
	 * this mainly used for decreasing response time
	 * 
	 * @author dilip
	 * 
	 */
	class ForkJoinComapareWithEmployee extends RecursiveAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		static final int THRESHOLD = 1;
		List<FileInfo> empList;
		Map<String, Object[]> empMap;

		ForkJoinComapareWithEmployee(List<FileInfo> personNames,
				Map<String, Object[]> map) {
			this.empList = personNames;
			this.empMap = map;
		}

		@Override
		protected void compute() {
			if (empList.size() <= THRESHOLD) {
				computeDirect();
			} else {
				// dividing the list into two parts
				int center = empList.size() / 2;
				List<FileInfo> lPart = splitList(empList, 0, center);
				List<FileInfo> rPart = splitList(empList, center,
						empList.size());
				invokeAll(new ForkJoinComapareWithEmployee(lPart, empMap),
						new ForkJoinComapareWithEmployee(rPart, empMap));
			}
		}

		protected void computeDirect() {
			if (empList != null) {
				Set<String> keyset = empMap.keySet();
				String employee;
				for (FileInfo person : empList) {
					double similarity = 0.0, jaro;
					String closeMatch = "";
					for (String key : keyset) {
						employee = ((String) empMap.get(key)[0]).toLowerCase();
						jaro = PersonNameMatcher.similarity(person.getTitle()
								.toLowerCase(), employee);
						if (jaro > similarity) {
							similarity = jaro;
							closeMatch = employee;
						}
						if (jaro == 1.0) {
							// if exact match found breaking remaining loop
							break;
						}
					}
					segregate(similarity, person, closeMatch);
				}
			}
		}

		// it is generic function to split a list
		protected <T> List<T> splitList(List<T> list, int start, int end) {
			List<T> part = new ArrayList<T>();
			for (int i = start; i < end; i++) {
				part.add(list.get(i));
			}
			return part;
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

	// it will put the employee in appropriate list based on similarity
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
