/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.evotek.qlns.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.zk.ui.Sessions;

import com.evotek.qlns.util.key.Values;

/**
 *
 * @author linhlh2
 */
public class ExcelUtil<T> {

    private static String FOLDER_TEMPLATE = "/report/";
    private static String EXPORT_SINGLE_SHEET = "export_single_sheet";
    private static String IMPORT_TEMPLATE = "import_template";
    private static int TITLE_ROW = 0;
    private static int HEADER_ROW = 3;
    private static int DATA_ROW_START = 4;
    private static int COL_START = 0;
    private DataFormat df;
    private XSSFWorkbook wb;
    private Map<Integer, String[]> convertMap;

    public ExcelUtil() {
    }

    public ExcelUtil(Map<Integer, String[]> convertMap) {
        this.convertMap = convertMap;
    }

    public void toSingleSheetXlsx(String fileName, String title,
            List<Object[]> headerInfors, List<String> properties, List<T> datas)
            throws Exception {
        try {
//            String tmpDir = StaticUtil.getTmpDir();
            String tmpDir = System.getProperty("java.io.tmpdir");

            ServletContext svc = getServletContext();

            StringBuilder fileInput = new StringBuilder();
            StringBuilder fileOutput = new StringBuilder();

            fileInput.append(EXPORT_SINGLE_SHEET);
            fileInput.append(StringPool.XLSX);

            fileOutput.append(fileName);
            fileOutput.append(StringPool.UNDERLINE);
            fileOutput.append(TimeUtil.getShortTimestamp());
            fileOutput.append(StringPool.UNDERLINE);
            fileOutput.append(1);
            fileOutput.append(StringPool.XLSX);

            String pathInput = svc.getRealPath(FOLDER_TEMPLATE
                    + fileInput.toString());

//            String pathOut = StaticUtil.getTmpDir() + StringPool.SLASH
//                    + fileOutput.toString();

            String pathOut = tmpDir + StringPool.SLASH +
                    FileUtil.getAutoIncrementName(tmpDir, fileOutput.toString());
            //transform data
            this.read(pathInput);

            //transform data
            this.transformerXlsx(Values.FIRST_INDEX, title, headerInfors,
                    properties, datas);
            //write file
            File fileOut = new File(pathOut);

            OutputStream outputStream = new FileOutputStream(fileOut);

            wb.write(outputStream);

            outputStream.close();
            //download
            FileUtil.download(fileOut);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void createImportTemplate(String fileName, List<Object[]> headerInfors)
            throws Exception {
        try {
//            String tmpDir = StaticUtil.getTmpDir();
            String tmpDir = System.getProperty("java.io.tmpdir");

            ServletContext svc = getServletContext();

            StringBuilder fileInput = new StringBuilder();
            StringBuilder fileOutput = new StringBuilder();

            fileInput.append(IMPORT_TEMPLATE);
            fileInput.append(StringPool.XLSX);

            fileOutput.append(fileName);
            fileOutput.append(StringPool.UNDERLINE);
            fileOutput.append(TimeUtil.getShortTimestamp());
            fileOutput.append(StringPool.UNDERLINE);
            fileOutput.append(1);
            fileOutput.append(StringPool.XLSX);

            String pathInput = svc.getRealPath(FOLDER_TEMPLATE
                    + fileInput.toString());

//            String pathOut = StaticUtil.getTmpDir() + StringPool.SLASH
//                    + fileOutput.toString();

            String pathOut = tmpDir + StringPool.SLASH +
                    FileUtil.getAutoIncrementName(tmpDir, fileOutput.toString());
            //transform data
            this.read(pathInput);

            //transform data
            this.transformerXlsx(Values.FIRST_INDEX, headerInfors);
            //write file
            File fileOut = new File(pathOut);

            OutputStream outputStream = new FileOutputStream(fileOut);

            wb.write(outputStream);

            outputStream.close();
            //download
            FileUtil.download(fileOut);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void transformerXlsx(int sheetPosition, String title,
            List<Object[]> headerInfors, List<String> properties,
            List<T> datas) throws Exception {
        try {
            Sheet sheet = wb.getSheetAt(sheetPosition);

            wb.getNumberOfSheets();
            
            int endCol = headerInfors.size() - 1;

            //get cell style
            CellStyle titleCellStyle = getCellStyle(sheet, TITLE_ROW,
                    COL_START);
            CellStyle headerCellStyle = getCellStyle(sheet, HEADER_ROW,
                    COL_START);
            CellStyle dataCellStyle = getCellStyle(sheet, DATA_ROW_START,
                    COL_START);

            //title
            createTitle(sheet, title, titleCellStyle, TITLE_ROW, TITLE_ROW,
                    COL_START, endCol);

            //header
            createHeader(sheet, headerCellStyle, headerInfors, HEADER_ROW);

            //data
            createData(sheet, dataCellStyle, properties, datas, DATA_ROW_START);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public void transformerXlsx(int sheetPosition, List<Object[]> headerInfors)
            throws Exception {
        try {
            Sheet sheet = wb.getSheetAt(sheetPosition);
            
            CellStyle headerCellStyle = getCellStyle(sheet, TITLE_ROW,
                    COL_START);

            //header
            createHeader(sheet, headerCellStyle, headerInfors, TITLE_ROW);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }
    }

    public XSSFWorkbook read(String filePath) throws FileNotFoundException,
            Exception {
        InputStream inp = null;

        try {
            inp = new FileInputStream(filePath);

            wb = new XSSFWorkbook(inp);

            df = wb.createDataFormat();

        } catch (FileNotFoundException fnfe) {
            _log.error(fnfe.getMessage(), fnfe);
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        } finally {
            try {
                inp.close();
            } catch (IOException ex) {
                _log.error(ex.getMessage(), ex);
            }
        }

        return wb;
    }

    public Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);

        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        return row;
    }

    public Cell getOrCreateCell(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);

        if (cell == null) {
            cell = row.createCell(cellIndex);
        }

        return cell;
    }

    public Cell getOrCreateCell(Row row, int cellIndex, CellStyle cellStyle) {
        Cell cell = getOrCreateCell(row, cellIndex);

        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }

        return cell;
    }

    public Cell getOrCreateCell(Row row, int cellIndex, CellStyle cellStyle,
            String value) {
        Cell cell = getOrCreateCell(row, cellIndex, cellStyle);

        cell.setCellValue(value);

        return cell;
    }

    public Cell getOrCreateCell(Row row, int cellIndex, CellStyle cellStyle,
            Object value) {
        Cell cell = getOrCreateCell(row, cellIndex, cellStyle);

        if (value != null) {
            if (value instanceof Date) {
                cell.setCellValue(DateUtil.getDate((Date) value,
                        DateUtil.LONG_DATE_PATTERN));
            } else {
                cell.setCellValue(value.toString());
            }
        }

        return cell;
    }

    public Row shiftCellToLeft(Row row, int startCol, int endCol) {
        for (int i = startCol + 1; i < endCol; i++) {
            Cell sourceCell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
            Cell desCell = row.getCell(i - 1, Row.CREATE_NULL_AS_BLANK);

            desCell.setCellStyle(sourceCell.getCellStyle());
            desCell.setCellValue(sourceCell.getStringCellValue());

            row.removeCell(sourceCell);
        }

        return row;
    }

    public List<CellStyle> getCellStyles(Row rowRef, int startCol,
            int endCol) {
        List<CellStyle> styles = new ArrayList<CellStyle>();

        for (int i = startCol; i <= endCol; i++) {
            Cell cell = rowRef.getCell(i);

            styles.add(cell.getCellStyle());
        }

        return styles;
    }

    public CellStyle getCellStyle(Row rowRef, int cellIndex) {
        Cell cell = rowRef.getCell(cellIndex);

        return cell.getCellStyle();
    }

    public CellStyle getCellStyle(Sheet sheet, int rowIndex, int cellIndex) {
        Row row = getOrCreateRow(sheet, rowIndex);

        return getCellStyle(row, cellIndex);
    }

    public Sheet createTitle(Sheet sheet, String title, CellStyle cellStyle,
            int firstRow, int lastRow, int firstCol, int lastCol) {

        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol,
                lastCol));

        Row row = getOrCreateRow(sheet, firstRow);

        getOrCreateCell(row, firstCol, cellStyle, title);

        return sheet;
    }

