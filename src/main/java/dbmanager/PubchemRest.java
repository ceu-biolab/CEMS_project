/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbmanager;

/**
 *
 * @author maria
 */
import cems_project.Compound;
import cems_project.Identifier;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import constants.Constants;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;

import static constants.Constants.PUBCHEM_ENDPOINT_COMPOUND_NAME;

/**
 *
 * @author ceu
 */
public class PubchemRest {

    public static Identifier getIdentifiersFromInChIPC(String inchi, int retries, int sleep) throws IOException, InterruptedException {
        int rep = 0;
        while (rep <= retries) {
            try {
                return getIdentifiersFromInChIPC(inchi);
            } catch (IOException ioe) {
                if (rep >= retries) {
                    throw ioe;
                }
                Thread.sleep(sleep);
            }
            rep++;
        }
        // Statement never reached
        return null;
    }

    public static Identifier getIdentifiersFromInChIPC(String inchi) throws IOException, NullPointerException {
        Content content = Request.post("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/inchi/property/InChIKey,CanonicalSMILES/JSON").
                bodyForm(Form.form().add("inchi", inchi).build())
                .execute().returnContent();
        String jsonResponseString = content.asString();
        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();

        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        String canonicalSmiles = properties.get("CanonicalSMILES").getAsString();
        String inchi_key = properties.get("InChIKey").getAsString();
        Identifier identifier = new Identifier(inchi, inchi_key, canonicalSmiles, cid);
        return identifier;
    }

    public static Compound getCompoundFromInChIPC(String inchi, Integer compound_id, String name, String casId, Integer cembioId) throws IOException, NullPointerException {
        Content content = Request.post("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/inchi/property/IUPACName,MonoisotopicMass,inchi,InChIKey,CanonicalSMILES,MolecularFormula,XLogP/JSON").
                bodyForm(Form.form().add("inchi", inchi).build())
                .execute().returnContent();
        String jsonResponseString = content.asString();
        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        System.out.println(compound_id + "\t" + "cid = " + cid);
        String IUPACName = null;
        if (properties.has("IUPACName")) {
            IUPACName = properties.get("IUPACName").getAsString();
        }
        String molecularFormula = properties.get("MolecularFormula").getAsString();
        String inchi_key = properties.get("InChIKey").getAsString();
        inchi = properties.get("InChI").getAsString();
        String smiles = properties.get("CanonicalSMILES").getAsString();
        Double logP = null;
        if (properties.has("XLogP")) {
            logP = properties.get("XLogP").getAsDouble();
        }
        Double mass = properties.get("MonoisotopicMass").getAsDouble();
        Integer compound_status = 0;
        Integer compound_type = 0;

        Identifier identifiers = new Identifier(inchi, inchi_key, smiles, cid, cembioId);
        Compound compound = new Compound(compound_id, name, casId, molecularFormula, mass, compound_status, compound_type, logP, identifiers);

        return compound;
    }

    public static Identifier getIdentifiersFromINCHIKEYPC(String inchi_key) throws IOException {
        Content content = Request.post("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/InChIKey/property/inchi,CanonicalSMILES/JSON").
                bodyForm(Form.form().add("inchikey", inchi_key).build())
                .execute().returnContent();
        String jsonResponseString = content.asString();
        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();

        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        String canonicalSmiles = properties.get("CanonicalSMILES").getAsString();
        String inchi = properties.get("InChI").getAsString();
        Identifier identifier = new Identifier(inchi, inchi_key, canonicalSmiles);
        return identifier;
    }

    public static Integer getPCIDFromInchiKey(String inchi_key) throws IOException {
        Content content = Request.post("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/InChIKey/property/inchi,CanonicalSMILES/JSON").
                bodyForm(Form.form().add("inchikey", inchi_key).build())
                .execute().returnContent();
        String jsonResponseString = content.asString();
        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();

        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        return cid;
    }

