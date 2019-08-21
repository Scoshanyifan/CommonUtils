package com.scosyf.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

import java.io.FileOutputStream;

/**
 * @program: commonUtils
 * @description: 大数据处理
 * @author: kunbu
 * @create: 2019-08-20 16:14
 **/
public class ExcelBigDataUtil {

    public static final String SXSSF_PATH = "D:\\kunbu\\commonUtils\\excelUtils/temp/sxssf.xlsx";

    public static void main(String[] args) throws Throwable {
        // keep 100 rows in memory, exceeding rows will be flushed to disk
//        Workbook wb = new SXSSFWorkbook(100);

        Workbook wb = new XSSFWorkbook();
        Sheet sh = wb.createSheet();
        for(int rownum = 0; rownum < 1000; rownum++){
            Row row = sh.createRow(rownum);
            for(int cellnum = 0; cellnum < 20; cellnum++){
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }

        }

        // Rows with rownum < 900 are flushed and not accessible
        for(int rownum = 0; rownum < 900; rownum++){
            Assert.assertNull(sh.getRow(rownum));
        }

        // ther last 100 rows are still in memory
        for(int rownum = 900; rownum < 1000; rownum++){
            Assert.assertNotNull(sh.getRow(rownum));
        }

        FileOutputStream out = new FileOutputStream(SXSSF_PATH);
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
//        wb.dispose();
    }

}
