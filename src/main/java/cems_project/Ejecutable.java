/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cems_project;

import dbmanager.ChemSpiderREST;
import dbmanager.ClassyFire;
import dbmanager.PubchemRest;
import exceptions.CompoundNotClassifiedException;
import exceptions.WrongRequestException;
import patternFinders.PatternFinder;
import patternFinders.RegexInChI;

import java.io.IOException;
import java.util.*;

/**
 * @author maria
 */
public class Ejecutable {

    public static final Set<String> subClassNoStereoChemistry = new HashSet<String>();

    static {
        subClassNoStereoChemistry.add("Amino acids, peptides, and analogues");
        subClassNoStereoChemistry.add("Carbohydrates and carbohydrate conjugates");
    }

    public static final Set<String> classNoStereoChemistry = new HashSet<String>();

    static {
        classNoStereoChemistry.add("Carboxylic acids and derivatives");
        classNoStereoChemistry.add("Steroids and steroid derivatives");
    }

    /**
     * Amino acids, like L-Histidine:
     * Subclass Amino acids, peptides, and analogues
     * <p>
     * Sugars, like glucose and Polyols like Erythritol
     * Subclass Carbohydrates and carbohydrate conjugates
     * <p>
     * Small organic acids like fumaric acid (if carbon count <8)
     * Class Carboxylic acids and derivatives
     * <p>
     * Steroids and bile acids such as ursocholic acid:
     * Class Steroids and steroid derivatives
     * <p>
     * Any compound starting by D-, L-, DL-, (+, (-, (R, (S, cis, trans, (E, (Z, meso.
     * Deuterated or 13C labeled compounds, containing “d” or “13C” or “15N”  in the name or formula
     *
     * @param c
     * @return wether the compound c should be checked taking into account the stereochemistry or not.
     */
    public static boolean checkStereoChemistry(Compound c) {
        String name = c.getCompoundName().toUpperCase();
        String patternDeuterium = "([dD][0-9]+)";
        boolean containsDeuterium = PatternFinder.containsPattern(name, patternDeuterium);

        if (name.startsWith("D-") || name.startsWith("L-") || name.startsWith("DL-") || name.startsWith("(+") || name.startsWith("(-")
                || name.startsWith("(R") || name.startsWith("(S") || name.contains("CIS") || name.contains("TRANS")
                || name.startsWith("(E") || name.startsWith("(Z") || name.contains("MESO")) {
            return true;
        } else if (name.contains("15N") || name.contains("13C")) {
            return true;
        } else if (containsDeuterium) {
            return true;
        }

        // get classification from classyfire
        String inchi = c.getIdentifiersParent().getInchi();
        String inchiKey = null;
        try {
            inchiKey = ChemSpiderREST.getINCHIKeyFromInchi(inchi);
        } catch (IOException e) {
            System.out.println("Check INCHI KEY FOR COMPOUND " + c.getCompound_id());
        } catch (WrongRequestException e) {
            System.out.println("Check INCHI KEY FOR COMPOUND " + c.getCompound_id());
        }
        try {
            ClassyfireClassification classyfireClassification = ClassyFire.getClassificationFromClassyFire(inchi, inchiKey);
            if(classyfireClassification == null)
            {
                return false;
            }
            Thread.sleep(1000);
            String ownClass = classyfireClassification.ownClass();
            String subClass = classyfireClassification.subClass();
            if (subClassNoStereoChemistry.contains(subClass)) {
                return true;
            } else if (classNoStereoChemistry.contains(ownClass)) {
                if (ownClass.equals("Carboxylic acids and derivatives")) {
                    String formula = RegexInChI.getFormulaFromInChI(inchi);
                    String patternCarbons = "C[1-7]?[A-Za-z]";
                    boolean carbonsLowerThan8 = PatternFinder.containsPattern(formula, patternCarbons);
                    if (carbonsLowerThan8) {
                        return true;
                    }
                } else {
                    return true;
                }
            }

        } catch (CompoundNotClassifiedException e) {
            System.out.println("NOT CLASSIFIED Check PARENT compound for classification " + c.getCompound_id());
        } catch (IOException e) {
            System.out.println("IOEXCEPTION Check PARENT compound for classification " + c.getCompound_id());
        } catch (IllegalStateException ise) {
            System.out.println("ILLEGAL STATE Check PARENT compound for classification " + c.getCompound_id());
        } catch (InterruptedException e) {
            System.out.println("Error sleeping the method");
        }

        // TO DO
        return false;
    }


