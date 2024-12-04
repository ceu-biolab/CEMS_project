package GCMS;

import cems_project.Compound;
import dbmanager.PubchemRest;
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
        double infocellnum1 = -1;
        double infocellnum2 = -1;
        CompoundGC compoundgc = null;
        Compound compoundInfo = null;
        List<CompoundGC> compoundgcList = null;
        compoundgcList = new ArrayList<>();

        int j=1;

        //Para este excel no haria falta leer el numero de sheets ya que sabemos que solo tiene una
        for (i = 0; i < numsheets; i++) { //for(Sheet sheet : wb)
            Sheet sheet = wb.getSheetAt(i);
            for (Row row : sheet) { //recorre filas de una hoja
                if(row.getRowNum() != 0) { //Row 0 has the titles (name, RI, RT)
                    //System.out.println("\nNUM FILA: "+j);
                    for (Cell cell : row) {//recorre columnas de la fila
                        if (cell.getColumnIndex() == 0) {//column 0 is the name
                            infocelltxt = cell.getStringCellValue();
                            //compoundgc.setName(infocelltxt);
                        } else if (cell.getColumnIndex() == 1) {//column 1 is the RI
                            infocellnum1 = cell.getNumericCellValue();
                            //compoundgc.setRI(infocellnum1);
                        } else if (cell.getColumnIndex() == 2) {//column 2 is the RT
                            infocellnum2 = cell.getNumericCellValue();
                            //compoundgc.setRT(infocellnum2);
                        }
                    }
                    //System.out.println("Antes sacar compound valores: "+infocelltxt+" 546547");

                    /*
                    El excel se seguira leyendo pero solo si hay un nombre de compuesto (infocelltxt)
                    se incluira en la lista y se buscara su informacion
                     */
                    if(!infocelltxt.isBlank()){
                        compoundInfo = PubchemRest.getCompoundFromName(infocelltxt);

                        /*System.out.println("INFO EXTRA: \n"+compoundInfo.getCompound_id() +
                                compoundInfo.getCompoundName() + " "+
                                compoundInfo.getCasId() + " "+ compoundInfo.getFormula() + " "+ compoundInfo.getMonoisotopicMass() + " "+ compoundInfo.getCompound_status() + " "+
                                compoundInfo.getCompound_type() + " "+ compoundInfo.getLogP() + " "+ compoundInfo.getIdentifiersOwn() + " "+ compoundInfo.getIdentifiersParent() + " "+
                                infocellnum1 + " "+ infocellnum2 + "\n");*/

                        compoundgc = new CompoundGC(compoundInfo.getCompound_id(),
                                compoundInfo.getCompoundName(),
                                compoundInfo.getCasId(), compoundInfo.getFormula(), compoundInfo.getMonoisotopicMass(), compoundInfo.getCompound_status(),
                                compoundInfo.getCompound_type(), compoundInfo.getLogP(), compoundInfo.getIdentifiersOwn(), compoundInfo.getIdentifiersParent(),
                                infocellnum1, infocellnum2);

                        compoundgcList.add(compoundgc);
                        infocelltxt = "";
                    }
                    j++;
                }
            }
        }
        //System.out.println("\nTam lista: "+compoundgcList.size());

        wb.close();
        fis.close();
        return compoundgcList;
    }

    public static void main(String[] args) {
        //Poner direccion en la que se tenga el excel del calculo de RI de ALkanes
        String filexcel = "C:\\Users\\marta\\Documents\\Uni\\Biomedica_TFG\\Calculo_RI_Alkanes.xlsx";
        try {
            List<CompoundGC> compoundgcList = readexcelalkanes(filexcel);
            GCMS gcms = new GCMS(compoundgcList);

            System.out.println("\nList Size: "+gcms.getCompoundgcList().size());
            System.out.println("INFO EXCEL ALKANES:"+gcms.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
