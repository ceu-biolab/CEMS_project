package GCMS;

import cems_project.Compound;
import cems_project.Identifier;
import dbmanager.DBManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static GCMS.ReadTxt.*;
import static patternFinders.RegexInChI.getFormulaFromInChI;


public class GCMS_insert {

    private static final Logger LOGGER = Logger.getLogger(ReadTxt.class.getName());

    /**
     * Printing to know which compounds from the txt list fail when they are used to obtain
     * their complete information from PubChem. It prints the errors, the compound map, the casid not found map,
     * the compound name not found map, the inchi not found map and the smiles not found map.
     * @param compoundGCAndPossibleErrors
     */
    private static void printingValidation(CompoundGCAndPossibleErrors compoundGCAndPossibleErrors){
        Map<Integer, CompoundGC> compoundGCMap = compoundGCAndPossibleErrors.getCompoundGC();
        Map<Integer, CompoundGCInformationTxt> casIdNotfoundMap = compoundGCAndPossibleErrors.getCasIdNotFound();
        Map<Integer, CompoundGCInformationTxt> nameNotFoundMap = compoundGCAndPossibleErrors.getNameNotFound();
        Map<Integer, CompoundGCInformationTxt> inchiNotFoundMap = compoundGCAndPossibleErrors.getInchiNotFound();
        Map<Integer, CompoundGCInformationTxt> smilesNotFoundMap = compoundGCAndPossibleErrors.getSmilesNotFound();

        int i;
        List<String> infoError = compoundGCAndPossibleErrors.getError();
        System.err.println("ERROR WARNING: ");
        for(i=0; i<infoError.size(); i++){
            System.err.println(infoError.get(i));
        }

        i=0;
        System.out.println("COMPOUNDS: ");
        for (Map.Entry<Integer, CompoundGC> compounGC : compoundGCMap.entrySet()) {

            System.out.println(i+1 + ": ORIGINAL LIST POSITION: " + compounGC.getKey() + "; " +
                    compounGC.getValue().getCompoundName() + "; "+compounGC.getValue().getINCHI() + "; "
                    + compounGC.getValue().getINCHIKey());
            i++;
        }
        System.out.println("Total compounds found: "+i);
        System.out.println("Total compounds found count: "+compoundGCMap.size());

        i=0;
        System.out.println("\nCASID NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> casIdNotFound : casIdNotfoundMap.entrySet()) {
            System.out.println(i+1 +": ORIGINAL LIST POSITION: " + casIdNotFound.getKey() +
                    "; NAME: " + casIdNotFound.getValue().getCName());
            i++;
        }
        System.out.println("Total compounds casId not found txt file: "+i);
        System.out.println("Total compounds casId not found txt file count: "+casIdNotfoundMap.size());

        i=0;
        System.out.println("\nNAME NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> nameNotFound : nameNotFoundMap.entrySet()) {
            System.out.println(i+1 +": ORIGINAL LIST POSITION: " + nameNotFound.getKey() +
                    "; NAME: " + nameNotFound.getValue().getCName());
            i++;
        }
        System.out.println("Total compounds name not found on PubChem: "+i);
        System.out.println("Total compounds name not found on PubChem count: "+nameNotFoundMap.size());

        i=0;
        System.out.println("\nINCHI NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> inchiNotFound : inchiNotFoundMap.entrySet()) {
            System.out.println(i+1 +": ORIGINAL LIST POSITION: " + inchiNotFound.getKey() +
                    "; NAME: " + inchiNotFound.getValue().getCName());
            i++;
        }
        System.out.println("Total compounds inchi not found txt file: "+i);
        System.out.println("Total compounds inchi not found txt file count: "+inchiNotFoundMap.size());

        i=0;
        System.out.println("\nSMILES NOT FOUND: ");
        for (Map.Entry<Integer, CompoundGCInformationTxt> smilesNotFound : smilesNotFoundMap.entrySet()) {
            System.out.println(i+1 +": ORIGINAL LIST POSITION: " + smilesNotFound.getKey() +
                    "; NAME: " + smilesNotFound.getValue().getCName() +
                    "; INCHI: " + smilesNotFound.getValue().getInchi() +
                    "; INCHIKEY: " +smilesNotFound.getValue().getInchiKey());
            i++;
        }
        System.out.println("Total compounds smiles not found txt file: "+i);
        System.out.println("Total compounds smiles not found txt file count: "+ smilesNotFoundMap.size());

        i=0;
        System.out.println("\nCOMPOUNDS ALL INFO: ");
        for (Map.Entry<Integer, CompoundGC> compounGC : compoundGCMap.entrySet()) {
                /*System.out.println("ORIGINAL LIST POSITION: " + compounGC.getKey()
                                + "\n" + compounGC.getValue().toString());*/
            //System.out.println(i+1 + ": ORIGINAL LIST POSITION: " + compounGC.getKey() + "; \n\t" +
               //     compounGC.getValue().toString());
            i++;
        }
        System.out.println("Total compounds found: "+i);
        System.out.println("Total compounds found count: "+compoundGCMap.size() + "\n");
    }