    // MAIN PARA RELLENAR EXCELS
//    public static void main(String[] args) {
//        // getInchisAndPCIdsFromCEMBIOList
//        //getParentInChIsFromPCIdsCEMBIOList();
//        // ComercialList();
//
//    }

    public static void getInchisAndPCIdsFromCEMBIOList() {
        List<Compound> compsPC = Fichero.leerCEMBIOLIST();

        Fichero.escribirPCIDs(compsPC);

    }

    public static void getParentInChIsFromPCIdsCEMBIOList() {

        List<Compound> compsPC = Fichero.readCEMBIOCuratedFile();

        // obtenemos las inchis de los padres
        List<Compound> padres = PubchemRest.getParentsFromChildComps(compsPC);
//        for (Compound c : padres) {
//            System.out.println(c);
//        }

        //escribimos compuestos en el excel
        Fichero.escribirCEMBIOLIST(padres, compsPC);
    }

    public static void ComercialList() {
        List<Compound> compsLeidos = Fichero.leerCOMERCIALLIST();
//        for (Compound c : compsLeidos) {
//            System.out.println(c);
//        }

        List<Compound> compsPC;

        // actualizamos los compuestos para tener su inchi y su pc_id
        System.out.println("Buscar su info");
        compsPC = PubchemRest.getCompoundsFromInchis(compsLeidos);
        //estos compounds tienen el nombre de la IUPAC (de sus parámetros solo queremos el pc_id y la inchi)
//        for (Compound c : compsPC) {
//            System.out.println(c);
//        }

        // obtenemos las inchis de los padres
        System.out.println("Buscar padres");
        List<Compound> padres = PubchemRest.getParentsFromChildComps(compsPC);
//        for (Compound c : padres) {
//            System.out.println(c);
//        }
//
//        //escribimos todo en el excel
        System.out.println("Escribir excel");
        Fichero.escribirCOMERCIALLIST(padres, compsPC);
    }
//


