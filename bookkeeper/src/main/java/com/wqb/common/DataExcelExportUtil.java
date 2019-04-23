package com.wqb.common;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataExcelExportUtil {
    private Map<String, XSSFCellStyle> styles;
    protected int rowIndex = 0;

    public XSSFWorkbook exportExcelWorkbook(String sheetName, String[] columnHeaders, List<Object[]> contentList,
                                            boolean flag, Map<String, Object> querySubMessage) throws Exception {
        rowIndex = 0;
        XSSFSheet sheet = null;
        XSSFWorkbook wb = null;
        wb = new XSSFWorkbook();
        createStyles(wb);
        if (sheetName == null || sheetName.length() == 0) {
            sheetName = "Sheet第一页";
        }
        sheet = wb.createSheet(sheetName);
        sheet.createFreezePane(0, 1, 0, 1);
        if (flag == true) {
            generateHeader(sheet, columnHeaders, 0);
        }
        for (int i = 0; i < contentList.size(); i++) {
            Object[] subject = contentList.get(i);
            generateContent(sheet, columnHeaders, subject, i + 1);
        }
        generateContentLast(sheet, columnHeaders, querySubMessage, contentList.size() + 1);
        return wb;
    }

    public void generateContent(XSSFSheet sheet, String[] columnHeaders, Object[] subject, int rowIndex) {
        XSSFRow row = sheet.createRow(rowIndex++);
        Object[] objArr = subject;
        for (int coloum = 0; coloum < columnHeaders.length; coloum++) {
            XSSFCell cell = row.createCell(coloum);
            cell.setCellStyle(styles.get("fontfinally"));
            cell.setCellValue(objArr[coloum] != null ? objArr[coloum].toString() : "");
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);

        }
    }

    public void generateContentLast(XSSFSheet sheet, String[] columnHeaders, Map<String, Object> querySubMessage,
                                    int rowIndex) {
        if (querySubMessage == null) {
            return;
        }
        XSSFRow row = sheet.createRow(rowIndex++);
        Map<String, Object> subject = querySubMessage;
        for (int coloum = 0; coloum < columnHeaders.length; coloum++) {
            XSSFCell cell = row.createCell(coloum);
            if (coloum == 0) {
                cell.setCellValue(0);
            }
            if (coloum == 1 || coloum == 3) {
                cell.setCellValue("");
            }
            if (coloum == 2) {
                cell.setCellValue("合计");
            }
            if (coloum == 4) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("initDebitBalanceTotal").toString())));
            }
            if (coloum == 5) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("initCreditBalanceTotal").toString())));
            }
            if (coloum == 6) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("currentAmountDebitTotal").toString())));
            }
            if (coloum == 7) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("currentAmountCreditTotal").toString())));
            }
            if (coloum == 8) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("yearAmountDebitTotal").toString())));
            }
            if (coloum == 9) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("yearAmountCreditTotal").toString())));
            }
            if (coloum == 10) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("endingBalanceDebitTotal").toString())));
            }
            if (coloum == 11) {
                cell.setCellValue(DoubleUtils.getNumber(Double.parseDouble(subject.get("endingBalanceCreditTotal").toString())));
            }
        }
    }

    public Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb) {
        styles = new HashMap<String, XSSFCellStyle>();
        XSSFDataFormat df = wb.createDataFormat();

        XSSFFont normalFont = wb.createFont();
        normalFont.setFontHeightInPoints((short) 10);

        XSSFFont boldFont = wb.createFont();
        boldFont.setFontHeightInPoints((short) 10);
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        XSSFFont blueBoldFont = wb.createFont();
        blueBoldFont.setFontHeightInPoints((short) 10);
        blueBoldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        blueBoldFont.setColor(HSSFColor.BLUE.index);

        XSSFCellStyle headerStyle = wb.createCellStyle();
        headerStyle.setFont(boldFont);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styles.put("header", headerStyle);

        XSSFCellStyle dateCellStyle = wb.createCellStyle();
        dateCellStyle.setFont(normalFont);
        dateCellStyle.setDataFormat(df.getFormat("yyyy-MM-dd"));
        setBorder(dateCellStyle);
        styles.put("dateCell", dateCellStyle);

        XSSFCellStyle numberCellStyle = wb.createCellStyle();
        numberCellStyle.setFont(normalFont);
        numberCellStyle.setDataFormat(df.getFormat("#,##0.00000000"));
        setBorder(numberCellStyle);
        styles.put("numberCell", numberCellStyle);

        XSSFCellStyle numberCellStyle1 = wb.createCellStyle();
        numberCellStyle1.setFont(boldFont);
        numberCellStyle1.setDataFormat(df.getFormat("#,##0.00000000"));
        setBorder(numberCellStyle1);
        styles.put("numberCell1", numberCellStyle1);

        XSSFCellStyle numberCellStyle2 = wb.createCellStyle();
        numberCellStyle2.setFont(blueBoldFont);
        numberCellStyle2.setDataFormat(df.getFormat("#,##0.00000000"));
        setBorder(numberCellStyle2);
        styles.put("numberCell2", numberCellStyle2);

        XSSFCellStyle totalStyle = wb.createCellStyle();
        totalStyle.setFont(blueBoldFont);
        totalStyle.setWrapText(true);
        totalStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        setBorder(totalStyle);
        styles.put("total", totalStyle);

        XSSFFont titleFont = wb.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setFontHeightInPoints((short) 20);

        XSSFFont middleFont = wb.createFont();
        middleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        middleFont.setFontHeightInPoints((short) 12);

        XSSFCellStyle titleCellStyle = wb.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styles.put("titleCell", titleCellStyle);

        XSSFCellStyle middleCellStyle = wb.createCellStyle();
        middleCellStyle.setFont(middleFont);
        middleCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styles.put("middleCell", middleCellStyle);

        XSSFCellStyle leftCellStyle = wb.createCellStyle();
        leftCellStyle.setFont(middleFont);
        leftCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        styles.put("leftCell", leftCellStyle);

        XSSFCellStyle dateCellStyle1 = wb.createCellStyle();
        dateCellStyle1.setFont(middleFont);
        dateCellStyle1.setDataFormat(df.getFormat("yyyy-MM-dd"));
        dateCellStyle1.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        setBorder(dateCellStyle);
        styles.put("dateCell1", dateCellStyle1);

        XSSFCellStyle contentStyle = wb.createCellStyle();
        contentStyle.setFont(normalFont);
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // contentStyle.setWrapText(true);
        styles.put("contentCell", contentStyle);

        XSSFCellStyle numberCellStyle3 = wb.createCellStyle();
        numberCellStyle3.setFont(middleFont);
        numberCellStyle3.setDataFormat(df.getFormat("#,##0.00000000"));
        setBorder(numberCellStyle3);
        styles.put("numberCell3", numberCellStyle3);

        XSSFCellStyle numberCellStyle4 = wb.createCellStyle();
        numberCellStyle4.setFont(normalFont);
        numberCellStyle4.setDataFormat(df.getFormat("#,##0.00000000"));
        setBorder(numberCellStyle4);
        styles.put("numberCell4", numberCellStyle4);

        XSSFCellStyle fontstyle = wb.createCellStyle();
        fontstyle.setDataFormat(df.getFormat("@"));
        styles.put("fontfinally", fontstyle);

        return styles;
    }

    public void generateHeader(XSSFSheet sheet, String[] columnHeaders, int rowIndex) {
        rowIndex = 0;
        XSSFRow r = sheet.createRow(rowIndex++);
        for (int i = 0; i < columnHeaders.length; i++) {
            XSSFCell cell = r.createCell(i);
            sheet.autoSizeColumn((short) i);
            cell.setCellValue(columnHeaders[i]);

            cell.setCellStyle(styles.get("header"));
        }
    }

    public void setBorder(XSSFCellStyle style) {
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(HSSFColor.BLACK.index);

        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(HSSFColor.BLACK.index);

        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(HSSFColor.BLACK.index);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
    }

}
