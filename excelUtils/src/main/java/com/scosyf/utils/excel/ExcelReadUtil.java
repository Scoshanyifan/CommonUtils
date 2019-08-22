package com.scosyf.utils.excel;

import com.google.common.collect.Lists;
import com.scosyf.utils.excel.common.ExcelConst;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * 读取excel
 *
 * @author kunbu
 * @time 2019/8/22 14:42
 **/
public class ExcelReadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReadUtil.class);

    /**
     * 大数据读取,不能使用usermodel模式,需要使用事件驱动SAX
     * https://my.oschina.net/OutOfMemory/blog/1068972
     * http://poi.apache.org/components/spreadsheet/how-to.html#xssf_sax_api
     *
     * @param
     * @author kunbu
     * @time 2019/8/22 11:14
     **/
    public static void readExcelbigData() {
        // TODO
    }

    /**
     * 简单读取excel(单sheet)
     *
     * @param data MultiPartFile中的data
     * @param originalFileName 文件名
     */
    public static List<List<String>> readExcelSimple(byte[] data, String originalFileName) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            return readExcelSimple(in, originalFileName);
        } catch (Exception e) {
            LOGGER.error(">>> 读取excel失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 简单读取excel(单sheet)
     * @param filePath    文件路径
     */
    public static List<List<String>> readExcelSimple(String filePath) {
        try {
            InputStream in;
            if (filePath.startsWith(ExcelConst.FILE_TYPE_HTTP)) {
                in = new URL(filePath).openStream();
            } else {
                File file = new File(filePath);
                if (!file.exists()) {
                    LOGGER.error("readExcelSimple, 文件不存在, filePath:{}", filePath);
                }
                in = new FileInputStream(file);
            }
            return readExcelSimple(in, filePath);
        } catch (Exception e) {
            LOGGER.error(">>> 读取excel失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 简单读取excel(单sheet)
     *
     * @param in    输入流
     * @param originalFileName      文件原始名
     * @author kunbu
     * @time 2019/8/22 14:41
     **/
    public static List<List<String>> readExcelSimple(InputStream in, String originalFileName) {
        return readExcelSimpleWithHeader(in, originalFileName, null);
    }

    /**
     * 简单读取excel(单sheet)
     *
     * @param in
     * @param originalFileName
     * @author kunbu
     * @time 2019/8/22 14:24
     **/
    public static List<List<String>> readExcelSimpleWithHeader(InputStream in, String originalFileName, List<String> headers) {
        try {
            Workbook workbook;
            if (originalFileName.endsWith(ExcelConst.EXCEL_XLS_2003)) {
                workbook = new HSSFWorkbook(in);
            } else if (originalFileName.endsWith(ExcelConst.EXCEL_XLSX_2007)) {
                workbook = new XSSFWorkbook(in);
            } else {
                LOGGER.error("readExcelSimple, excel文件格式错误, originalFileName:{}", originalFileName);
                throw new RuntimeException("workbook null");
            }
            return getExcelWithOneSheet(workbook, headers);
        } catch (Exception e) {
            LOGGER.error(">>> 读取excel失败", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取只有一个sheet的excel
     * @param wb            工作簿
     * @param headers       首行
     * @return              row:cell
     */
    private static List<List<String>> getExcelWithOneSheet(Workbook wb, List<String> headers) {
        int sheetCount = wb.getNumberOfSheets();
        if (sheetCount == 0) {
            return null;
        } else if (sheetCount == 1) {
            if (CollectionUtils.isEmpty(headers)) {
                return getSheet(wb, 1, -1, 0);
            } else {
                return getSheet(wb, 2, headers.size(), 0);
            }
        } else {
            List<List<String>> headerArr = null;
            if (CollectionUtils.isNotEmpty(headers)) {
                headerArr = Lists.newArrayList();
                headerArr.add(headers);
            }
            List<List<List<String>>> sheets = getExcelWithSheets(wb, headerArr);
            if (CollectionUtils.isNotEmpty(sheets)) {
                return sheets.get(0);
            } else {
                return null;
            }
        }
    }

    /**
     * 读取包含多个sheet的excel(首行其实位置按照header判断,若有header则从2开始算)
     *
     * @param wb            工作簿
     * @param headerArr     头行key,必须和sheet一一对应
     * @return              sheet:row:cell
     */
    private static List<List<List<String>>> getExcelWithSheets(Workbook wb, List<List<String>> headerArr) {
        int sheetCount = wb.getNumberOfSheets();
        if (sheetCount <= 0) {
            return null;
        } else {
            List<List<List<String>>> sheets = Lists.newArrayList();
            if (CollectionUtils.isEmpty(headerArr)) {
                for (int i = 0; i < sheetCount; i++) {
                    //首行从1算起
                    List<List<String>> sheet = getSheet(wb, 1, -1, i);
                    sheets.add(sheet);
                }
            } else {
                for (int i = 0; i < headerArr.size(); i++) {
                    //因为有header,首行从2算起
                    List<List<String>> sheet = getSheet(wb, 2, headerArr.get(i).size(), i);
                    sheets.add(sheet);
                }
            }
            return sheets;
        }
    }

    /**
     * 读取sheet TODO 用二维数组代替List
     *
     * @param wb           工作簿
     * @param dataRow 	   数据起始行（1 表示第一行）
     * @param headerSize   首行size（-1 表示无）
     * @param sheetIdx     表格下标
     * @return             row:cell
     */
    private static List<List<String>> getSheet(Workbook wb, int dataRow, int headerSize, int sheetIdx) {
        List<List<String>> rowArr = Lists.newArrayList();
        Sheet sheet = wb.getSheetAt(sheetIdx);
        //行row
        int rftn = sheet.getFirstRowNum();
        int rltn = sheet.getLastRowNum();
        // 遍历
        for (int j = rftn + dataRow - 1; j <= rltn; j++) {
            List<String> colArr = Lists.newArrayList();
            Row row = sheet.getRow(j);
            if (row == null) {
                continue;
            }
            //列cell
            int cftn = row.getFirstCellNum();
            int cltn = row.getLastCellNum();
            if (headerSize > 0) {
                cltn = headerSize;
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
     * @param cell 单元格
     * @return value的字符串形式
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