    //MAIN PARA COMAPARAR LA COMERCIAL Y LA CEMBIO INDEPENDIENTES (No he hecho un método)
//    public static void main(String[] args) {
//
//        List<String> parentInchis = new LinkedList();
//        List<String> repetidos = new ArrayList<>();
//        System.out.println(repetidos.size());
//        List<String> parentInchiMainParts = new ArrayList<>();
//        System.out.println("Leer fichero");
////        List<Compound> comps = Fichero.leerInchisExcelCembio();
//        List<Compound> comps = Fichero.leerInchisExcelComercial();
////        System.out.println(comps);
//        for (Compound c : comps) {
//            String inchi = c.getIdentifiersParent().getInchi();
//
//            parentInchis.add(inchi);
//            try {
//                String mainPartInchi = RegexInChI.getMainPart(inchi);
//                parentInchiMainParts.add(mainPartInchi);
//            } catch (IllegalArgumentException iae) {
//                parentInchiMainParts.add(inchi);
//            }
//        }
//        //System.out.println(inchis);
//        //System.out.println(inchisMain);
//
//        //------------------------------------------------------
//        //rellenamos la lista de repetidos con 0:
//        for (String cop : parentInchiMainParts) {
//            repetidos.add("0");
//        }
//
//        int counter;
//        for (counter = 0; counter < comps.size(); counter++) {
//            String parentInchiMainPart = parentInchiMainParts.get(0);
//            String parentInchiFull = parentInchis.get(0);
//            //elimino el primer elemento de la lista porque es el que queremos comparar
//            parentInchiMainParts.remove(0);
//            parentInchis.remove(0);
//            if (repetidos.get(counter).equals("0")) {   //si todavia no hemos comparado esa string hacemos todas las comparaciones, si ya la comparamos, pasamos al siguiente
//                if (parentInchiMainPart.equals("No se puede buscar al padre")) {
//                    repetidos.set(counter, "---");
////                    System.out.println("Lista repetidos");
////                    System.out.println(repetidos);
////                counter++;
//                    continue;
//                }
//                if (parentInchiMainParts.contains(parentInchiMainPart)) {
//                    Compound compound = comps.get(counter);
//                    boolean checkStereoChemistry = checkStereoChemistry(compound);
//
//                    // if check stereochemistry, then we will look the full inchi, otherwise, only the main part
//                    if (!checkStereoChemistry) {
//                        int pos = 0;
//                        for (String parentInchiMainPartAux : parentInchiMainParts) {
//                            if (parentInchiMainPartAux.equals(parentInchiMainPart)) {
//                                int lineExcel = pos + counter + 1;
//                                Compound compoundToCompare = comps.get(lineExcel);
//                                checkStereoChemistry = checkStereoChemistry(compoundToCompare);
//                                if (!checkStereoChemistry) {
//                                    String stringRepetido1 = "Igual que Id = " + compound.getCompound_id();
//                                    String stringRepetido2 = "Igual que Id = " + compoundToCompare.getCompound_id();
//                                    repetidos.set(lineExcel, stringRepetido1);
//                                    repetidos.set(counter, stringRepetido2);
//                                } else {
//                                    String parentInchiFullAux = compoundToCompare.getIdentifiersParent().getInchi();
//                                    if (parentInchiFullAux.equals(parentInchiFull)) {
//                                        String stringRepetido1 = "Igual que Id = " + compound.getCompound_id();
//                                        String stringRepetido2 = "Igual que Id = " + compoundToCompare.getCompound_id();
//                                        repetidos.set(lineExcel, stringRepetido1);
//                                        repetidos.set(counter, stringRepetido2);
//                                    }
//                                }
//                            }
//                            pos++;
//                        }
//                    } else {
//                        int pos = 0;
//                        for (String parentInchiFullAux : parentInchis) {
//                            if (parentInchiFullAux.equals(parentInchiFull)) {
//                                int lineExcel = pos + counter + 1;
//                                String stringRepetido1 = "Igual que Id = " + compound.getCompound_id();
//                                String stringRepetido2 = "Igual que Id = " + comps.get(lineExcel).getCompound_id();
//                                repetidos.set(lineExcel, stringRepetido1);
//                                repetidos.set(counter, stringRepetido2);
//                            }
//                            pos++;
//                        }
//                    }
//                } else {
//                    //no esta repetido
////                    System.out.println("no esta repe");
//                    repetidos.set(counter, "No esta repetido");
////                    System.out.println("Lista repetidos");
////                    System.out.println(repetidos);
//                }
//            }
//        }
//
//        // System.out.println(repetidos);
//
//        System.out.println("Escribir en fichero");
////        Fichero.escribirInchisCembio(repetidos);
//        Fichero.escribirInchisComercial(repetidos);
//    }


