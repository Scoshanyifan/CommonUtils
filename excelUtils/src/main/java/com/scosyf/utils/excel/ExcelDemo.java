package com.scosyf.utils.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;

import lombok.Data;

/**
 * https://www.cnblogs.com/liyafei/p/8146136.html
 *
 */
public class ExcelDemo {
    
    public static final String BASE_PATH = "C:/Users/Think/Desktop/";
    
    public static void main(String[] args) {
//        writeExcel();
        readExcel();
    }
    
    private static void writeExcel() {
        List<String> headers = Lists.newArrayList("ID", "NAME", "ADDRESS", "DESCRIPTION");
        List<Bean> datas = Lists.newArrayList();
        datas.add(new Bean(1, "2333", "No.1 Street", null));
        datas.add(new Bean(2, "2dsafs3", "No.1 S第五期treet", null));
        datas.add(new Bean(3, "ds", "No.1 Street", "ssssss"));
        datas.add(new Bean(11, "23v从的撒旦撒33", null, "dsw11"));
        datas.add(new Bean(12, "2332223", "No.1 Street", null));
        
        try {
            //生成xlsx （2007-2010）
            Workbook book = new XSSFWorkbook();// 2003-xls >>> HSSFWorkbook()
            //工作簿
            Sheet sheet = book.createSheet("测试Sheet");
            //这是默认width
            sheet.setDefaultColumnWidth(20);
            //设置样式
            CellStyle style = book.createCellStyle();
            style.setAlignment(CellStyle.ALIGN_LEFT);
            //设置头行
            ExcelUtilsPoi.setHeader(sheet, headers, null);
            //填充数据
            ExcelUtilsPoi.setDatas(sheet, datas, Bean.class, 1, style);
            //输出excel
            ExcelUtilsPoi.writeExcel(book, BASE_PATH + "test." + ExcelUtilsPoi.EXCEL_XLSX);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void readExcel() {
        try {
            Workbook wk = ExcelUtilsPoi.readExcel(BASE_PATH + "demo.xlsx");
            List<List<String>> datas = ExcelUtilsPoi.getExcelWithSingleSheet(wk, null);
            for (List<String> list : datas) {
                System.out.println(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
//    public static void fillData(List<Bean> datas, Sheet sheet, CellStyle style, int rowNum) {
//        int size = datas.size();
//        sheet.setDefaultColumnWidth(20);
//        for (int i = 0; i < size; i++) {
//            Bean data = datas.get(i);
//            Row dataRow = sheet.createRow(rowNum++);
//            //TODO 如果优化，用RTTI 动态读取bean的字段并填充
//            if (style != null) {
//                Cell cell0 = dataRow.createCell(0);
//                cell0.setCellValue(data.getId());
//                cell0.setCellStyle(style);
//                Cell cell1 = dataRow.createCell(1);
//                cell1.setCellValue(data.getName());
//                cell0.setCellStyle(style);
//                Cell cell2 = dataRow.createCell(2);
//                cell2.setCellValue(data.getAddr());
//                cell2.setCellStyle(style);
//                Cell cell3 = dataRow.createCell(3);
//                cell3.setCellValue(data.getDesc());
//                cell3.setCellStyle(style);
//            } else {
//                dataRow.createCell(0).setCellValue(data.getId());
//                dataRow.createCell(1).setCellValue(data.getName());
//                dataRow.createCell(2).setCellValue(data.getAddr());
//                dataRow.createCell(3).setCellValue(data.getDesc());
//            }
//        }
//    }
    
    @Data
    static class Bean {

        private Integer id;
        private String name;
        private String addr;
        private String desc;
        
        public Bean(Integer id, String name, String addr, String desc) {
            this.id = id;
            this.name = name;
            this.addr = addr;
            this.desc = desc;
        }
        
    }
}
