/*package GCMS;


import com.mysql.cj.result.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; //used for .xlsx

import java.io.*;
import java.util.*;


public class PruebasLectura {
    public static void main(String[] args) {
        String filexcel = "C:\\Users\\marta\\Documents\\Uni\\i. biomedica\\4\\TFG\\GCMS\\Calculo_RI_Alkanes.xlsx";
        //String filexcel = "C:\\Users\\marta\\Documents\\Uni\\i. biomedica\\4\\TFG\\GCMS\\Prueba.xlsx";

        try {
            FileInputStream fis = new FileInputStream(new File(filexcel));
            Workbook wb = new XSSFWorkbook(fis); //es el usado en los archivos xlsx
            int numsheets = wb.getNumberOfSheets();
            int i;
            String infocelltxt;
            double infocellnum;
            for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
                Sheet sheet = wb.getSheetAt(i);
                System.out.println("\nSheet num: " + i+ "\nNOMBRE SHEET: "+sheet.getSheetName()+"\n");
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        switch (cell.getCellType()){
                            case STRING:{
                                infocelltxt = cell.getStringCellValue();
                                System.out.println("INFO CELL: " + infocelltxt);
                                break;
                            }
                            case NUMERIC:{
                                infocellnum = cell.getNumericCellValue();
                                System.out.println("INFO CELL: "+infocellnum);
                                break;
                            }
                        }
                    }
                }
            }
        wb.close();
        fis.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}*/

    /*public static void main(String[] args) {
        File file = new File("C:\\Users\\marta\\Documents\\Uni\\i. biomedica\\4\\TFG\\GCMS\\Prueba.xlsx");

        try {
            InputStream is = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(is);
            WorksheetCollection collection = wb.getWorksheets();
            //AÑADIR A MAVEN:

            <repository>
    <id>AsposeJavaAPI</id>
    <name>Aspose Java API</name>
    <url>https://repository.aspose.com/repo/</url>
</repository>
<dependency>
    <groupId>com.aspose</groupId>
    <artifactId>aspose-cells</artifactId>
    <version>21.11</version>
</dependency>

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }*/
//}






//            try {
//                File file = new File("C:\\Users\\marta\\Documents\\Uni\\i. biomedica\\4\\TFG\\GCMS\\Prueba.xlsx");
//                //InputStream inp =  new FileInputStream(f);
//        /*} catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }*/
//            /*BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//            String txt = bf.readLine();*/
//                InputStream inp = new FileInputStream(file);
//                Workbook wb = WorkbookFactory.create(inp);
//
//                int numsheets = wb.getNumberOfSheets();
//                System.out.println("num sheets: " + numsheets);
//
//                int i;
//                for (i = 0; i < numsheets; i++) {
//                    Sheet sheet = wb.getSheetAt(i);
//                    System.out.println("Sheet num: " + i);
//
//                    Row row = sheet.getRow(1);
//                    System.out.println("La row es: " + row.toString());
//                    //int[] row = sheet.getRowBreaks();
//                    //Row row;
//                    //Iterator<Row> row = sheet.rowIterator();
//                    /*while (row.hasNext()) {
//                        System.out.println("La row es: " + row);
//                        //Cell cell = row.next();
//                        //Cell cell = row.getCell(row);
//                        //String value = cell.getStringCellValue();
//                        //System.out.println("Valor de la celda es " + value);
//                        //iRow++;
//                        //row = sh.getRow(iRow);
//                    }*/
//
//                /*while (row != null) {
//                    //Cell cell = row.getCell(1);
//                    String value = cell.getStringCellValue();
//                    System.out.println("Valor de la celda es " + value);
//                    iRow++;
//                    row = sh.getRow(iRow);
//                }*/
//                }
//
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//
//
//    /*String excelPath = "C:\\Users\\marta\\Documents\\Uni\\i. biomedica\\4\\TFG\\GCMS\\Prueba.xlsx";
//    //FileInputStream fis;
//
//    {
//        try {
//            FileInputStream fis = new FileInputStream(new File(excelPath));
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }*/
//
//    /*try{
//        FileInputStream fis = new FileInputStream(new File(excelPath));
//    }catch (IOException e){
//        e.printStackTrace();
//    }*/
//
//
////    }throw new RuntimeException(IOException e);
////    //throw new RuntimeException(${EXCEPTION});
////    /*catch (IOException e){
////        e.printStackTrace();
////    }*/
//
////    try {
////        FileInputStream fis = new FileInputStream(new File(excelPath));
////
////    }throw new RuntimeException(IOException e);
////    //throw new RuntimeException(${EXCEPTION});
////    /*catch (IOException e){
////        e.printStackTrace();
////    }*/
//
//    }
//}


/*for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
    Sheet sheet = wb.getSheetAt(i);
    for (Row row : sheet) { //recorre filas de una hoja
        for (Cell cell : row) {
            /*switch (cell.getCellType()) {//TBN ESTA XA BOOLEAN?
                case STRING: {
                    infocelltxt = cell.getStringCellValue();
                    //System.out.println("INFO CELL: " + infocelltxt);
                    System.out.print(infocelltxt);
                    break;
                }
                case NUMERIC: {
                    infocellnum = cell.getNumericCellValue();
                    //System.out.println("INFO CELL: " + infocellnum);
                    System.out.print(infocellnum);
                    break;
                }
            }
        }
    }
}*/