    // MAIN PARA COMPARAR AMBAS LISTAS ENTRE ELLAS
    public static void main(String[] args) {

        List<String> fullInchisCembio = new ArrayList<>();
        List<String> fullInchisComercial = new LinkedList<>();
        List<String> repetidos = new ArrayList<>();
//        System.out.println(repetidos.size());
        List<String> parentInchiMainPartsCembio = new ArrayList<>();
        List<String> parentInchiMainPartsComercial = new ArrayList<>();

//        System.out.println("Leer fichero");
        List<Compound> compsCembio = Fichero.leerInchisExcelCembio();
        List<Compound> compsComercial = Fichero.leerInchisExcelComercial();

//        System.out.println(compsCembio);
//        System.out.println(compsComercial);
        for (Compound c : compsCembio) {
            String parentInchiCembio = c.getIdentifiersParent().getInchi();
            fullInchisCembio.add(parentInchiCembio);
            try {
                String mainPartInchi = RegexInChI.getMainPart(parentInchiCembio);
                parentInchiMainPartsCembio.add(mainPartInchi);
            } catch (IllegalArgumentException iae) {
                parentInchiMainPartsCembio.add(parentInchiCembio);
            }
        }
        for (Compound c : compsComercial) {
            String parentInchiComercial = c.getIdentifiersParent().getInchi();
            fullInchisComercial.add(parentInchiComercial);
            try {
                String mainPartInchi = RegexInChI.getMainPart(parentInchiComercial);
                parentInchiMainPartsComercial.add(mainPartInchi);
            } catch (IllegalArgumentException iae) {
                parentInchiMainPartsComercial.add(parentInchiComercial);
            }
        }
//        System.out.println(inchis);


        //------------------------------------------------------
        //rellenamos la lista de repetidos con 0:
        for (String cop : parentInchiMainPartsCembio) {
            repetidos.add("0");
        }

//        List<String> cambiantes = inchisMain;
        int counter;
        for (counter = 0; counter < compsCembio.size(); counter++) {
            String mainPartInchiCEMBIO = parentInchiMainPartsCembio.get(counter);
            String fullInchiCEMBIO = fullInchisCembio.get(counter);
            Compound compoundCEMBIO = compsCembio.get(counter);
//            System.out.println("A comparar");
//            System.out.println(inchiComp);

            if (mainPartInchiCEMBIO.equals("No se puede buscar al padre")) {
//                System.out.println("no padre");
                repetidos.set(counter, "---");
//                System.out.println("Lista repetidos");
//                System.out.println(repetidos);
                continue;
            }

            if (parentInchiMainPartsComercial.contains(mainPartInchiCEMBIO)) {
                boolean checkStereoChemistry = checkStereoChemistry(compoundCEMBIO);

//                    // if check stereochemistry, then we will look the full inchi, otherwise, only the main part
                if (!checkStereoChemistry) {
                    int pos = parentInchiMainPartsComercial.indexOf(mainPartInchiCEMBIO);
                    Compound commercialCompoundToCompare = compsComercial.get(pos);
                    checkStereoChemistry = checkStereoChemistry(commercialCompoundToCompare);
                    if (!checkStereoChemistry) {
                        String stringRepetido = "Igual que Id = " + commercialCompoundToCompare.getCompound_id();
                        repetidos.set(counter, stringRepetido);
                    } else {
                        String fullInchiCommercial = commercialCompoundToCompare.getIdentifiersParent().getInchi();
                        if (fullInchiCommercial.equals(fullInchiCEMBIO)) {
                            String stringRepetido = "Igual que Id = " + commercialCompoundToCompare.getCompound_id();
                            repetidos.set(counter, stringRepetido);
                        }
                    }
                } else {
                    int pos = parentInchiMainPartsComercial.indexOf(mainPartInchiCEMBIO);
                    Compound commercialCompoundToCompare = compsComercial.get(pos);
                    String fullInchiCommercial = commercialCompoundToCompare.getIdentifiersParent().getInchi();
                    if (fullInchiCommercial.equals(fullInchiCEMBIO)) {
                        String stringRepetido = "Igual que Id = " + commercialCompoundToCompare.getCompound_id();
                        repetidos.set(counter, stringRepetido);
                    }
                }

            } else {
                repetidos.set(counter, "No esta repetido");
            }
        }
        Fichero.escribirInchisCompararAmbos(repetidos);

    }

//
//    public static void main(String[] args) {
//        //imprimimos la lista de metabolitos que tenemos
//        List<Metabolito> metabolitos = Fichero.leerFichero();
//        for (Metabolito m : metabolitos) {
//            System.out.println(m);
//        }
//
//        //conectamos con la database
//        DBManager db = new DBManager();
//        String filename = "resources/connectionData.pass";
//        try {
//            Gson gson = new Gson();
//            String readJSONStr = readStringFromFile(filename);
//            JsonElement element = gson.fromJson(readJSONStr, JsonElement.class);
//            JsonObject jsonObj = element.getAsJsonObject();
//            String dbName = jsonObj.get("db_name").getAsString();
//            String dbUser = jsonObj.get("db_user").getAsString();
//            String dbPassword = jsonObj.get("db_password").getAsString();
//
//            db.connectToDB("jdbc:mysql://localhost/" + dbName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", dbUser, dbPassword);
//
//            //insertamos los metabolitos leidos
//            for (Metabolito m : metabolitos) {
//                db.insertMetabolite(m);
//            }
//
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ioe) {
//            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ioe);
//        }
//    }
}
