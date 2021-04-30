/**
 * 
 */
package com.evotek.qlns.excel;

/**
 * @author LinhLH
 *
 */
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExcelPOIHelper {

	public Map<Integer, List<SimpleCell>> readExcel(String fileLocation) throws IOException {

		Map<Integer, List<SimpleCell>> data = new HashMap<>();
		FileInputStream fis = new FileInputStream(new File(fileLocation));

		if (fileLocation.endsWith(".xls")) {
			data = readHSSFWorkbook(fis);
		} else if (fileLocation.endsWith(".xlsx")) {
			data = readXSSFWorkbook(fis);
		}

		int maxNrCols = data.values().stream().mapToInt(List::size).max().orElse(0);

		data.values().stream().filter(ls -> ls.size() < maxNrCols).forEach(ls -> {
			IntStream.range(ls.size(), maxNrCols).forEach(i -> ls.add(new SimpleCell("")));
		});

		return data;
	}

	private String readCellContent(Cell cell) {
		String content;
		
		switch (cell.getCellTypeEnum()) {
		
		case STRING:
			content = cell.getStringCellValue();
			
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				content = cell.getDateCellValue() + "";
			} else {
				content = cell.getNumericCellValue() + "";
			}
			
			break;
		case BOOLEAN:
			content = cell.getBooleanCellValue() + "";
			
			break;
		case FORMULA:
			content = cell.getCellFormula() + "";
			
			break;
		default:
			content = "";
		}
		
		return content;
	}

	private Map<Integer, List<SimpleCell>> readHSSFWorkbook(FileInputStream fis) throws IOException {
		Map<Integer, List<SimpleCell>> data = new HashMap<>();
		HSSFWorkbook workbook = null;
		try {
			workbook = new HSSFWorkbook(fis);

			HSSFSheet sheet = workbook.getSheetAt(0);
			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				HSSFRow row = sheet.getRow(i);
				
				data.put(i, new ArrayList<>());
				
				if (row != null) {
					for (int j = 0; j < row.getLastCellNum(); j++) {
						HSSFCell cell = row.getCell(j);
						
						if (cell != null) {
							HSSFCellStyle cellStyle = cell.getCellStyle();

							SimpleCell SimpleCell = new SimpleCell();

							HSSFColor bgColor = cellStyle.getFillForegroundColorColor();
							
							if (bgColor != null) {
								short[] rgbColor = bgColor.getTriplet();
								SimpleCell
										.setBgColor("rgb(" + rgbColor[0] + "," + rgbColor[1] + "," + rgbColor[2] + ")");
							}
							
							HSSFFont font = cell.getCellStyle().getFont(workbook);
							
							SimpleCell.setTextSize(font.getFontHeightInPoints() + "");
							
							if (font.getBold()) {
								SimpleCell.setTextWeight("bold");
							}
							
							HSSFColor textColor = font.getHSSFColor(workbook);
							
							if (textColor != null) {
								short[] rgbColor = textColor.getTriplet();
								SimpleCell.setTextColor(
										"rgb(" + rgbColor[0] + "," + rgbColor[1] + "," + rgbColor[2] + ")");
							}
							
							SimpleCell.setContent(readCellContent(cell));
							
							data.get(i).add(SimpleCell);
						} else {
							data.get(i).add(new SimpleCell(""));
						}
					}
				}
			}
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		
		return data;
	}

	private Map<Integer, List<SimpleCell>> readXSSFWorkbook(FileInputStream fis) throws IOException {
		XSSFWorkbook workbook = null;
		Map<Integer, List<SimpleCell>> data = new HashMap<>();
		try {

			workbook = new XSSFWorkbook(fis);
			
			XSSFSheet sheet = workbook.getSheetAt(0);

			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);
				
				data.put(i, new ArrayList<>());
				
				if (row != null) {
					for (int j = 0; j < row.getLastCellNum(); j++) {
						XSSFCell cell = row.getCell(j);
						
						if (cell != null) {
							XSSFCellStyle cellStyle = cell.getCellStyle();

							SimpleCell SimpleCell = new SimpleCell();
							
							XSSFColor bgColor = cellStyle.getFillForegroundColorColor();
							
							if (bgColor != null) {
								byte[] rgbColor = bgColor.getRGB();
								
								SimpleCell.setBgColor("rgb(" + (rgbColor[0] < 0 ? (rgbColor[0] + 0xff) : rgbColor[0])
										+ "," + (rgbColor[1] < 0 ? (rgbColor[1] + 0xff) : rgbColor[1]) + ","
										+ (rgbColor[2] < 0 ? (rgbColor[2] + 0xff) : rgbColor[2]) + ")");
							}
							XSSFFont font = cellStyle.getFont();
							
							SimpleCell.setTextSize(font.getFontHeightInPoints() + "");
							
							if (font.getBold()) {
								SimpleCell.setTextWeight("bold");
							}
							
							XSSFColor textColor = font.getXSSFColor();
							
							if (textColor != null) {
								byte[] rgbColor = textColor.getRGB();
								SimpleCell.setTextColor("rgb(" + (rgbColor[0] < 0 ? (rgbColor[0] + 0xff) : rgbColor[0])
										+ "," + (rgbColor[1] < 0 ? (rgbColor[1] + 0xff) : rgbColor[1]) + ","
										+ (rgbColor[2] < 0 ? (rgbColor[2] + 0xff) : rgbColor[2]) + ")");
							}
							
							SimpleCell.setContent(readCellContent(cell));
							
							data.get(i).add(SimpleCell);
						} else {
							data.get(i).add(new SimpleCell(""));
						}
					}
				}
			}
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		
		return data;
	}

}