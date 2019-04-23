package com.wqb.common;

import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectExcelRead {

    /**
     * @param filepath //文件路径
     * @param filename //文件名
     * @param startrow //开始行号
     * @param startcol //开始列号
     * @param sheetnum //sheet
     * @return list
     */
    public static List<Object> readExcel(String filepath, String filename,
                                         int startrow, int startcol, int sheetnum) {
        List<Object> varList = new ArrayList<Object>();
        // DecimalFormat df1 = new DecimalFormat("0");
        try {
            File target = new File(filepath, filename);
            FileInputStream fi = new FileInputStream(target);
            HSSFWorkbook wb = new HSSFWorkbook(fi);
            HSSFSheet sheet = wb.getSheetAt(sheetnum); // sheet 从0开始
//			int rowNum = sheet.getLastRowNum() + 1; // 取得最后一行的行号
            int rowNum = sheet.getLastRowNum() + 1; // 取得最后一行的行号 去除之前的+1 不然导入银行单会多一行

            for (int i = startrow; i < rowNum; i++) { // 行循环开始

                Map varpd = new HashMap();
                // System.out.println("+++++++++++++++++++++"+sheet.getRow(i));
                HSSFRow row = null;
                if (sheet.getRow(i) != null) // 不做判断会报空指针异常
                {
                    row = sheet.getRow(i); // 行
                } else {
                    continue;
                }
                int cellNum = row.getLastCellNum(); // 每行的最后一个单元格位置

                for (int j = startcol; j < cellNum; j++) { // 列循环开始
                    HSSFCell cell = null;
                    cell = row.getCell(Short.parseShort(j + ""));
                    String cellValue = null;
                    if (null != cell) {
                        switch (cell.getCellType()) { // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                            case 0:
                                if (HSSFDateUtil.isCellDateFormatted(cell)) { // 判断是日期类型
                                    SimpleDateFormat dateformat = new SimpleDateFormat(
                                            "yyyy-MM-dd");
                                    Date dt = HSSFDateUtil.getJavaDate(cell
                                            .getNumericCellValue());// 获取成DATE类型
                                    cellValue = dateformat.format(dt);
                                } else {
                                    cellValue = cell.getNumericCellValue() + "";
                                }
                                break;
                            case 1:
                                cellValue = cell.getStringCellValue();
                                break;
                            case 2:
                                cellValue = cell.getNumericCellValue() + "";
                                // cellValue =
                                // String.valueOf(cell.getDateCellValue());
                                break;
                            case 3:
                                cellValue = "";
                                break;
                            case 4:
                                cellValue = String.valueOf(cell
                                        .getBooleanCellValue());
                                break;
                            case 5:
                                cellValue = String
                                        .valueOf(cell.getErrorCellValue());
                                break;
                        }
                    } else {
                        cellValue = "";
                    }

                    varpd.put("var" + j, cellValue);

                }
                varList.add(varpd);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return varList;
    }

    /**
     * 读取excel销项发票
     *
     * @param filepath //文件路径
     * @param filename //文件名
     * @param startrow //开始行号
     * @param startcol //开始列号
     * @param sheetnum //sheet
     * @return list
     */
    public static List<Object> readExcelInvoice(String filepath,
                                                String filename, int startrow, int startcol, int sheetnum) {
        List<Object> varList = new ArrayList<Object>();

        try {
            File target = new File(filepath, filename);
            FileInputStream fi = new FileInputStream(target);
            HSSFWorkbook wb = new HSSFWorkbook(fi);
            HSSFSheet sheet = wb.getSheetAt(sheetnum); // sheet 从0开始
            // 取得最后一行的行号
            int rowNum = sheet.getLastRowNum() + 1;// 总行数
            // 判断销项、销项发票模版、自定义模版
            String type = null;
            Map maplist = new HashMap();
            // 遍历模版内容
            outer1:
            for (int i = 0; i < rowNum; i++) {
                HSSFRow hrow = sheet.getRow(i);// 第一行
                if (hrow == null) {
                    startrow = startrow + 1;
                    continue;
                }
                int cellNum = hrow.getLastCellNum();
                for (int j = 0; j < cellNum; j++) {
                    HSSFCell hcell = hrow.getCell(Short.parseShort(j + ""));// (0,0)
                    if (hcell == null) {
                        continue;
                    }
                    String hval = null;
                    if (hcell.getCellType() == 0) {

                        hval = hcell.getNumericCellValue() + "";// 取值
                    } else {
                        hval = hcell.getStringCellValue();
                    }
                    if (hval == null || "".equals(hval)) {
                        continue;
                    }
                    if (hval.startsWith("纳税人识别号")) {
                        HSSFCell hcella = hrow.getCell(Short.parseShort((j + 1)
                                + ""));
                        String hvala = hcella.getStringCellValue();// 取值
                        maplist.put("taxno", hcella);
                    }
                    if ("销方名称".equals(hval)) {
                        type = "进项发票";
                        maplist.put("type", type);
                        startrow = i + 1;
                        break outer1;
                    }
                    if ("购方企业名称".equals(hval)) {
                        type = "销项发票";
                        maplist.put("type", type);
                        startrow = i + 1;
                        break outer1;
                    }
                    if ("发票导入".equals(hval)) {
                        type = "发票导入";
                        maplist.put("type", "default");
                        startrow = i;
                        break outer1;
                    }
                }
            }
            // HSSFRow hrow=sheet.getRow(0);//第一行
            // int cellNum = hrow.getLastCellNum();
            // HSSFCell hcell = hrow.getCell(Short.parseShort(0 + ""));//(0,0)
            // String hval=hcell.getStringCellValue();//取值
            // 判断类型
            if (type.startsWith("销项")) {
                varList.add(maplist);
                readExcel2(varList, sheet, startrow, startcol, rowNum);
            } else if (type.startsWith("进项")) {
                varList.add(maplist);
                readExcel2(varList, sheet, startrow, startcol, rowNum);
            } else if (type.startsWith("发票导入")) {
                varList.add(maplist);// 自定义
                readExcel2(varList, sheet, 2, startcol, rowNum);
            } else {

            }
            // varList.add("");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return varList;
    }

    private static void readExcel2(List<Object> varList, HSSFSheet sheet,
                                   int startrow, int startcol, int rowNum) {
        // List<Object> varList = new ArrayList<Object>();
        DecimalFormat df1 = new DecimalFormat("0");
        Map var = (Map) varList.get(0);
        String type = var.get("type").toString();
        for (int i = startrow; i < rowNum; i++) { // 行循环开始

            Map varpd = new HashMap();
            HSSFRow row = sheet.getRow(i); // 行
            if (row == null) {
                continue;
            }
            int cellNum = row.getLastCellNum(); // 每行的最后一个单元格位置

            for (int j = startcol; j < cellNum; j++) { // 列循环开始

                HSSFCell cell = row.getCell(Short.parseShort(j + ""));
                String cellValue = null;
                if (null != cell) {
                    switch (cell.getCellType()) { // 判断excel单元格内容的格式，并对其进行转换，以便插入数据库
                        case 0:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) { // 判断是日期类型
                                SimpleDateFormat dateformat = new SimpleDateFormat(
                                        "yyyy-MM-dd");
                                Date dt = HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue());// 获取成DATE类型
                                cellValue = dateformat.format(dt);
                            } else {
                                cellValue = df1.format(cell.getNumericCellValue());
                            }
                            // add by lisc
                            if (type.equals("销项发票") && j == 6) {
                                SimpleDateFormat dateformat = new SimpleDateFormat(
                                        "yyyy-MM-dd");
                                Date dt = HSSFDateUtil.getJavaDate(cell
                                        .getNumericCellValue());// 获取成DATE类型
                                cellValue = dateformat.format(dt);
                            }
                            if (type.equals("销项发票") && j > 10) {
                                cellValue = cell.getNumericCellValue() + "";
                            }
                            break;
                        case 1:
                            cellValue = cell.getStringCellValue();
                            break;
                        case 2:
                            cellValue = cell.getNumericCellValue() + "";
                            // cellValue = String.valueOf(cell.getDateCellValue());
                            break;
                        case 3:
                            cellValue = "";
                            break;
                        case 4:
                            cellValue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case 5:
                            cellValue = String.valueOf(cell.getErrorCellValue());
                            break;
                    }
                } else {
                    cellValue = "";
                }

                varpd.put("var" + j, cellValue);

            }
            varList.add(varpd);
        }
        // return varList;
    }
}