    public Sheet createHeader(Sheet sheet, CellStyle cellStyle,
            List<Object[]> headerInfors, int headRowIndex) {
        Row row = getOrCreateRow(sheet, headRowIndex);

        int size = headerInfors.size();

        //STT
//        getOrCreateCell(row, 0, cellStyle, STT);

        for (int i = 0; i < size; i++) {
            Object[] infor = headerInfors.get(i);

            String label = (String) infor[0];
            Integer width = (Integer) infor[1];

            getOrCreateCell(row, i, cellStyle, label);

            sheet.setColumnWidth(i, width.intValue());
        }

        return sheet;
    }

    public Sheet createData(Sheet sheet, CellStyle cellStyle,
            List<String> properties, List<T> datas, int dataRowIndex)
            throws Exception {
        try {
            if (datas.isEmpty()) {
                return sheet;
            }

            Row row = null;

            int count = dataRowIndex;

            CellStyle[] cellStyles = getDataCellStyle(properties, datas.get(0),
                    cellStyle);

            for (int i = 0; i < datas.size(); i++) {
                T data = datas.get(i);

                row = getOrCreateRow(sheet, count);

                //STT
                getOrCreateCell(row, 0, cellStyle, i + 1);

                Object value = null;

                for (int j = 0; j < properties.size(); j++) {
                    if(data instanceof Map){
                        value = ((Map) data).get(properties.get(j));
                    }else{
                        value = PropertyUtils.getProperty(
                            data, properties.get(j));
                    }

                    if (Validator.isNotNull(convertMap)
                            && Validator.isNotNull(convertMap.get(j))) {
                        value = getStringValue((Long) value, convertMap.get(j));
                    }

                    getOrCreateCell(row, j + 1,
                            cellStyles[j], value);
                }

                count++;
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return sheet;
    }

    public CellStyle[] getDataCellStyle(List<String> properties,
            T data, CellStyle baseStyle) throws Exception {
        CellStyle[] cellStyles = new CellStyle[properties.size()];

        try {
            for (int i = 0; i < properties.size(); i++) {
                Object value = PropertyUtils.getProperty(
                        data, properties.get(i));

                cellStyles[i] = wb.createCellStyle();

                cellStyles[i].cloneStyleFrom(baseStyle);

                if (Validator.isNotNull(convertMap)
                        && Validator.isNotNull(convertMap.get(i))) {
                    cellStyles[i].setAlignment(CellStyle.ALIGN_LEFT);
                } else {
                    cellStyles[i] = setTextAlign(cellStyles[i], value);
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getMessage(), ex);
        }

        return cellStyles;
    }

    public CellStyle setTextAlign(CellStyle cellStyle, Object data) {
        if (data instanceof Long
                || data instanceof Integer
                || data instanceof Double
                || data instanceof Short) {
            cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        } else if (data instanceof String) {
            cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        } else if (data instanceof Date) {
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setDataFormat(df.getFormat(DateUtil.SHORT_DATE_PATTERN));
        } else {
            cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        }

        return cellStyle;
    }

    private String getStringValue(Long index, String[] values) {
        String statusName = StringPool.BLANK;

        if (Validator.isNotNull(index)
                && Validator.isNotNull(values)
                && index < values.length) {
            statusName = values[index.intValue()];
        }

        return statusName;
    }

    private static ServletContext getServletContext() {
        return Sessions.getCurrent().getWebApp().getServletContext();
    }
    
    private static final Logger _log =
            LogManager.getLogger(ExcelUtil.class);
}