    public static Identifier getIdentifiersFromSMILESPC(String smiles) throws IOException {
        Content content = Request.post("https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/smiles/property/InChI,inchikey/JSON").
                bodyForm(Form.form().add("smiles", smiles).build())
                .execute().returnContent();
        String jsonResponseString = content.asString();
        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();

        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        String inchi = properties.get("InChI").getAsString();
        String inchi_key = properties.get("InChIKey").getAsString();
        Identifier identifier = new Identifier(inchi, inchi_key, smiles);
        return identifier;
    }

    public static Compound getCompoundFromName(Integer compound_id, String name, String casId, Integer cembioId) throws IOException, IllegalArgumentException {
        String nameForSearch = name.trim();
        nameForSearch = nameForSearch.replaceAll(" ", "%20");
        String uriString = PUBCHEM_ENDPOINT_COMPOUND_NAME + nameForSearch + "/property/IUPACName,MonoisotopicMass,inchi,InChIKey,CanonicalSMILES,MolecularFormula,XLogP/JSON";

        Request request = Request.get(uriString);
        request.addHeader("Connection", "keep-alive");

        Response response = request.execute();
        Content jsonResponse = response.returnContent();
        String jsonResponseString = jsonResponse.asString();

        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        System.out.println(compound_id + "\t" + "cid = " + cid);
        String IUPACName = null;
        if (properties.has("IUPACName")) {
            IUPACName = properties.get("IUPACName").getAsString();
        }
        String molecularFormula = properties.get("MolecularFormula").getAsString();
        String inchi_key = properties.get("InChIKey").getAsString();
        String inchi = properties.get("InChI").getAsString();
        String smiles = properties.get("CanonicalSMILES").getAsString();
        Double logP = null;
        if (properties.has("XLogP")) {
            logP = properties.get("XLogP").getAsDouble();
        }
        Double mass = properties.get("MonoisotopicMass").getAsDouble();
        Integer compound_status = 0;
        Integer compound_type = 0;

        Identifier identifiers = new Identifier(inchi, inchi_key, smiles, cid, cembioId);
        Compound compound = new Compound(compound_id, name, casId, molecularFormula, mass, compound_status, compound_type, logP, identifiers);

        return compound;
    }

    //ESTA
    public static Compound getCompoundFromName(String name) throws IOException, IllegalArgumentException {

        String nameForSearch = name.trim();
        nameForSearch = nameForSearch.replaceAll(" ", "%20");
        String uriString = PUBCHEM_ENDPOINT_COMPOUND_NAME + nameForSearch + "/property/IUPACName,MonoisotopicMass,inchi,InChIKey,CanonicalSMILES,MolecularFormula,XLogP/JSON";

        Request request = Request.get(uriString);
        request.addHeader("Connection", "keep-alive");

        Response response = request.execute();
        Content jsonResponse = response.returnContent();
        String jsonResponseString = jsonResponse.asString();

        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        System.out.println("cid = " + cid);
        String IUPACName = null;
        if (properties.has("IUPACName")) {
            IUPACName = properties.get("IUPACName").getAsString();
        }
        String molecularFormula = properties.get("MolecularFormula").getAsString();
        String inchi_key = properties.get("InChIKey").getAsString();
        String inchi = properties.get("InChI").getAsString();
        String smiles = properties.get("CanonicalSMILES").getAsString();
        Double logP = null;
        if (properties.has("XLogP")) {
            logP = properties.get("XLogP").getAsDouble();
        }
        Double mass = properties.get("MonoisotopicMass").getAsDouble();
        String casId = null;
        Integer compound_id = 0;
        Integer compound_status = 0;
        Integer compound_type = 0;

        Identifier identifiers = new Identifier(inchi, inchi_key, smiles, cid);
        Compound compound = new Compound(compound_id, name, casId, molecularFormula, mass, compound_status, compound_type, logP, identifiers);

        return compound;
    }

