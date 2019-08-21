package com.scosyf.utils.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * https://www.cnblogs.com/liyafei/p/8146136.html
 *
 */
public class ExcelTest {
    
    public static final String BASE_PATH = "C:/Users/mojun/Desktop/";
    
    private static List<String> headers = Lists.newArrayList("ID", "NAME", "ADDRESS", "DESCRIPTION");
    private static List<String> keys = Lists.newArrayList("id", "name", "addr", "desc");
    private static List<Bean> datas = Lists.newArrayList(
    		new Bean(1, "2333", "No.1 Street", null),
    		new Bean(2, "2dsafs3", "No.1 S第五期treet", null),
    		new Bean(3, "ds", "No.1 Street", "ssssss"),
    		new Bean(11, "23v从的撒旦撒33", null, "dsw11"),
    		new Bean(12, "2332223", "No.1 Street", null));
    
    public static void main(String[] args) {
//        writeExcel();
        readExcel();
//    	exportExcel();
    }
    
    private static void exportExcel() {
    	List<Map<String, Object>> dataList = Lists.newArrayList();
    	for (Bean bean : datas) {
    		Map<String, Object> dataMap = Maps.newHashMap();
    		dataMap.put("id", bean.getId());
        	dataMap.put("name", bean.getName());
        	dataMap.put("addr", bean.getAddr());
        	dataMap.put("desc", bean.getDesc());
        	dataList.add(dataMap);
		}
    	try {
    		OutputStream os = new FileOutputStream(new File(BASE_PATH + "kunbu.xlsx"));
    		ExcelUtil.exportExcelSimple2007(headers, keys, dataList, os);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
    }
    
    private static void readExcel() {
        try {
//            Workbook wk = ExcelUtilsPoi.readExcel(BASE_PATH + "demo.xlsx");
//            List<List<String>> datas = ExcelUtilsPoi.getExcelWithSingleSheet(wk, null);
            
            List<List<String>> datas = ExcelUtil.readExcelSimple(BASE_PATH + "kunbu.xlsx");
            
            for (List<String> list : datas) {
                System.out.println(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static class Bean {
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
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddr() {
			return addr;
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
    }
}
