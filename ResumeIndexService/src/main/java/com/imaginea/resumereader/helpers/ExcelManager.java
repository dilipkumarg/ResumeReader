package com.imaginea.resumereader.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {
	private final InputStream fileIS;
	private final String filePath;

	public ExcelManager(String inputFile) throws FileNotFoundException {

		this.fileIS = new FileInputStream(inputFile);
		this.filePath = inputFile;

	}

	/*
	 * public static void main(String[] args) throws IOException { ExcelManager
	 * excelManager = new ExcelManager( "/home/ashwin/Desktop/Book1.xlsx");
	 * List<String> index = new ArrayList<String>(); index.add("11450");
	 * excelManager.delete(index); }
	 */

	/*
	 * public ExcelManager() { this.fileIS =
	 * ExcelManager.class.getClassLoader().getResourceAsStream( ("Book1.xlsx"));
	 * filePath = ExcelManager.class.getClassLoader().getResource("Book1.xlsx");
	 * }
	 */

	/*
	 * public List<String> read() throws IOException { // Create an ArrayList to
	 * store the data read from excel sheet. List<String> data = new
	 * ArrayList<String>(); try { // // Create an excel workbook from the file
	 * system. // XSSFWorkbook workbook = new XSSFWorkbook(this.fileIS); // //
	 * Get the first sheet on the workbook. // XSSFSheet sheet =
	 * workbook.getSheetAt(0); // // When we have a sheet object in hand we can
	 * iterate on // each sheet's rows and on each row's cells. We store the //
	 * data read in an ArrayList Iterator<Row> rows = sheet.rowIterator(); while
	 * (rows.hasNext()) { XSSFRow row = (XSSFRow) rows.next(); Iterator<Cell>
	 * cells = row.cellIterator();
	 * 
	 * while (cells.hasNext()) { XSSFCell cell = (XSSFCell) cells.next();
	 * data.add(cell.toString()); break; } } } catch (IOException e) {
	 * e.printStackTrace(); } finally { if (this.fileIS != null) {
	 * this.fileIS.close(); } } return data; }
	 */

	public void write(String employeeName, int employeeId) throws IOException {
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
		data.put(Integer.toString(++i),
				new Object[] { employeeName, employeeId });
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

	public void delete(List<String> index) throws IOException {
		Map<Integer, String> data = read(index);
		new File(this.filePath).delete();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Sheet1");
		// Iterate over data and write to sheet
		int cellnum, rownum = 0;
		Cell cell;
		for (Map.Entry<Integer, String> entry : data.entrySet()) {
			Row row = ((XSSFSheet) sheet).createRow(rownum++);
			cellnum = 0;
			cell = row.createCell(cellnum++);
			cell.setCellValue(entry.getValue());
			cell = row.createCell(cellnum++);
			cell.setCellValue(entry.getKey());
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

	public Map<Integer, String> read(List<String> index) throws IOException {
		String employeeName;
		int employeeId;
		Cell cell;
		// Create an ArrayList to store the data read from excel sheet.
		Map<Integer, String> data = new TreeMap<Integer, String>();
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
				employeeName = cells.next().getStringCellValue();
				if ("".equals(employeeName.trim()))
					break;
				cell = cells.next();
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					employeeId = Integer.parseInt(cell.toString());
				} else {
					employeeId = (int) cell.getNumericCellValue();
				}
				if (index != null && index.contains(employeeId + "")) {
					continue;
				} else {
					data.put(employeeId, employeeName);
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
}