    public static Identifier getIdentifierFromName(String name) throws IOException {

        String nameForSearch = name.trim();
        nameForSearch = nameForSearch.replaceAll(" ", "%20");
        String uriString = PUBCHEM_ENDPOINT_COMPOUND_NAME + nameForSearch + "/property/inchi,InChIKey,CanonicalSMILES/JSON";

        Request request = Request.get(uriString);
        request.addHeader("Connection", "keep-alive");

        Response response = request.execute();
        Content jsonResponse = response.returnContent();
        String jsonResponseString = jsonResponse.asString();

        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer pc_id = properties.get("CID").getAsInt();
        String inchi_key = properties.get("InChIKey").getAsString();
        String inchi = properties.get("InChI").getAsString();
        String smiles = properties.get("CanonicalSMILES").getAsString();

        Identifier identifier = new Identifier(inchi, inchi_key, smiles, pc_id);

        return identifier;
    }

    public static int getPCIDFromName(String name) throws IOException {

        String nameForSearch = name.trim();
        nameForSearch = nameForSearch.replaceAll(" ", "%20");
        String uriString = PUBCHEM_ENDPOINT_COMPOUND_NAME + nameForSearch + "/JSON";
        Request request = Request.get(uriString);
        request.addHeader("Connection", "keep-alive");

        Response response = request.execute();
        Content jsonResponse = response.returnContent();
        String jsonResponseString = jsonResponse.asString();

        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        //System.out.println("cid = " + cid);
        return cid;
    }

    public static Compound getCompoundFromPCID(int pc_id) throws IOException {
        String uriString = Constants.PUBCHEM_ENDPOINT_COMPOUND + pc_id + "/property/IUPACName,MonoisotopicMass,inchi,InChIKey,CanonicalSMILES,MolecularFormula,XLogP/JSON";
        Request request = Request.get(uriString);
        request.addHeader("Connection", "keep-alive");

        Response response = request.execute();
        Content jsonResponse = response.returnContent();
        String jsonResponseString = jsonResponse.asString();

        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
        JsonObject properties = jsonrepsonse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
        Integer cid = properties.get("CID").getAsInt();
        String IUPACName = null;
        if (properties.has("IUPACName")) {
            IUPACName = properties.get("IUPACName").getAsString();
        }
        String molecularFormula = properties.get("MolecularFormula").getAsString();
        String inchi_key = properties.get("InChIKey").getAsString();
        String inchi = properties.get("InChI").getAsString();
        String smiles = properties.get("CanonicalSMILES").getAsString();
        Double logP = null;
        if (properties.has("XLogP")) {
            logP = properties.get("XLogP").getAsDouble();
        }
        Double mass = properties.get("MonoisotopicMass").getAsDouble();
        String casId = null;
        Integer compound_id = 0;
        Integer compound_status = 0;
        Integer compound_type = 0;

        Identifier identifiers = new Identifier(inchi, inchi_key, smiles, cid);
        Compound compound = new Compound(compound_id, IUPACName, casId, molecularFormula, mass, compound_status, compound_type, logP, identifiers);

        return compound;
    }

    /**
     * @param pc_id
     * @return
     * @throws IOException
     */
    public static Compound getParentCompoundFromPCID(int pc_id) throws IOException {
        String uriString = Constants.PUBCHEM_ENDPOINT_COMPOUND + pc_id + "/cids/JSON?cids_type=parent";
        Request request = Request.get(uriString);
        request.addHeader("Connection", "keep-alive");

        Response response = request.execute();
        Content jsonResponse = response.returnContent();
        String jsonResponseString = jsonResponse.asString();

        JsonObject jsonrepsonse = JsonParser.parseString(jsonResponseString).getAsJsonObject();
        //try{
        Integer parentPC_ID = jsonrepsonse.get(("IdentifierList")).getAsJsonObject().get("CID").getAsInt();

        // catch propertyNotFound
        // HACER LO QUE QUERAMOS (NULL? EXCEPTION? HIJO?)
        //System.out.println("id del padre: " + parentPC_ID);
        //Lanzar execepción si no encuentra un cid del padre
        Compound c = PubchemRest.getCompoundFromPCID(parentPC_ID);
        return c;

    }

