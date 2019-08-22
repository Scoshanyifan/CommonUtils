package com.scosyf.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 基于 Apache poi 的excel工具类
 *
 * @author scosyf
 * @time 2019年5月9日
 *
 * 2019.8.22 新增
 *
 */
public class ExcelExportUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportUtil.class);

	/**
	 * 大数据导出,不能使用普通的导出,会OOM
	 * https://blog.csdn.net/daiyutage/article/details/53010491
	 * http://poi.apache.org/spreadsheet/how-to.html#sxssf
	 *
	 * @param headers
	 * @param keys
	 * @param datas
	 * @param out
	 * @author kunbu
	 * @time 2019/8/22 11:08
	 * @return
	 **/
	public static boolean exportExcelSimpleBigData(List<String> headers, List<String> keys, List<Map<String, Object>> datas, OutputStream out) {
		try {
			// keep 100 rows in memory, exceeding rows will be flushed to disk
			SXSSFWorkbook workbook = new SXSSFWorkbook(100);
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
				// dispose of temporary files backing this workbook on disk
				workbook.dispose();
				return true;
			} catch (IOException e) {
				LOGGER.error(">>> exportExcelSimpleBigData, 资源处理异常", e);
				return false;
			}
		} catch (Exception e) {
			LOGGER.error(">>> exportExcelSimpleBigData, 导出失败", e);
			return false;
		}

	}

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
     * 简单导出excel(只有一个sheet)
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
    			LOGGER.error(">>> exportExcelSimple, 资源处理异常", e);
    			return false;
    		}
		} catch (Exception e) {
			LOGGER.error(">>> exportExcelSimple, 导出失败", e);
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


}
