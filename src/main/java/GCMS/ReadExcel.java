package GCMS;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
    public static List<CompoundGC> readexcelalkanes (String direxcel) throws IOException{

        FileInputStream fis = new FileInputStream((direxcel));
        Workbook wb = new XSSFWorkbook(fis); //XSSF es el usado en los archivos xlsx

        int numsheets = wb.getNumberOfSheets();

        int i;
        String infocelltxt = "";
        double infocellnum;
        CompoundGC compoundgc = null;
        List<CompoundGC> compoundgcList = null;
        compoundgcList = new ArrayList<>();

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
        String filexcel = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Calculo_RI_Alkanes.xlsx";
        try {
            List<CompoundGC> compoundgcList = readexcelalkanes(filexcel);
            GCMS gcms = new GCMS(compoundgcList);

            //System.out.println("List Size: "+gcms.getCompoundgcList().size());
            System.out.println("INFO EXCEL ALKANES:"+gcms.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
