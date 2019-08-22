package com.scosyf.utils.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.scosyf.utils.excel.common.Entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * https://www.cnblogs.com/liyafei/p/8146136.html
 *
 * 测试简单的excel读取,即用户模式user model
 * 测试大数据情况下的问题,OOM,GC
 */
public class ExcelTest {
    
    public static final String BASE_PATH = "C:/Users/mojun/Desktop/";
    
    private static List<String> headers = Lists.newArrayList("ID", "姓名", "地址", "描述");
    private static List<String> keys = Lists.newArrayList("id", "name", "address", "desc");
    private static List<Entity> dataList = Lists.newArrayList();

    static {
        Random r = new Random();
        for (int i = 1; i < 10000; i++) {
            Entity e = new Entity(i, Integer.toString(i * i), "No." + i, Integer.toString(r.nextInt(1000)));
            dataList.add(e);
        }
    }

    public static void main(String[] args) {

//        exportExcel();
        readExcel();
    }
    
    private static void exportExcel() {
    	List<Map<String, Object>> mapList = Lists.newArrayList();
    	for (Entity e : dataList) {
    		Map<String, Object> dataMap = Maps.newHashMap();
    		dataMap.put("id", e.getId());
        	dataMap.put("name", e.getName());
        	dataMap.put("address", e.getAddress());
        	dataMap.put("desc", e.getDesc());
            mapList.add(dataMap);
		}
    	try {
    		OutputStream os = new FileOutputStream(new File(BASE_PATH + "kunbu.xlsx"));

    		// 大数据下报错: Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
//    		ExcelExportUtil.exportExcelSimple2007(headers, keys, mapList, os);

    		ExcelExportUtil.exportExcelSimpleBigData(headers, keys, mapList, os);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
    }
    
    private static void readExcel() {
        try {
            // 数据量大的时候报错: lang.OutOfMemoryError: Java heap space
            List<List<String>> datas = ExcelReadUtil.readExcelSimple(BASE_PATH + "kunbu.xlsx");
            
            for (List<String> list : datas) {
                System.out.println(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
