package com.example.authmoduls.common.utils;

import com.example.authmoduls.auth.decorator.bookDecorator.BookResponseExcel;
import com.example.authmoduls.auth.decorator.UserResponseExcel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.List;


public class ExcelGenerator {

/*    private List <UserResponseExcel> userResponseExcels;*/
    private HashMap<String,List<BookResponseExcel>> bookResponseExcels;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelGenerator(List <UserResponseExcel> userResponseExcels, HashMap<String, List<BookResponseExcel>> bookResponseExcels) {
        /*this.userResponseExcels = userResponseExcels;*/
        this.bookResponseExcels = bookResponseExcels;
        workbook = new XSSFWorkbook();

    }
/*    private void writeHeader() {
        sheet = workbook.createSheet("Student");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "FirstName", style);
        createCell(row, 2, "MiddleName", style);
        createCell(row, 3, "LastName", style);
        createCell(row, 4, "MoblieNo", style);

    }*/
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }
/*
    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (UserResponseExcel record: userResponseExcels) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, record.getId(), style);
            createCell(row, columnCount++, record.getFirstName(), style);
            createCell(row, columnCount++, record.getMiddleName(), style);
            createCell(row, columnCount++, record.getLastName(), style);
            createCell(row, columnCount++, record.getMobileNo(), style);

        }
    }
    public void generateExcelFile(HttpServletResponse response) throws IOException {
        writeHeader();
        write();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }
*/
/*
    private void writeHeaderr() {
        sheet = workbook.createSheet("BookDetails");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        createCell(row, 2, "userId", style);
        createCell(row, 3, "FULL NAME", style);
        createCell(row, 4, "DATE", style);
        createCell(row, 5, "PRICE", style);
    }

    public void generateExcelSheet(HttpServletResponse response) throws IOException {
        writeHeaderr();
        writes();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

    }

    private void writes() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (Map.Entry<String, List<BookResponseExcel>> stringListEntry : bookResponseExcels.entrySet()) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, stringListEntry.getValue().get(0), style);
           *//* createCell(row, columnCount++, stringListEntry.getFullName(), style);
            createCell(row, columnCount++, stringListEntry.getPrice(), style);
            createCell(row, columnCount++, stringListEntry.getDate(), style);*//*
        }
    }*/

}
