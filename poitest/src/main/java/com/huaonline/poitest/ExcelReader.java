package com.huaonline.poitest;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;

public class ExcelReader {

    public static String fileToBeRead = "\\sdcard\\test.xls";

    /**
     * @param args
     */
    public static void ReaderTet() {
        try {
            // 创建对Excel工作簿文件的引用
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
                    fileToBeRead));
            HSSFSheet sheet = workbook.getSheetAt(0);
            int i = 0;
            while (true) {
                HSSFRow row = sheet.getRow(i);
                if (row == null) {
                    break;
                }
                HSSFCell cell0 = row.getCell((short) 0);
                HSSFCell cell1 = row.getCell((short) 1);
                HSSFCell cell2 = row.getCell((short) 2);
                HSSFCell cell3 = row.getCell((short) 3);
                HSSFCell cell4 = row.getCell((short) 4);
                HSSFCell cell5 = row.getCell((short) 5);
                HSSFCell cell6 = row.getCell((short) 6);

                System.out.print(cell0.getStringCellValue());
                System.out.print("," + cell1.getStringCellValue());
                System.out.print("," + cell2.getStringCellValue());
                System.out.print("," + cell3.getStringCellValue());
                System.out.print("," + cell4.getStringCellValue());
                System.out.print("," + cell5.getStringCellValue());
                System.out.println("," + cell6.getStringCellValue());
                i++;
            }
        } catch (Exception e) {
            System.out.println("已运行xlRead() : " + e);
        }
    }
}