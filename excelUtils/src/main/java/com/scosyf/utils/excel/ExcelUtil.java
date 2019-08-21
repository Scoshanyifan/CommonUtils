package com.scosyf.utils.excel;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 基于 Apache poi 的excel工具类
 * 
 * @author scosyf
 * @time 2019年5月9日
 */
public class ExcelUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
	
	public static final String EXCEL_XLS_2003 = "xls";
    public static final String EXCEL_XLSX_2007 = "xlsx";

    /**
     * 简单导出excel 2003
     * @param headers 标头行
     * @param keys 标头key
     * @param datas 数据：Map<标头key, 数据value>
     * @param out 输出流
     * @return
     */
    public static boolean exportExcelSimple2003(List<String> headers, List<String> keys, List<Map<String, Object>> datas, OutputStream out) {
    	Workbook workbook = new HSSFWorkbook();
    	return exportExcelSimple(workbook, headers, keys, datas, out);
    }
    
    /**
     * 简单导出excel 2007+
     * @param headers 标头行
     * @param keys 标头key
     * @param datas 数据：Map<标头key, 数据value>
     * @param out 输出流
     * @return
     */
    public static boolean exportExcelSimple2007(List<String> headers, List<String> keys, List<Map<String, Object>> datas, OutputStream out) {
    	Workbook workbook = new XSSFWorkbook();
    	return exportExcelSimple(workbook, headers, keys, datas, out);
    }
    
    /**
     * 简单导出excel
     * 
     * @param workbook 
     * @param headers
     * @param datas
     * @param out
     * @return
     */
    private static boolean exportExcelSimple(Workbook workbook, List<String> headers, List<String> keys, List<Map<String, Object>> datas, OutputStream out) {
    	try {
    		//单工作簿
            Sheet sheet = workbook.createSheet("sheet");
            //设置样式
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_LEFT);
            //设置默认width
            sheet.setDefaultColumnWidth(20);
            //设置头行
            setHeader(sheet, headers, style);
            //填充数据
            setData(sheet, 1, style, keys, datas);
        	try {
        		workbook.write(out);
    			out.flush();
    			out.close();
    			return true;
    		} catch (IOException e) {
    			LOGGER.error(">>> 资源处理异常", e);
    			return false;
    		}
		} catch (Exception e) {
			LOGGER.error(">>> excel导出失败", e);
			return false;
		}
    }
    
    /**
     * 设置头行
     * @param sheet
     * @param headers
     * @param style
     */
    private static void setHeader(Sheet sheet, List<String> headers, CellStyle style) {
        int headerSize = headers.size();
        Row header = sheet.createRow(0);
        for (int i = 0; i < headerSize; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(headers.get(i));
            if (style != null) {
            	headerCell.setCellStyle(style);
            }
        }
    }
    
    /**
     * 填充数据
     * @param sheet 
     * @param rowNum 数据起始行
     * @param style
     * @param keys
     * @param datas
     */
    private static void setData(Sheet sheet, int rowNum, CellStyle style, List<String> keys, List<Map<String, Object>> datas) {
    	int headerSize = keys.size();
        for (Map<String, Object> dataMap : datas) {
        	Row data = sheet.createRow(rowNum);
			for (int colume = 0; colume < headerSize; colume++) {
				Cell dataCell = data.createCell(colume);
				String key = keys.get(colume);
				setCellValue(dataCell, dataMap.get(key));
				if (style != null) {
					dataCell.setCellStyle(style);
	            }
			}
			rowNum++;
		}
    }
    
    /**
     * 设置value
     * @param obj
     * @return
     */
    private static void setCellValue(Cell cell, Object obj) {
		if (obj != null) {
			cell.setCellValue(obj.toString());
		} else {
			cell.setCellValue("-");
		}
	}

    /**
     * 简单读取excel
     * @param data MultiPartFile中的data
     * @param originalFileName
     * @return
     */
    public static List<List<String>> readExcelSimple(byte[] data, String originalFileName) {
    	try {
    		ByteArrayInputStream in = new ByteArrayInputStream(data);
    		return readExcelSimple(in, originalFileName);
    	} catch (Exception e) {
        	LOGGER.error(">>> 读取excel失败", e);
			throw new RuntimeException("读取excel失败");
        }
    }
    
    /**
     * 简单读取excel
     * @param filePath
     * @return
     */
    public static List<List<String>> readExcelSimple(String filePath) {
    	try {
    		InputStream in = null;
            if (filePath.startsWith("http")) {
            	in = new URL(filePath).openStream();
            } else {
                File file = new File(filePath);
                if (!file.exists()) {
                	LOGGER.error("excel文件不存在");
                }
                in = new FileInputStream(file);
            }
            return readExcelSimple(in, filePath);
        } catch (Exception e) {
        	LOGGER.error(">>> 读取excel失败", e);
			throw new RuntimeException("读取excel失败");
        }
    }
    
    /**
     * 简单读取excel
     * @param in 输入流
     * @param originalFileName
     * @return
     */
    public static List<List<String>> readExcelSimple(InputStream in, String originalFileName) {
    	try {
    		Workbook workbook = null;
    		if (originalFileName.endsWith(EXCEL_XLS_2003)) {
    			workbook = new HSSFWorkbook(in);
    		} else if (originalFileName.endsWith(EXCEL_XLSX_2007)) {
    			workbook = new XSSFWorkbook(in);
    		} else {
    			LOGGER.error("excel文件格式错误");
    		}
    		return getExcelWithOneSheet(workbook, null);
		} catch (Exception e) {
			LOGGER.error(">>> 读取excel失败", e);
			throw new RuntimeException("读取excel失败");
		}
    }
    
    /**
     * 读取只有一个sheet的excel
     * @param wb
     * @param headers       首行
     * @return
     */
    private static List<List<String>> getExcelWithOneSheet(Workbook wb, List<String> headers) {
        int sheetCount = wb.getNumberOfSheets();
        if (sheetCount == 0) {
            return null;
        } else if (sheetCount == 1) {
            if (CollectionUtils.isEmpty(headers)) {
                return getSheet(wb, 2, -1, 0);
            } else {
                return getSheet(wb, 2, headers.size(), 0);
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
    private static List<List<List<String>>> getExcelWithSheets(Workbook wb, List<List<String>> headerArr) {
        int sheetCount = wb.getNumberOfSheets();
        if (sheetCount <= 0) {
            return null;
        } else {
            List<List<List<String>>> sheets = Lists.newArrayList();
            if (CollectionUtils.isEmpty(headerArr)) {
                for (int i = 0; i < sheetCount; i++) {
                    List<List<String>> sheet = getSheet(wb, 2, -1, i);
                    sheets.add(sheet);
                }
            } else {
                for (int i = 0; i < headerArr.size(); i++) {
                    List<List<String>> sheet = getSheet(wb, 2, headerArr.get(i).size(), i);
                    sheets.add(sheet);
                }
            }
            return sheets;
        }
    }
    
    /**
     * 读取sheet
     * @param wb
     * @param dataRow 	      数据起始行（1 表示第一行）
     * @param headerSize   首行size（-1 表示无）
     * @param sheetIdx     表格下标
     * @return
     */
    private static List<List<String>> getSheet(Workbook wb, int dataRow, int headerSize, int sheetIdx) {
        List<List<String>> rowArr = Lists.newArrayList();
        Sheet sheet = wb.getSheetAt(sheetIdx);
        int rftn = sheet.getFirstRowNum();
        int rltn = sheet.getLastRowNum();
        //跳过标头行
        for (int j = rftn + dataRow - 1; j <= rltn; j++) {
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
     */
    private static String getValue(Cell cell) {
        if (cell == null) {
            return "";
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
