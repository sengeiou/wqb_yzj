package com.wqb.common;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadExcal {
    /**
     * @param filePath 文件路径
     * @param fileName 文件名
     * @param startrow 开始行
     * @param startcol 开始列
     * @return list
     * @throws Exception
     */
    public static List<Map<String, Object>> readExcel(String filePath, String fileName, int startrow, int startcol)
            throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        File file = new File(filePath, fileName);
        FileInputStream fs = new FileInputStream(file);
        Workbook wk = null;
        FormulaEvaluator evaluator = null;
        try {
            wk = new XSSFWorkbook(fs);
            evaluator = wk.getCreationHelper().createFormulaEvaluator();
        } catch (Exception ex) {
            wk = new HSSFWorkbook(new FileInputStream(file));
            evaluator = wk.getCreationHelper().createFormulaEvaluator();
        }
        // Workbook wk = WorkbookFactory.create(fs);
        // XSSFWorkbook wk=new XSSFWorkbook(fs);
        Sheet sheet = wk.getSheetAt(0);
        int rowNum = sheet.getLastRowNum() + 1;

        for (int i = startrow; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            int cellNum = row.getLastCellNum();
            if (cellNum < 3) {
                continue;
            }

            Map<String, Object> map = new LinkedHashMap<String, Object>();

            for (int j = 0; j < cellNum; j++) {

                Cell cell = row.getCell(j);
                String cellvalue = null;
                if (cell != null) {
                    switch (cell.getCellType()) {
                        case 0:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                Date dt = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());//获取成DATE类型
                                cellvalue = dateformat.format(dt);
                                break;
                            } else {
                                //cellvalue = cell.getNumericCellValue()+"";
                                DecimalFormat df = new DecimalFormat("0.##");
                                cellvalue = df.format(cell.getNumericCellValue()) + "";
                                break;
                            }
                        case 1:
                            cellvalue = cell.getStringCellValue();
                            break;
                        case 2:
                            //处理公式的值
                            CellValue evaluate = null;
                            try {
                                double numericCellValue = cell.getNumericCellValue();
                                System.out.println(numericCellValue);
                                DecimalFormat df = new DecimalFormat("0.##");
                                cellvalue = df.format(numericCellValue) + "";
                            } catch (Exception e) {
                                e.printStackTrace();

                                String cellFormula = cell.getCellFormula();
                                evaluate = evaluator.evaluate(cell);
                                //CellValue evaluate = evaluator.evaluate(cell);
                                int cellType = evaluate.getCellType();
                                if (cellType == 0) {
                                    try {
                                        DecimalFormat df = new DecimalFormat("0.##");
                                        cellvalue = df.format(cell.getNumericCellValue()) + "";
                                    } catch (Exception a) {
                                        a.printStackTrace();
                                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
                                        double numberValue = evaluate.getNumberValue();
                                        Date dt = HSSFDateUtil.getJavaDate(numberValue);//获取成DATE类型
                                        cellvalue = dateformat.format(dt);
                                    }
                                    if (cellvalue == null) {
                                        cellvalue = String.valueOf(evaluate);
                                    }
                                } else if (cellType == 1) {
                                    cellvalue = evaluate.getStringValue();
                                } else if (cellType == 3) {
                                    cellvalue = "";
                                } else if (cellType == 4) {
                                    cellvalue = String.valueOf(evaluate.getBooleanValue());
                                } else if (cellType == 5) {
                                    cellvalue = String.valueOf(evaluate.getErrorValue());
                                } else {
                                    cellvalue = "";
                                }
                            }
                            break;
                        case 3:
                            cellvalue = "";
                            break;
                        case 4:
                            cellvalue = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case 5:
                            cellvalue = String.valueOf(cell.getErrorCellValue());
                            break;
                    }
                } else {
                    cellvalue = "";
                }
                map.put("map" + j, cellvalue);
            }
            list.add(map);
        }

        return list;
    }

}