    /**
     * Insert compoundGC into the DataBase
     * @param compoundGCList
     */
    private static void uploadInformationCompoundGCDB(List<CompoundGC> compoundGCList) throws IOException {
        String dbName = "jdbc:mysql://localhost/compounds?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

        String dbUser = "root";
        String dbPassword = "";
        DBManager db = new DBManager();

        try {
            db.connectToDB(dbName, dbUser, dbPassword);

            CompoundGC compoundGC;
            int i;
            int size = compoundGCList.size();

            for (i = 1; i <= compoundGCList.size(); i++) {
                compoundGC = compoundGCList.get(i - 1);

                int gcRIid = -1;
                int compoundGCId = -1;
                int idSpectrum = -1;


                compoundGC.setCompound_id(db.getCompoundIdFromInchiKey(compoundGC.getINCHIKey()));

                if (compoundGC.getCompound_id() == 0) {//The compound is not found - we have to insert everything
                    if(i==102){ //compound 168564 - THE COMPOUND EXIST BUT DO NOT HAVE THE IDENTIFIERS
                        compoundGC.setCompound_id(168564);
                        Integer pubchemId = compoundGC.getIdentifiersOwn().getPc_id();
                        if(pubchemId!=null){
                            db.insertPC(168564, pubchemId);
                        }
                        db.insertIntoCompoundIdentifiers(compoundGC);

                    } else if(i==146){ //compound 98953
                        compoundGC.setCompound_id(98953);
                        Integer pubchemId = compoundGC.getIdentifiersOwn().getPc_id();
                        if(pubchemId!=null){
                            db.insertPC(98953, pubchemId);
                        }
                    } else {
                        compoundGCId = db.insertIntoCompounds(compoundGC);
                        compoundGC.setCompound_id(compoundGCId);

                        Integer pubchemId = compoundGC.getIdentifiersOwn().getPc_id();
                        if (pubchemId != null) {
                            db.insertPC(compoundGCId, pubchemId);
                        }

                        db.insertIntoCompoundIdentifiers(compoundGC);

                    }

                    db.insertDerivatizationMethod(compoundGC);
                    db.insertGCColumn(compoundGC);
                    db.insertRIRT(compoundGC);

                    int spectraSize = compoundGC.getGcmsSpectrum().size();
                    for(int j=0; j<spectraSize; j++){
                        int spectrumId = db.insertgcmsSpectrum(compoundGC);

                        int peaksSize = compoundGC.getGcmsSpectrum().get(j).getGcms_peaksList().size();
                        for(int k=0; k<peaksSize; k++){
                            GCMS_Peaks gcms_peaks = compoundGC.getGcmsSpectrum().get(j).getGcms_peaksList().get(k);
                            db.insertGCMSPeaks(spectrumId, gcms_peaks);
                        }
                    }

                } else { //IF THE COMPOUND ALREADY EXISTS
                    gcRIid = db.getgcrirtIdfromCompoundId(compoundGC.getCompound_id());
                    if (gcRIid == 0) {//the ri is not inserted
                        db.insertDerivatizationMethod(compoundGC);
                        db.insertGCColumn(compoundGC);
                        db.insertRIRT(compoundGC);

                        int spectraSize = compoundGC.getGcmsSpectrum().size();
                        for(int j=0; j<spectraSize; j++){
                            int spectrumId = db.insertgcmsSpectrum(compoundGC);

                            int peaksSize = compoundGC.getGcmsSpectrum().get(j).getGcms_peaksList().size();
                            for(int k=0; k<peaksSize; k++){
                                GCMS_Peaks gcms_peaks = compoundGC.getGcmsSpectrum().get(j).getGcms_peaksList().get(k);
                                db.insertGCMSPeaks(spectrumId, gcms_peaks);
                            }
                        }
                    } else { //ri is inserted
                        int spectraSize = compoundGC.getGcmsSpectrum().size();
                        for(int j=0; j<spectraSize; j++){
                            int spectrumId = db.insertgcmsSpectrum(compoundGC);

                            int peaksSize = compoundGC.getGcmsSpectrum().get(j).getGcms_peaksList().size();
                            for(int k=0; k<peaksSize; k++){
                                GCMS_Peaks gcms_peaks = compoundGC.getGcmsSpectrum().get(j).getGcms_peaksList().get(k);
                                db.insertGCMSPeaks(spectrumId, gcms_peaks);
                            }
                        }
                    }
                }
            }
        } finally {
            //WE NEED TO CLOSE THE RESOURCES EVEN IF THERE IS AN EXCEPTION
            db.closeStatementResource();
            db.closeConnectionResource();
        }
    }

