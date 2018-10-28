package com.scosyf.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;
import com.scosyf.utils.net.URLUtils;

/**
 * poi工具类
 * 
 * https://blog.csdn.net/qq547276542/article/details/75175289
 *
 */
public class ExcelUtilsPoi {
    
    public static final int HSSF = 1;
    public static final String EXCEL_XLS = "xls";
    
    public static final int XSSF = 2;
    public static final String EXCEL_XLSX = "xlsx";
    
    /**
     * 设置首行
     * @param sheet
     * @param headers
     * @param style
     */
    public static void setHeader(Sheet sheet, List<String> headers, CellStyle style) {
        Row header = sheet.createRow(0);
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers.get(i));
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }
    
    /**
     * 填充数据
     * @param sheet     表格
     * @param datas     数据，必须是DTO模型，且其中字段表示列，严格按照Bean规范
     * @param target    DTO模型的Class类
     * @param rowNum    起始行
     * @param style     样式，可为null
     * @throws Exception
     * create by kunbu
     */
    public static void setDatas(Sheet sheet, List<?> datas, Class<?> target, int rowNum, CellStyle style) throws Exception{
        List<Method> targetMethods = Lists.newArrayList();
        for (Method method : target.getDeclaredMethods()) {
            if (method.getName().startsWith("get")) {
                targetMethods.add(method);
            }
        }
        int num = targetMethods.size();
        for (Object object : datas) {
            Row dataRow = sheet.createRow(rowNum++);
            for (int i = 0; i < num; i++) {
                Cell cell = dataRow.createCell(i);
                Object o = targetMethods.get(i).invoke(object);
                if (o != null) {
                    cell.setCellValue(o.toString());
                }
            }
        }
    }
    
    /**
     * 输出excel
     * @param book
     * @param filePath
     * @throws Exception
     */
    public static void writeExcel(Workbook book, String filePath) throws Exception {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(filePath));
            book.write(os);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
    
    /**
     * file转换excel的workbook
     * @param file
     * @return
     * @throws Exception
     */
    public static Workbook readExcel(String filePath) throws Exception {
        Workbook book = null;
        InputStream is = null;
        try {
            if (filePath.startsWith("http")) {
                is = URLUtils.getInputStreamFromUrl(filePath);
            } else {
                File file = new File(filePath);
                if (!file.exists()) {
                    throw new RuntimeException("excel file not exists");
                }
                is = new FileInputStream(file);
            }
            
            if (filePath.endsWith(EXCEL_XLS)) {
                book = new HSSFWorkbook(is);
            } else if (filePath.endsWith(EXCEL_XLSX)) {
                book = new XSSFWorkbook(is);
            } else {
                throw new RuntimeException("no matched excel supported");
            }
            return book;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
    
    /**
     * 读取只有一个sheet的excel
     * @param wb
     * @param headers       首行
     * @return
     */
    public static List<List<String>> getExcelWithSingleSheet(Workbook wb, List<String> headers) {
        int sheetCount = wb.getNumberOfSheets();
        if (sheetCount == 0) {
            return null;
        } else if (sheetCount == 1) {
            if (headers != null && !headers.isEmpty()) {
                return getSheet(wb, -1, 0);
            } else {
                return getSheet(wb, headers.size(), 0);
            }
        } else { 
            List<List<String>> headerArr = Lists.newArrayList();
            headerArr.add(headers);
            return getExcelWithSheets(wb, headerArr).get(0);
        }
    }
    
    /**
     * 读取包含多个sheet的excel
     * @param wb
     * @param headerArr     首行集合         
     * @return
     */
    public static List<List<List<String>>> getExcelWithSheets(Workbook wb, List<List<String>> headerArr) {
        int sheetCount = wb.getNumberOfSheets();
        if (sheetCount <= 0) {
            return null;
        } else {
            List<List<List<String>>> sheets = Lists.newArrayList();
            if (headerArr != null && !headerArr.isEmpty()) {
                for (int i = 0; i < sheetCount; i++) {
                    List<List<String>> sheet = getSheet(wb, -1, i);
                    sheets.add(sheet);
                }
            } else {
                for (int i = 0; i < headerArr.size(); i++) {
                    List<List<String>> sheet = getSheet(wb, headerArr.get(i).size(), i);
                    sheets.add(sheet);
                }
            }
            return sheets;
        }
    }
    
    /**
     * 读取sheet
     * @param wb
     * @param headerSize   首行size（-1 表示无）
     * @param sheetIdx     表格下标
     * @return
     */
    private static List<List<String>> getSheet(Workbook wb, int headerSize, int sheetIdx) {
        List<List<String>> rowArr = Lists.newArrayList();
        Sheet sheet = wb.getSheetAt(sheetIdx);
        int rftn = sheet.getFirstRowNum();
        int rltn = sheet.getLastRowNum();
        //跳过首行
        for (int j = rftn + 1; j <= rltn; j++) {
            List<String> colArr = Lists.newArrayList();
            Row row = sheet.getRow(j);
            if (row == null) 
                continue;
            int cftn = 0;
            int cltn = 0;
            if (headerSize > 0) {
                cltn = headerSize;
            } else {
                cftn = row.getFirstCellNum(); 
                cltn = row.getLastCellNum();
            }
            for (int k = cftn; k < cltn; k++) {
                Cell cell = row.getCell(k);
                //默认内容为字符串
                colArr.add(getValue(cell));
            }
            rowArr.add(colArr);
        }
        return rowArr;
    }
    
    /**
     * 获取每格的值
     * @param cell
     * @return
     * create by kunbu
     */
    private static String getValue(Cell cell) {
        if (cell == null) {
            return "----";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return Boolean.toString(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return Double.toString(cell.getNumericCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else {
            return "";
        }
    }
    
}
