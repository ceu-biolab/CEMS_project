/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

/**
 *
 * @author maria
 */
public class Constants {

    // public static String CEFilePath = "C:/Users/maria/Documents/CEU/beca/datos/Tabla_patrones_CEMSnegnegSinProblemas.xlsx";

    public static String RESOURCES_PATH = "src/main/resources/";

    public static String CEFilePath = RESOURCES_PATH + "input_excels/";

//     public static String CEFilePath = RESOURCES_PATH + "input_excels/Tabla_patrones_CEMSnegnegSinProblemas.xlsx";


    public static String CEMBIOLIST = RESOURCES_PATH + "input_compoundlists/CEMBIOLISTReducida.xlsx";

    public static String CEMBIOCURATEDLIST = RESOURCES_PATH + "output/CEMBIOPCsAndInchis.xlsx";
    //CEMBIOLIST1.xlsx
    //CEMBIOLISTReducida.xlsx
    public static String INCHISCEMBIO = RESOURCES_PATH + "input_compoundlists/InchisCEMBIO.xlsx";

    public static String CEMBIOWITHPARENTS = RESOURCES_PATH + "output/CEMBIOWithParents.xlsx";
    //CEMBIOLIST1.xlsx
    //InchisCEMBIO.xlsx
    public static String COMERCIALLIST = RESOURCES_PATH + "input_compoundlists/COMERCIALLISTReducida.xlsx";
    //COMERCIALLIST1.xlsx
    //COMERCIALLISTReducida.xlsx
    public static String INCHISCOMERCIAL = RESOURCES_PATH + "output/COMWithParents.xlsx";
    //COMERCIALLIST1.xlsx
    //InchisCOMERCIAL.xlsx
    public static String OUTPUT_DIRECTORY = RESOURCES_PATH + "output/";
    public static final String PUBCHEM_ENDPOINT_COMPOUND = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/cid/";
    public static final String PUBCHEM_ENDPOINT_COMPOUND_NAME = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/";
    public static final String PUBCHEM_ENDPOINT_INCHI_TO_INCHIKEY_START = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/inchi/";
    public static final String PUBCHEM_ENDPOINT_INCHI_TO_INCHIKEY_END = "/property/InChIKey/JSON";
    public static final String CHEBI_COMPOUND_ENDPOINT = "https://www.ebi.ac.uk/chebi/backend/api/public/compound/";
    public static final String CHEBI_STRUCTURE_SEARCH_ENDPOINT = "https://www.ebi.ac.uk/chebi/backend/api/public/structure_search/";

    public static final String KEGG_PATHWAYS_FROM_COMPOUND = "https://rest.kegg.jp/link/pathway/";

    public static final String CAS_ONLINE_PATH = "https://commonchemistry.cas.org/detail?cas_rn=";

    public static final String FIEHN_TRANSLATOR_SERVICE = "https://cts.fiehnlab.ucdavis.edu/rest/convert/CAS/InChI%20Code/";

    public static final String CAS_RESOURCES_PATH = "src/main/resources/cas/";

    public static final String CLASSYFIRE_ONLINE_NODES_PATH = "http://classyfire.wishartlab.com/";
    public static final String CLASSYFIRE_ONLINE_RESOURCES_PATH = CLASSYFIRE_ONLINE_NODES_PATH + "entities/";
    public static final String CLASSYFIRE_RESOURCES_PATH = RESOURCES_PATH + "CLASSYFIRE/";


}
