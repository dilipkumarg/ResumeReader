package com.imaginea.resumereader.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {
	private final InputStream fileIS;

	public ExcelReader(String inputFile) throws FileNotFoundException {
		this.fileIS = new FileInputStream(inputFile);
	}

	public ExcelReader(FileInputStream fileIS) {
		this.fileIS = fileIS;
	}

	public ExcelReader() {
		/*
		 * StringWriter writer = new StringWriter();
		 * IOUtils.copy(this.getClass()
		 * .getClassLoader().getResourceAsStream("Book1.xlsx"), writer);
		 */
		this.fileIS = ExcelReader.class.getClassLoader().getResourceAsStream(
				("Book1.xlsx"));
	}

	/*
	 * public static void main(String[] args) throws Exception { ExcelReader
	 * test = new ExcelReader();
	 * test.setInputFile("/home/ashwin/Downloads/Book1.xlsx"); test.read(); }
	 */

	public void read(List<String> data) throws IOException {
		//
		// Create an ArrayList to store the data read from excel sheet.
		//
		try {
			//
			// Create a FileInputStream that will be use to read the
			// excel file.
			//

			//
			// Create an excel workbook from the file system.
			//
			XSSFWorkbook workbook = new XSSFWorkbook(this.fileIS);
			//
			// Get the first sheet on the workbook.
			//
			XSSFSheet sheet = workbook.getSheetAt(0);

			//
			// When we have a sheet object in hand we can iterator on
			// each sheet's rows and on each row's cells. We store the
			// data read on an ArrayList so that we can printed the
			// content of the excel to the console.
			//
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

	}
}