    //----------------------------------------------------------------------------------------

    /**
     * Este método recibe una lista de compounds y mediante accesos a pubchem
     * accede a la inchi y el pc_id a través del nombre de cada compuesto.
     *
     * @param simpleCompounds Lista de compounds (leídos del excel del Cambio)
     * @return lista de compounds con toda la info sacada de pubchem
     */
    public static List<Compound> getCompsFromNames(List<Compound> simpleCompounds) {
        //int cid;
        Compound newComp;
        List<Compound> comp = new LinkedList<Compound>();
        for (Compound c : simpleCompounds) {
            try {

                //consultar el pc_id del compound y su inchi
                newComp = getCompoundFromName(c.getCompound_id(), c.getCompoundName(), c.getCasId(), c.getIdentifiersOwn().getCembio_id());
                //creamos los compounds con toda la info completa
                comp.add(newComp);

            } catch (IOException | IllegalArgumentException ex) {
                System.out.println("Ha salido mal");
                Identifier i = new Identifier("REVISAR");
                newComp = new Compound(c.getCompound_id(), c.getCompoundName(), c.getCasId(), i);
                comp.add(newComp);
                Logger.getLogger(PubchemRest.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return comp;
    }

    /**
     * Este método recibe una lista de compounds y mediante accesos a pubchem
     * accede a la inchi de sus padres mediante el pc_id de cada uno de ellos.
     *
     * @param compounds Lista de compounds hijos
     * @return lista de compuestos padre
     */
    public static List<Compound> getParentsFromChildComps(List<Compound> compounds) {
        // es mejor que devuelva a los padres porque así comparamos los ids
        //int cid;
        Compound compHijo;
        List<Compound> padres = new LinkedList<>();
        for (Compound c : compounds) {
            try {
                //si previamente no hemos encontrado el compuesto hijo, no vamos a poder buscar al padre: lo ponemos todo a null
                if (c.getIdentifiersOwn().getPc_id() == null) {
                    System.out.println("No ha encontrado el hijo, asi que no puede buscar el padre");
                    Identifier i = new Identifier("No se puede buscar al padre", null);
                    Compound p = new Compound(i);
                    padres.add(p);
                    //nos vamos al siguiente compuesto de la lista
                    continue;
                }

                //buscar el padre
                Compound compPadre = getParentCompoundFromPCID(c.getIdentifiersOwn().getPc_id());
                padres.add(compPadre);
                //System.out.println("parent: " + comp);

            } catch (IOException ex) {
                if(c.getIdentifiersOwn().getPc_id() != null){
                    Compound compPadre = c;
                    padres.add(compPadre);
                }

                Logger.getLogger(PubchemRest.class.getName()).log(Level.SEVERE, null, ex);

            }
        }
        return padres;
    }

    public static List<Compound> getCompoundsFromInchis(List<Compound> compounds) {
        Identifier i;
        List<Compound> comps = new LinkedList<Compound>();
        for (Compound c : compounds) {
            try {
//              getIdentifiersFromInChIPC para obtener el Identifier completo a partir de la inchi
                i = getIdentifiersFromInChIPC(c.getIdentifiersOwn().getInchi());
//                System.out.println(i);

                Identifier i_final;
                if (c.getIdentifiersOwn().getHmdb_id() == null) {
                    i_final = new Identifier(i.getInchi(), i.getInchi_key(), i.getSmiles(), i.getPc_id(), null, null);
                } else {
                    i_final = new Identifier(i.getInchi(), i.getInchi_key(), i.getSmiles(), i.getPc_id(), c.getIdentifiersOwn().getHmdb_id(), null);
                }

                Compound p = new Compound(c.getCompound_id(), c.getCompoundName(), c.getCasId(), i_final);
                comps.add(p);

            } catch (IOException | NullPointerException ex) {
                System.out.println("Ha salido mal");
                Identifier iden = new Identifier("REVISAR");
                Compound newComp = new Compound(c.getCompound_id(), c.getCompoundName(), c.getCasId(), iden);
                comps.add(newComp);
            }
        }
        return comps;
    }

    public static void main(String[] args) {
        Identifier i = new Identifier("InChI=1S/C7H14N2O3/c1-2-9-6(10)4-3-5(8)7(11)12/h5H,2-4,8H2,1H3,(H,9,10)(H,11,12)/t5-/m0/s1");
        Compound c = new Compound("L-Theanine ", i);

        try {
//          getIdentifiersFromInChIPC para obtener el Identifier completo a partir de la inchi
            i = getIdentifiersFromInChIPC(c.getIdentifiersOwn().getInchi());
            System.out.println(i);

//          getPCIDFromInchiKey para obtener el id
            Integer cid = getPCIDFromInchiKey(i.getInchi_key());
            System.out.println(cid);

//          getParentCompoundFromPCID y ya sacar la inchi del padre
            Compound parent = getParentCompoundFromPCID(cid);
            System.out.println(parent);
        } catch (IOException ex) {
            System.out.println("Ha salido mal");
            i = new Identifier("REVISAR");
            Compound comp = new Compound(c.getCompoundName(), i);
            System.out.println(comp);
            Logger.getLogger(PubchemRest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    public static void main(String[] args) {
////        Compound c = new Compound("DL-α-Hydroxycaproic acid");
//        Compound c = new Compound("Acetyl-L-carnitine hydrochloride");
//
//        try {
//            System.out.println(c);
////            Compound comp = getCompoundFromName(c.getName());
////            System.out.println(comp);
//            int id = getPCIDFromName(c.getName());
//            System.out.println(id);
//            System.out.println("Hola");
//            Compound comp = getParentCompoundFromPCID(id);
//            System.out.println(comp);
//        } catch (IOException ex) {
//            System.out.println("Ha salido mal");
//            Identifier i = new Identifier("REVISAR");
//            Compound comp = new Compound(c.getName(), i);
//            System.out.println(comp);
//            Logger.getLogger(PubchemRest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    public static void main(String[] args) {
//        try {
//            String inchiInput = "InChI=1S/C46H78O4/c1-3-4-5-6-7-8-9-10-11-12-14-19-22-25-28-31-34-37-40-43-46(49)50-44(2)41-38-35-32-29-26-23-20-17-15-13-16-18-21-24-27-30-33-36-39-42-45(47)48/h4-5,7-8,10-11,14,19,25,28,34,37,44H,3,6,9,12-13,15-18,20-24,26-27,29-33,35-36,38-43H2,1-2H3,(H,47,48)/b5-4-,8-7-,11-10-,19-14-,28-25-,37-34-";
//            String inchiKeyInput = "WSRNVQBHNOZMJH-YWCHUICKSA-N";
//            String smilesInput = "CCC=CCC=CCC=CCC=CCC=CCC=CCCC(=O)OC(C)CCCCCCCCCCCCCCCCCCCCCC(=O)O";
//            Identifier identifier = getIdentifiersFromInChIPC(inchiInput);
//            Integer cid = getPCIDFromInchiKey(inchiKeyInput);
//            String canonicalSmiles = identifier.getSmiles();
//            String InchiKey = identifier.getInchi_key();
//            System.out.println(cid);
//            System.out.println(canonicalSmiles);
//            System.out.println(InchiKey);
//
//            if (cid == 134777005) {
//                System.out.println("Test REST PUBCHEM PASSED");
//            } else {
//                System.out.println("FAILED TEST REST PUBCHEM. CHECK THE CONNECTION INTERNET AND THE METHOD");
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(PubchemRest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}