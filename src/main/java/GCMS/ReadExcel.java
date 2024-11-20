package GCMS;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
TODO
3_ Esto deberia estar en carpeta utilities?
4_ funcion readexcel solo funciona para excel "Calculo_RI_Alkanes.xlsx"
5_ Crear clase que contenga la lista de compoundGC o hacerlo de otra menra?-> pensarlo  mas
 */

public class ReadExcel {
    public static List<CompoundGC> readexcel (String direxcel) throws IOException{

        FileInputStream fis = new FileInputStream((direxcel));
        Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

        int numsheets = wb.getNumberOfSheets();

        int i;
        String infocelltxt;
        double infocellnum;
        CompoundGC compoundgc = null;
        List<CompoundGC> compoundgcList = null;
        compoundgcList = new ArrayList<>();//Usar LinkedList??

        //Para este excel no haria falta leer el numero de sheets ya que sabemos que solo tiene una
        for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
            Sheet sheet = wb.getSheetAt(i);
            for (Row row : sheet) { //recorre filas de una hoja
                if(row.getRowNum() != 0) { //Row 0 has the titles (name, RI, RT)
                    compoundgc = new CompoundGC();
                    for (Cell cell : row) {//recorre columnas de la fila
                        if (cell.getColumnIndex() == 0){//column 0 is the name
                            infocelltxt = cell.getStringCellValue();
                            compoundgc.setName(infocelltxt);
                        } else if (cell.getColumnIndex() == 1){//column 1 is the RI
                            infocellnum = cell.getNumericCellValue();
                            compoundgc.setRI(infocellnum);
                        } else if (cell.getColumnIndex() == 2){//column 2 is the RT
                            infocellnum = cell.getNumericCellValue();
                            compoundgc.setRT(infocellnum);
                        }

                        /*
                        * esto est bien si no se el contenido de mi excel
                        * PEro sabemos que:
                            * primera columna es nombre
                            * segunda es RI
                            * tercera es RT
                        */
                        /*switch (cell.getCellType()) {
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
                        System.out.print(" | ");*/
                    }
                    compoundgcList.add(compoundgc);
                }
            }
        }

        wb.close();
        fis.close();
        return compoundgcList;
    }

    public static void main(String[] args) {
        String filexcel = "C:\\Users\\marta\\Documents\\Uni\\i. biomedica\\4\\TFG\\GCMS\\Calculo_RI_Alkanes.xlsx";
        try {
            List<CompoundGC> compoundgcList = readexcel (filexcel);
            Iterator <CompoundGC> itcompoundgc = compoundgcList.iterator();
            System.out.println("Lista: ");
            while(itcompoundgc.hasNext()){
                System.out.println("Elemento : "+itcompoundgc.next());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
