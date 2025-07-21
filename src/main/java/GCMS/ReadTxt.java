package GCMS;

import cems_project.Compound;
import dbmanager.PubchemRest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReadTxt {

    private static final Logger LOGGER = Logger.getLogger(ReadTxt.class.getName());

    private static final String NUMBER = "NUMBER:";
    private static final String NAME = "NAME:";
    private static final String RETENTION_INDEX = "RETENTION INDEX:";
    private static final String INCHI = "INCHI:";
    private static final String SMILES = "SMILES:";
    private static final String CAS = "CAS:";
    private static final String MONOISOTOPIC = "MONOISOTOPIC:";
    private static final String INCHIKEY = "INCHIKEY:";
    private static final String BEGIN_IONS = "BEGIN IONS";
    private static final String END_IONS = "END IONS";

    /**
     * This method calculates the normalized intensities in % of a list of peaks (Spectrum).
     * @param gcms_peaksList list of peaks
     * @return the normalized PeakList
     */
    private static List<GCMS_Peaks> normalizedPeakList(List<GCMS_Peaks> gcms_peaksList){
        List<GCMS_Peaks> normalizedSpectrum = new ArrayList<>();
        int size = gcms_peaksList.size();
        for(int i=0; i<size; i++){
            //THE FIRST INTENSITY IS THE HIGHEST SINCE THE INTENSITIES ARE IN ORDER
            GCMS_Peaks gcms_peaks = new GCMS_Peaks();
            double mz = gcms_peaksList.get(i).getMz();

            double maxIntensity = gcms_peaksList.get(0).getIntensity();
            double intensity = gcms_peaksList.get(i).getIntensity();

            double normalizedIntensity = (intensity/maxIntensity)*100;

            gcms_peaks.setMz(mz);
            gcms_peaks.setIntensity(normalizedIntensity);
            normalizedSpectrum.add(gcms_peaks);
        }
        return normalizedSpectrum;
    }

    /**
     * From the txt file with the compounds it gets the information (Name, RI, CasId and Spectrum) and creates java objects
     * @param FilePath Path where the txt file is stored
     * @return CompoundGCInformationTxt List (A compound with only the information of the txt)
     * @throws IOException
     */
    public static List<CompoundGCInformationTxt> getCompoundFromTxt(String FilePath) throws IOException {

        //INFORMATION COMPOUND TXT
        Double RI = -1.0;
        String numberOriginalListString = "";
        Integer numberOriginalList = -1;
        String compoundName ="";
        String RIString = "";
        String casId = "";
        String inchi = "NotRelevant";
        String smiles = "NotRelevant";
        String inchiKey = "NotRelevant";
        String monoisotopicMassString = "";
        Double monoisotopicMass = null;

        List<CompoundGCInformationTxt> compoundGCInformationTxtList = new ArrayList<>();

        List<String> AllLines = Files.readAllLines(Paths.get(FilePath));
        int i=0;
        int j=1;
        int l=1;

        List<GCMS_Peaks> gcms_peaksList_Normalized = new ArrayList<>();

        GCMS_Spectrum gcmsSpectrum;
        List<GCMS_Spectrum> gcmsSpectra = new ArrayList<>();

        for(i=0; i< AllLines.size(); i++){
            if(((AllLines.get(i) !=null) || (!AllLines.get(i).trim().isEmpty()))){
                if (AllLines.get(i).toUpperCase().contains(NUMBER)) {
                    numberOriginalListString = AllLines.get(i).substring(NUMBER.length()).trim();
                    numberOriginalList = Integer.parseInt(numberOriginalListString);
                } else if (AllLines.get(i).toUpperCase().contains(NAME)) {
                    compoundName = AllLines.get(i).substring(NAME.length()).trim();
                } else if (AllLines.get(i).toUpperCase().contains(INCHI)) {
                    inchi = AllLines.get(i).substring(INCHI.length()).trim();
                    if(inchi.equals("")){
                        inchi = "NotRelevant"; //We will get it in the next steps
                    }
                } else if (AllLines.get(i).toUpperCase().contains(SMILES)) {
                    smiles = AllLines.get(i).substring(SMILES.length()).trim();
                    if(smiles.equals("")){
                        smiles = "NotRelevant"; //We will get it in the next steps
                    }
                } else if (AllLines.get(i).toUpperCase().contains(INCHIKEY)) {
                    inchiKey = AllLines.get(i).substring(INCHIKEY.length()).trim();
                    if(inchiKey.equals("")){
                        inchiKey = "NotRelevant"; //We will get it in the next steps
                    }
                }else if (AllLines.get(i).toUpperCase().contains(RETENTION_INDEX)) {
                    RIString = AllLines.get(i).substring(RETENTION_INDEX.length()).trim();
                    RIString = RIString.replace(",", ".");
                    RI = Double.parseDouble(RIString);
                } else if (AllLines.get(i).toUpperCase().contains(MONOISOTOPIC)) {
                    monoisotopicMassString = AllLines.get(i).substring(MONOISOTOPIC.length()).trim();
                    monoisotopicMassString = monoisotopicMassString.replace(",", ".");
                    monoisotopicMass = Double.parseDouble(monoisotopicMassString);
                } else if (AllLines.get(i).toUpperCase().contains(CAS)) {
                    casId = AllLines.get(i).substring(CAS.length()).trim();
                    if(casId.equals("")){
                        casId = "NotFound";
                    }
                } else if(AllLines.get(i).toUpperCase().contains(BEGIN_IONS)){//TODO que lea todos espectros
                    //i++;//NOT USE LINE: 'BEGIN IONS'

                    List<GCMS_Peaks> gcms_peaksList;// = new ArrayList<>();
                    //System.out.println("\nNEW COMPOUND "+j);
                    do{
                        i++;
                        gcms_peaksList = new ArrayList<>();
                        while(!AllLines.get(i).equalsIgnoreCase(END_IONS)){
                            GCMS_Peaks gcms_peaks = new GCMS_Peaks();

                            String info = AllLines.get(i).trim();
                            info = info.replace(",", ".");
                            // '\\s+' --> FOR SPACES (' '), TAB (\t) OR NEW LINE (\n); '+' -> CONSECUTIVE SPACES
                            String[] peaks = info.split("\\s+");
                            //System.out.println("Peaks: "+peaks[0]+";"+peaks[1]);
                            double mz = Double.parseDouble(peaks[0]);
                            double intensity = Double.parseDouble(peaks[1]);

                            gcms_peaks.setMz(mz);
                            gcms_peaks.setIntensity(intensity);

                            gcms_peaksList.add(gcms_peaks);

                            i++;
                        }
                        gcms_peaksList_Normalized = normalizedPeakList(gcms_peaksList);
                        //System.out.println("GCMSPEAKList: "+gcms_peaksList_Normalized);
                        gcmsSpectrum = new GCMS_Spectrum();
                        gcmsSpectrum.setGcms_peaksList(gcms_peaksList_Normalized);
                        gcmsSpectra.add(gcmsSpectrum);
                        i++;
                    }while (i < AllLines.size() && AllLines.get(i).equalsIgnoreCase(BEGIN_IONS));
                    j++;
                    /*gcms_peaksList_Normalized = normalizedPeakList(gcms_peaksList);
                    //System.out.println("GCMSPEAKList: "+gcms_peaksList_Normalized);
                    gcmsSpectrum = new GCMS_Spectrum();
                    gcmsSpectrum.setGcms_peaksList(gcms_peaksList_Normalized);
                    gcmsSpectra.add(gcmsSpectrum);*/
                }
            }
            //ONLY IF ALL THE INFORMATION IS SAVED, compoundGCInformationTxtList CAN BE CREATED
            //if((!casId.equals("")) && (!gcms_peaksList_Normalized.isEmpty()) /*&& (RI!=-1.0)*/) {
            if((!casId.equals("")) && (!gcms_peaksList_Normalized.isEmpty()) /*&& (RI!=-1.0)*/) {
                //gcmsSpectrum = new GCMS_Spectrum();
                //gcmsSpectrum.setGcms_peaksList(gcms_peaksList_Normalized);
                CompoundGCInformationTxt compoundGCInformationTxt;

                //if(RI!=-1.0){
                    if (!inchi.equals("NotRelevant") && !smiles.equals("NotRelevant")){
                        compoundGCInformationTxt = new CompoundGCInformationTxt(numberOriginalList,
                                //compoundName, RI, casId, gcmsSpectrum, inchi, smiles, monoisotopicMass);
                                //compoundName, RI, casId, gcmsSpectra, inchi, smiles, monoisotopicMass);
                                compoundName, RI, casId, gcmsSpectra, inchi, inchiKey, smiles, monoisotopicMass);

                        //System.out.println("En if: "+compoundGCInformationTxt.toString());
                        /*compoundGCInformationTxtList.add(compoundGCInformationTxt);

                        //RESET casId & gcms_peaksList_Normalized SO THAT IT DOES NOT CREAT A NEW COMPOUND WITH THE WRONG INFORMATION
                        casId = "";
                        RIString = "";
                        RI = -1.0;
                        gcms_peaksList_Normalized = new ArrayList<>();
                        inchi = "NotRelevant";
                        smiles = "NotRelevant";*/
                    } else {
                        compoundGCInformationTxt = new CompoundGCInformationTxt(numberOriginalList,
                                //compoundName, RI, casId, gcmsSpectrum);
                                compoundName, RI, casId, gcmsSpectra);
                        /*compoundGCInformationTxtList.add(compoundGCInformationTxt);

                        //RESET casId & gcms_peaksList_Normalized SO THAT IT DOES NOT CREAT A NEW COMPOUND WITH THE WRONG INFORMATION
                        casId = "";
                        RIString = "";
                        RI = -1.0;
                        gcms_peaksList_Normalized = new ArrayList<>();
                        inchi = "NotRelevant";
                        smiles = "NotRelevant";*/
                    }
               // }
                compoundGCInformationTxtList.add(compoundGCInformationTxt);

                //RESET casId & gcms_peaksList_Normalized SO THAT IT DOES NOT CREAT A NEW COMPOUND WITH THE WRONG INFORMATION
                casId = "";
                RIString = "";
                RI = -1.0;
                gcms_peaksList_Normalized = new ArrayList<>();
                inchi = "NotRelevant";
                smiles = "NotRelevant";
                inchiKey = "NotRelevant";
                monoisotopicMassString = "";
                monoisotopicMass = null;
                gcmsSpectra = new ArrayList<>();
            }
        }
        return compoundGCInformationTxtList;
    }

    /**
     * From the casId it gets the compoundGC
     * @param compoundGCInformationTxts List with the information of the txt file
     * @return CompoundGC with the relevant information of PubChem website
     * @throws IOException
     */
    private static CompoundGC getCompoundGCIfCasID (CompoundGCInformationTxt compoundGCInformationTxts) throws IOException{
        Compound compoundInfo;
        CompoundGC compoundGC;
        int cid = PubchemRest.getPCIDFromCasId(compoundGCInformationTxts.getCasId());
        compoundInfo = PubchemRest.getCompoundFromPCID(cid);
        String nameIupac = compoundInfo.getCompoundName();
        String givenName = compoundGCInformationTxts.getCName();
        //String name = (!nameIupac.isEmpty()) ? nameIupac : givenName;
        String name = (nameIupac!=null) ? nameIupac : givenName;
        compoundGC = new CompoundGC(compoundInfo.getCompound_id(),
                //compoundGCInformationTxts.getCName(), //Better use the IUPAC name
                //compoundInfo.getCompoundName(),
                name,
                compoundGCInformationTxts.getCasId(),
                compoundInfo.getFormula(),
                compoundInfo.getMonoisotopicMass(),
                compoundInfo.getCompound_status(),
                compoundInfo.getCompound_type(), compoundInfo.getLogP(),
                compoundInfo.getIdentifiersOwn(),
                compoundInfo.getIdentifiersParent(),
                compoundGCInformationTxts.getRI(),
                compoundGCInformationTxts.getGcmsSpectra());

        System.out.println("Nombre tengo from CasId: "+compoundGCInformationTxts.getCName() +
                "\nNombre que recibo IUPAC: " + compoundInfo.getCompoundName() +
                "\nName:                  : " + name);

        return compoundGC;
    }

    /**
     * From the name it gets the compoundGC
     * @param compoundGCInformationTxts List with the information of the txt file
     * @return CompoundGC with the relevant information of PubChem website
     * @throws IOException
     */
    private static CompoundGC getCompoundGCIfName (CompoundGCInformationTxt compoundGCInformationTxts) throws IOException{
        Compound compoundInfo;
        CompoundGC compoundGC;
        //compoundInfo = PubchemRest.getCompoundFromName(compoundGCInformationTxts.getCName());
        compoundInfo = PubchemRest.getCompoundIUPACNameFromName(compoundGCInformationTxts.getCName());
        String nameIupac = compoundInfo.getCompoundName();
        String givenName = compoundGCInformationTxts.getCName();
        //String name = (!nameIupac.isEmpty()) ? nameIupac : givenName;
        String name = (nameIupac!=null) ? nameIupac : givenName;
        compoundGC = new CompoundGC(compoundInfo.getCompound_id(),
                //compoundGCInformationTxts.getCName(), //Better use the IUPAC name
                //compoundInfo.getCompoundName(),
                name,
                //compoundGCInformationTxts.get(i).getCasId(), //NotFound
                compoundInfo.getCasId(), //null
                compoundInfo.getFormula(),
                compoundInfo.getMonoisotopicMass(),
                compoundInfo.getCompound_status(),
                compoundInfo.getCompound_type(), compoundInfo.getLogP(),
                compoundInfo.getIdentifiersOwn(),
                compoundInfo.getIdentifiersParent(),
                compoundGCInformationTxts.getRI(),
                compoundGCInformationTxts.getGcmsSpectra());

        System.out.println("Nombre tengo from  Name: "+compoundGCInformationTxts.getCName() +
                "\nNombre que recibo IUPAC: " + compoundInfo.getCompoundName() +
                "\nName:                  : " + name);

        return compoundGC;
    }

    /**
     * TODO
     * From the Inchi it gets the compoundGC
     * @param compoundGCInformationTxts List with the information of the txt file
     * @return CompoundGC with the relevant information of PubChem website
     * @throws IOException
     */
    private static CompoundGC getCompoundGCIfInchi (CompoundGCInformationTxt compoundGCInformationTxts) throws IOException{
        Compound compoundInfo;
        CompoundGC compoundGC;
        int cid = PubchemRest.getPCIDFromInchi(compoundGCInformationTxts.getInchi());
        compoundInfo = PubchemRest.getCompoundFromPCID(cid);
        String nameIupac = compoundInfo.getCompoundName();
        String givenName = compoundGCInformationTxts.getCName();
        //String name = (!nameIupac.isEmpty()) ? nameIupac : givenName;
        String name = (nameIupac!=null) ? nameIupac : givenName;

        compoundGC = new CompoundGC(compoundInfo.getCompound_id(),
                //compoundGCInformationTxts.getCName(), //Better use the IUPAC name
                //compoundInfo.getCompoundName(),
                name,
                compoundInfo.getCasId(), //null
                compoundInfo.getFormula(),
                compoundInfo.getMonoisotopicMass(),
                compoundInfo.getCompound_status(),
                compoundInfo.getCompound_type(), compoundInfo.getLogP(),
                compoundInfo.getIdentifiersOwn(),
                compoundInfo.getIdentifiersParent(),
                compoundGCInformationTxts.getRI(),
                compoundGCInformationTxts.getGcmsSpectra());
        //para comprobacion
        System.out.println("Nombre tengo from Inchi: "+compoundGCInformationTxts.getCName() +
                "\nNombre que recibo IUPAC: " + compoundInfo.getCompoundName() +
                "\nName:                  : " + name);
        return compoundGC;
    }

    /**
     * From the Inchi it gets the compoundGC
     * @param compoundGCInformationTxts List with the information of the txt file
     * @return CompoundGC with the relevant information of PubChem website
     * @throws IOException
     */
    private static CompoundGC getCompoundGCIfSmiles (CompoundGCInformationTxt compoundGCInformationTxts) throws IOException{
        //System.out.println("ENTRA EN IFSMILES");
        Compound compoundInfo;
        CompoundGC compoundGC;
        int cid = PubchemRest.getPCIDFromSmiles(compoundGCInformationTxts.getSmiles());
        compoundInfo = PubchemRest.getCompoundFromPCID(cid);
        String nameIupac = compoundInfo.getCompoundName();
        String givenName = compoundGCInformationTxts.getCName();
        //String name = (!nameIupac.isEmpty()) ? nameIupac : givenName;
        String name = (nameIupac!=null) ? nameIupac : givenName;

        compoundGC = new CompoundGC(compoundInfo.getCompound_id(),
                //compoundGCInformationTxts.getCName(), //Better use the IUPAC name
                //compoundInfo.getCompoundName(),
                name,
                compoundInfo.getCasId(),
                compoundInfo.getFormula(),
                compoundInfo.getMonoisotopicMass(),
                compoundInfo.getCompound_status(),
                compoundInfo.getCompound_type(), compoundInfo.getLogP(),
                compoundInfo.getIdentifiersOwn(),
                compoundInfo.getIdentifiersParent(),
                compoundGCInformationTxts.getRI(),
                compoundGCInformationTxts.getGcmsSpectra());
        //para comprobacion
        System.out.println("Nombre teng from smiles: "+compoundGCInformationTxts.getCName() +
                "\nNombre que recibo IUPAC: " + compoundInfo.getCompoundName() +
                "\nName:                  : " + name);
        return compoundGC;
    }

    /**
     * From the list of CompoundGCInformationTxt will get all their relevant information from PubChem website
     * @param compoundGCInformationTxts List with the information of the txt file
     * @return CompoundGCAndPossibleErrors a class that has a list of compoundsGC, a map with the
     * 'original index compound' (the one from the txt file) and CompoundGCInformationTxt
     */
    public static CompoundGCAndPossibleErrors getGCMSCloroformiatesFromTxt
            (List<CompoundGCInformationTxt> compoundGCInformationTxts){

        CompoundGCAndPossibleErrors compoundGCAndPossibleErrors = new CompoundGCAndPossibleErrors();

        int i;
        for(i=0; i<compoundGCInformationTxts.size(); i++){
            String formula = "";
            CompoundGC compoundGC = null;

            System.out.println("\nCompouesto busqueda info pubchem: "+(i+1));

            if (!compoundGCInformationTxts.get(i).getCasId().equalsIgnoreCase("NotFound")) {
                //IF IT HAS CASID
                try {
                    compoundGC = getCompoundGCIfCasID(compoundGCInformationTxts.get(i));
                    formula = compoundGC.getFormula();
                } catch (Exception ex){
                    //IF THE CASID DOES NOT EXIST ON PUBCHEM
                    compoundGCAndPossibleErrors.casIdNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                            compoundGCInformationTxts.get(i));
                    try { //IF THE NAME IS FOUND
                        compoundGC = getCompoundGCIfName(compoundGCInformationTxts.get(i));
                        formula = compoundGC.getFormula();

                    } catch (Exception e) { //IF THE NAME IS NOT FOUND
                        compoundGCAndPossibleErrors.nameNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                                compoundGCInformationTxts.get(i));
                        try{ //IF THE INCHI IS FOUND
                            compoundGC = getCompoundGCIfInchi(compoundGCInformationTxts.get(i));
                            formula = compoundGC.getFormula();

                        } catch (Exception exc){ //IF INCHI IS NOT FOUND
                            compoundGCAndPossibleErrors.inchiNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                                    compoundGCInformationTxts.get(i));
                            try{ //IF SMILES IS FOUND
                                compoundGC = getCompoundGCIfSmiles(compoundGCInformationTxts.get(i));
                                formula = compoundGC.getFormula();

                            } catch (IOException ioe) { //IF SMILES IS NOT FOUND
                                String infoError = "\nThe given casId, the name, the inchi, and smiles are not found on PubChem website:"
                                        + "\nThe data will be manually created from:"
                                        + "\nNumListTxt: "+ compoundGCInformationTxts.get(i).getNumberOriginalListTxt()
                                        + "; Name: " + compoundGCInformationTxts.get(i).getCName()
                                        + "; \n\tInchi: " + compoundGCInformationTxts.get(i).getInchi()
                                        + "; \n\tSmiles: " + compoundGCInformationTxts.get(i).getSmiles()
                                        + ": \n" + exc.getMessage();
                                compoundGCAndPossibleErrors.error.add(infoError);
                                compoundGCAndPossibleErrors.smilesNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                                        compoundGCInformationTxts.get(i));
                            }

                            /*String infoError = "The casId, the name, and the inchi are not found on PubChem website:"
                                    + " NumListTxt: "+ compoundGCInformationTxts.get(i).getNumberOriginalListTxt()
                                    + "; Name: " + compoundGCInformationTxts.get(i).getCName()
                                    + ": Inchi: " + compoundGCInformationTxts.get(i).getInchi() + ": " + exc.getMessage();
                            compoundGCAndPossibleErrors.error.add(infoError);
                            compoundGCAndPossibleErrors.inchiNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                                    compoundGCInformationTxts.get(i));*/
                        }

                    }
                }
            //IF THE COMPOUND DOES NOT HAVE CASID ASSIGNED
            }else {
                try { //LOOKS BY NAME
                    compoundGC = getCompoundGCIfName(compoundGCInformationTxts.get(i));
                    formula = compoundGC.getFormula();
                    /*System.out.println("\nCompuesto encontrado por nombre & no casId: "+compoundGCInformationTxts.get(i).getNumberOriginalListTxt()
                        +": "+compoundGC.getCompoundName() + "; CasId: " + compoundGC.getCasId() + "\n");*/

                } catch (Exception e) { //LOOKS BY INCHI IF THERE IS NOT NAME
                    compoundGCAndPossibleErrors.nameNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                            compoundGCInformationTxts.get(i));
                    try{ //IF INCHI IS FOUND
                        compoundGC = getCompoundGCIfInchi(compoundGCInformationTxts.get(i));
                        formula = compoundGC.getFormula();

                    } catch (Exception exc){ //IF INCHI IS NOT FOUND
                        compoundGCAndPossibleErrors.inchiNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                                compoundGCInformationTxts.get(i));
                        try{ //IF SMILES IS FOUND
                            compoundGC = getCompoundGCIfSmiles(compoundGCInformationTxts.get(i));
                            formula = compoundGC.getFormula();

                        } catch (IOException ioe) { //IF SMILES IS NOT FOUND
                            String infoError = "\nThe given casId, the name, the inchi and smiles are not found on PubChem website."
                                    + "\nThe data will be manually created from:"
                                    + "\nNumListTxt: "+ compoundGCInformationTxts.get(i).getNumberOriginalListTxt()
                                    + "; Name: " + compoundGCInformationTxts.get(i).getCName()
                                    + "; \n\tInchi: " + compoundGCInformationTxts.get(i).getInchi()
                                    + "; \n\tSmiles: " + compoundGCInformationTxts.get(i).getSmiles()
                                    + ": \n" + exc.getMessage();
                            compoundGCAndPossibleErrors.error.add(infoError);
                            compoundGCAndPossibleErrors.smilesNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                                    compoundGCInformationTxts.get(i));
                        }
                        /*String infoError = "The casId, the name, and the inchi are not found on PubChem website:"
                                + " NumListTxt: "+ compoundGCInformationTxts.get(i).getNumberOriginalListTxt()
                                + "; Name: " + compoundGCInformationTxts.get(i).getCName()
                                + ": Inchi: " + compoundGCInformationTxts.get(i).getInchi() + ": " + e.getMessage();
                        compoundGCAndPossibleErrors.error.add(infoError);
                        compoundGCAndPossibleErrors.inchiNotFound.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                                compoundGCInformationTxts.get(i));*/
                    }
                }
            }
            if (!formula.equals("")) {
                compoundGCAndPossibleErrors.compoundGC.put(compoundGCInformationTxts.get(i).getNumberOriginalListTxt(),
                        compoundGC);
            }

            try {
                Thread.sleep(850);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
        return compoundGCAndPossibleErrors;
    }

    public static void main(String[] args) {
        //try{
            //List<CompoundGCInformationTxt> compoundGCInformationTxts = getCompoundFromTxt(filexcel2);
            //CompoundGCAndPossibleErrors compoundGCAndPossibleErrors = getGCMSCloroformiatesFromLibraryTxt(compoundGCInformationTxts);

            //Map<Integer, CompoundGC> compoundGCList = compoundGCAndPossibleErrors.getCompoundGC();
            //Map<Integer, CompoundGCInformationTxt> casIdNotfoundMap = compoundGCAndPossibleErrors.getCasIdNotFound();
            //Map<Integer, CompoundGCInformationTxt> nameNotFoundMap = compoundGCAndPossibleErrors.getNameNotFound();

            CompoundGCInformationTxt compoundExample = new CompoundGCInformationTxt();
            /*compoundExample.setCName("Isoleucine, N-(methoxycarbonyl)-, methyl ester");
            compoundExample.setNumberOriginalListTxt(95);*/
            /*compoundExample.setCasId("NotFound");
            compoundExample.setCName("Methoxysuccinic acid, dimethyl ester");
            compoundExample.setNumberOriginalListTxt(15);*/

            /*compoundExample.setCName("Isoleucine, N-(methoxycarbonyl)-, methyl ester");
            compoundExample.setCasId("NotFound");
            compoundExample.setNumberOriginalListTxt(95);
            compoundExample.setInchi("InChI=1S/C9H17NO4/c1-5-6(2)7(8(11)13-3)10-9(12)14-4/h6-7H,5H2,1-4H3,(H,10,12)/t6-,7-/m0/s1");
            */

            compoundExample.setCName("A");
            compoundExample.setCasId("11111");
            compoundExample.setNumberOriginalListTxt(0);
            compoundExample.setInchi("(H,3,4)");
            compoundExample.setSmiles("CC(=O)O");

            String smiles = "CC(=O)O";
        /*try {
            int cid = PubchemRest.getPCIDFromSmiles(smiles);
            System.out.println("CID PRUEBA PUBCHEM: "+cid);
        } catch (IOException e) {
            System.out.println("ERROR: SMILES!!!!");
        }*/

        try {
            CompoundGC cgcex = getCompoundGCIfSmiles(compoundExample);
            System.out.println("CID PRUEBA PUBCHEM: "+cgcex.toString());
        } catch (IOException e) {
            System.out.println("ERROR: IFSMILES" + e.getStackTrace());
        }

        //compoundExample.setCasId("1000344-11-6");
            List<CompoundGCInformationTxt> example = new ArrayList<>();
            example.add(compoundExample);
            //CompoundGCAndPossibleErrors compoundGCAndPossibleErrors = getGCMSCloroformiatesFromLibraryTxt(example);
            CompoundGCAndPossibleErrors compoundGCAndPossibleErrors = getGCMSCloroformiatesFromTxt(example);

            System.out.println("Compound: "+compoundGCAndPossibleErrors.getCompoundGC().get(0));
            //System.out.println(compoundGCAndPossibleErrors.getCompoundGC().get(95).getCompoundName());
            System.out.println("\nCasId not found: "+compoundGCAndPossibleErrors.getCasIdNotFound().get(0));
            //compoundGCAndPossibleErrors.casIdNotFound
            System.out.println("\nName not found: "+compoundGCAndPossibleErrors.getNameNotFound().get(0));
            //System.out.println(compoundGCAndPossibleErrors.casIdNotFound);
            System.out.println("\nInchi not found: "+compoundGCAndPossibleErrors.getInchiNotFound().get(0));
            System.out.println("\nSmiles not found: "+compoundGCAndPossibleErrors.getSmilesNotFound().get(0));


            int i;
            List<String> infoError = compoundGCAndPossibleErrors.getError();
            System.err.println("\n\nERROR WARNING: ");
            for(i=0; i<infoError.size(); i++){
                System.err.println(infoError.get(i));
            }

        /*} catch (CompoundNameException e) {
            LOGGER.log(Level.SEVERE, "ERROR: " + e.getMessage(), e);

        } catch (IOException e) {
            //throw new RuntimeException(e);
        }*/
    }
}

