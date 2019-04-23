package com.wqb.common;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExcelReader {
    private POIFSFileSystem fs; // 解析Ex格式
    private HSSFWorkbook wb; // 得到文档对象
    private HSSFSheet sheet; // 得到一个表单
    private HSSFRow row; // 获得Ex行数

    /**
     * 读取Excel表格表头的内容
     *
     * @param InputStream
     * @return String 表头内容的数组
     * @edit by lisc 2017年5月3日18:58:10 修改判定title逻辑
     */
    public String[] readExcelTitle(InputStream is) {
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);

            sheet = wb.getSheetAt(0);
            boolean flag = false;
            String[] title = new String[1];
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
                }
                int colNum = row.getPhysicalNumberOfCells();
                if (colNum != 0) {
                    int colNum1 = row.getPhysicalNumberOfCells();
                    for (int j = 0; j < colNum1; j++) {
                        String val = getCellFormatValue(row.getCell((short) j));
                        if (!val.trim().equals("")) {
                            title[0] = val;
                            flag = true;
                            break;
                        }
                    }
                }
                if (flag) {
                    break;
                }
            }
            return title;
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"招商银行"};
        }

    }

    private String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        // 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();

                        // 方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        cellvalue = sdf.format(date);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    /**
     * 读取Excel数据内容
     *
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public String readExcelContent(InputStream is, int i, int j) {
        Map<Integer, String> content = new HashMap<Integer, String>();
        String str = "";
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        row = sheet.getRow(i);
        str = getCellFormatValue(row.getCell((short) j)).trim();
        return str;
    }
}
