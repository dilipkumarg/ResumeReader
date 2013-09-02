package com.imaginea.resumereader.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {
	private final InputStream fileIS;
	private final String filePath;

	public ExcelManager(String inputFile) throws FileNotFoundException {

		this.fileIS = new FileInputStream(inputFile);
		filePath = inputFile;

	}
/*public static void main(String[] args) throws IOException {
	PropertyFileReader prop = new PropertyFileReader();
	ExcelManager excelManager = new ExcelManager("/home/ashwin/Documents/Book1.xlsx");
	excelManager.write("ashwin", 1234);
}*/
	/*public ExcelManager() {
		this.fileIS = ExcelManager.class.getClassLoader().getResourceAsStream(
				("Book1.xlsx"));
		filePath = ExcelManager.class.getClassLoader().getResource("Book1.xlsx");
	}*/

	public List<String> read() throws IOException {
		// Create an ArrayList to store the data read from excel sheet.
		List<String> data = new ArrayList<String>();
		try {
			//
			// Create an excel workbook from the file system.
			//
			XSSFWorkbook workbook = new XSSFWorkbook(this.fileIS);
			//
			// Get the first sheet on the workbook.
			//
			XSSFSheet sheet = workbook.getSheetAt(0);
			//
			// When we have a sheet object in hand we can iterate on
			// each sheet's rows and on each row's cells. We store the
			// data read in an ArrayList
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				XSSFRow row = (XSSFRow) rows.next();
				Iterator<Cell> cells = row.cellIterator();

				while (cells.hasNext()) {
					XSSFCell cell = (XSSFCell) cells.next();
					data.add(cell.toString());
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (this.fileIS != null) {
				this.fileIS.close();
			}
		}
		return data;
	}

	public void write(String employeeName, int employeeId)
			throws IOException {
		// Get the Excel workbook
		XSSFWorkbook workbook = new XSSFWorkbook(this.fileIS);

		// Get the employee sheet
		// XSSFSheet sheet = workbook.createSheet("Employee Data");
		XSSFSheet sheet = workbook.getSheetAt(0);

		int lastColumnIndex = sheet.getLastRowNum();
		int i = lastColumnIndex;
		// This data needs to be written (Object[])
		/*
		 * ExcelReader excelReader = new ExcelReader(); Map<String, Object[]>
		 * data = excelReader.read();
		 */
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put(Integer.toString(++i), new Object[] { employeeName,
				employeeId });
		/*
		 * data.put(Integer.toString(++i), new Object[] {"Lokesh", 11425 });
		 * data.put(Integer.toString(++i), new Object[] {"John", 11426 });
		 * data.put(Integer.toString(++i), new Object[] {"Brian", 11427 });
		 */

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = lastColumnIndex + 1;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(this.filePath);
			workbook.write(out);
			out.close();
			System.out.println("Book1.xlsx written successfully on disk.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