    /**
     *
     * Creates the compoundGC 'manually', with the information provided
     * @param compoundGCFromSmilesNotFound
     * @return The compoundGC
     */
    private static CompoundGC compoundGCInformationManuallyExtracted(CompoundGCInformationTxt compoundGCFromSmilesNotFound){
        CompoundGC compoundGC = null;

        Double RI = compoundGCFromSmilesNotFound.getRI();
        List<GCMS_Spectrum> gcmsSpectrum = compoundGCFromSmilesNotFound.getGcmsSpectra();

        String name = compoundGCFromSmilesNotFound.getCName();
        String casId = null;
        String inchi = compoundGCFromSmilesNotFound.getInchi();
        String inchiKey = compoundGCFromSmilesNotFound.getInchiKey();
        String smiles = compoundGCFromSmilesNotFound.getSmiles();

        String formula = getFormulaFromInChI(inchi);
        Double monoisotopicMass = compoundGCFromSmilesNotFound.getMonoisotopicMass();
        Integer compound_id = 0;
        Integer compound_type = 0;
        Integer compound_status = 0;
        Double logP = null;

        Identifier identifiersOwn = new Identifier(inchi, inchiKey, smiles);
        Compound compound = new Compound(compound_id, name, casId, formula, monoisotopicMass, compound_status, compound_type, logP, identifiersOwn);

        compoundGC = new CompoundGC(compound_id, name, casId, formula, monoisotopicMass, compound_status, compound_type, logP,
                identifiersOwn, compound.getIdentifiersParent(), RI, gcmsSpectrum);

        return compoundGC;
    }

    //MAIN TO INSERT COMPOUNDS FROM TXT
    public static void main(String[] args) {

        String fileTxt = "src/main/resources/gcms_excels/gcms_cloroformiates_library.txt";

        try{
            int i;

            List<CompoundGCInformationTxt> compoundGCInformationTxts = getCompoundFromTxt(fileTxt);
            CompoundGCAndPossibleErrors compoundGCAndPossibleErrors = getGCMSCloroformiatesFromTxt(compoundGCInformationTxts);

            CompoundGC compoundgc;

            //printingValidation(compoundGCAndPossibleErrors);

            Map<Integer, CompoundGCInformationTxt> smilesNotFoundMap = compoundGCAndPossibleErrors.getSmilesNotFound();
            Map<Integer, CompoundGC> compoundGCMap = compoundGCAndPossibleErrors.getCompoundGC();

            List<CompoundGC> compoundGCList = new ArrayList<>(compoundGCMap.values());

            i=0;
            //COMPOUNDS NOT FOUND BY CASID, NAME, INCHI OR SMILES --> 'MANUALLY' EXTRACT THE INFORMATION
            for (Map.Entry<Integer, CompoundGCInformationTxt> smilesNotFound : smilesNotFoundMap.entrySet()) {
                compoundgc = compoundGCInformationManuallyExtracted(smilesNotFound.getValue());
                compoundGCList.add(compoundgc);
                i++;

            }

            uploadInformationCompoundGCDB(compoundGCList);

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}