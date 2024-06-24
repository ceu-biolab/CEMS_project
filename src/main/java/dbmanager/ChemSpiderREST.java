/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbmanager;

import cems_project.Compound;
import cems_project.Identifier;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import static constants.Constants.*;
import exceptions.WrongRequestException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Form;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpResponse;

/**
 *
 * @author maria
 */
public class ChemSpiderREST {

    public static String getINCHIKeyFromInchi(String inchi) throws IOException, WrongRequestException {
        try {
            Content content = Request.post(CHEMSPIDER_SERVICE_INCHI_TO_INCHIKEY).
                    bodyForm(Form.form().add("inchi", inchi).build())
                    .execute().returnContent();
            String responseString = content.asString();
            // String newLine = System.getProperty("line.separator");
            String newLine = "\r\n";
            String htmlToRemove1 = "(.)*" + newLine;
            String htmlToRemove2 = "<string xmlns=\"http://www.chemspider.com/\">";
            String htmlToRemoveEnd = "</string>";
            String inchi_key = responseString.replaceFirst(htmlToRemove1, "");
            inchi_key = inchi_key.replace(htmlToRemove2, "").replace(htmlToRemoveEnd, "");
            return inchi_key;
        } catch (HttpResponseException re) {
            throw new WrongRequestException("Check Inchi to generate the inchi key");
        }
    }

    public static String getSMILESFromInchi(String inchi) throws IOException, WrongRequestException {
        try {
            Content content = Request.post(CHEMSPIDER_SERVICE_INCHI_TO_SMILES).
                    bodyForm(Form.form().add("inchi", inchi).build())
                    .execute().returnContent();
            String responseString = content.asString();

            String newLine = System.getProperty("line.separator");
            String htmlToRemove1 = "(.)*" + newLine;
            String htmlToRemove2 = "<string xmlns=\"http://www.chemspider.com/\">";
            String htmlToRemoveEnd = "</string>";
            String smiles = responseString.replaceFirst(htmlToRemove1, "");
            smiles = smiles.replace(htmlToRemove2, "").replace(htmlToRemoveEnd, "");
            return smiles;
        } catch (HttpResponseException re) {
            throw new WrongRequestException("Check Inchi to generate SMILES");
        }
    }

    public static String getMolFromInchi(String inchi) throws IOException, WrongRequestException {
        Response response = Request.post(CHEMSPIDER_SERVICE_INCHI_TO_MOL).
                bodyForm(Form.form().add("inchi", inchi).build())
                .execute();
        HttpResponse returnResponse = response.returnResponse();
        Integer code = returnResponse.getCode();

        if (code == 200) {
            ClassicHttpResponse classicResponse = (ClassicHttpResponse) returnResponse;

            InputStream contentStream = classicResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(contentStream));
            StringBuilder result = new StringBuilder();
            boolean flag = false;
            String newLine = System.getProperty("line.separator");
            // skip two lines
            reader.readLine();
            reader.readLine();
            for (String line; (line = reader.readLine()) != null;) {
                result.append(flag ? newLine : "").append(line);
                flag = true;
            }
            String molFile = result.toString();

            String htmlToRemoveEnd = "</string>";
            String inchi_key = molFile.replace(htmlToRemoveEnd, "");
            return inchi_key;
        }
        throw new WrongRequestException("Wrong inchi to generate Mol");
    }

    public static String getINCHIFromInchiKey(String inchiKey) throws IOException, WrongRequestException {
        try {
            Content content = Request.post(CHEMSPIDER_SERVICE_INCHIKEY_TO_INCHI).
                    bodyForm(Form.form().add("inchi_key", inchiKey).build())
                    .execute().returnContent();
            String responseString = content.asString();
            String newLine = System.getProperty("line.separator");
            String htmlToRemove1 = "(.)*" + newLine;
            String htmlToRemove2 = "<string xmlns=\"http://www.chemspider.com/\">";
            String htmlToRemoveEnd = "</string>";
            String inchi = responseString.replaceFirst(htmlToRemove1, "");
            inchi = inchi.replace(htmlToRemove2, "").replace(htmlToRemoveEnd, "");
            return inchi;

        } catch (HttpResponseException re) {
            throw new WrongRequestException("INCHI KEY NOT FOUND");
        }
    }

    public static String getMolFromInchiKey(String inchiKey) throws IOException, WrongRequestException {
        Response response = Request.post(CHEMSPIDER_SERVICE_INCHIKEY_TO_MOL).
                bodyForm(Form.form().add("inchi_key", inchiKey).build())
                .execute();
        HttpResponse returnResponse = response.returnResponse();
        Integer code = returnResponse.getCode();

        if (code == 200) {
            ClassicHttpResponse classicResponse = (ClassicHttpResponse) returnResponse;

            InputStream contentStream = classicResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(contentStream));
            StringBuilder result = new StringBuilder();
            boolean flag = false;
            String newLine = System.getProperty("line.separator");
            // skip two lines
            reader.readLine();
            reader.readLine();
            for (String line; (line = reader.readLine()) != null;) {
                result.append(flag ? newLine : "").append(line);
                flag = true;
            }
            String molFile = result.toString();

            String htmlToRemoveEnd = "</string>";
            String inchi_key = molFile.replace(htmlToRemoveEnd, "");
            return inchi_key;
        }
        throw new WrongRequestException("Wrong inchi to generate Mol");
    }

    public static Compound getCompoundFromName(String name) throws IOException {
        name = name.replace(" ", "%20");
        name = name.replace(" ", "%20");
        String uriString = PUBCHEM_ENDPOINT_COMPOUND_NAME + name + "/property/IUPACName,MonoisotopicMass,inchi,InChIKey,CanonicalSMILES,MolecularFormula,XLogP/JSON";

        Request request = Request.get(uriString);
        request.addHeader("Connection", "keep-alive");

        Response response = request.execute();
        Content content = response.returnContent();
        String responseString = content.asString();

        JsonObject jsonResponse = JsonParser.parseString(responseString).getAsJsonObject();
        JsonObject properties = jsonResponse.get(("PropertyTable")).getAsJsonObject().get("Properties").getAsJsonArray().get(0).getAsJsonObject();
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

        Identifier identifiers = new Identifier(inchi, inchi_key, smiles);
        Compound compound = new Compound(compound_id, IUPACName, casId, molecularFormula, mass, compound_status, compound_type, logP, identifiers);

        return compound;
    }

    public static void main(String[] args) {
        try {
            String inchi_input = "InChI=1S/C24H27N3O2/c1-7-24(5,6)21-18(13-20-23(29)25-15(4)22(28)27-20)17-11-10-16(9-8-14(2)3)12-19(17)26-21/h7-8,10-13,26H,1,4,9H2,2-3,5-6H3,(H,25,29)(H,27,28)/b20-13+";
            String inchi_key_input = "DTTXMEFLUMXFTB-NASQKTNHSA-N";
            try {
                String inchi_key = getINCHIKeyFromInchi(inchi_input);
                System.out.println(inchi_key);
            } catch (WrongRequestException ex) {
                System.out.println("ex: " + ex);
            }
            try {
                String smiles = getSMILESFromInchi(inchi_input);
                System.out.println(smiles);
            } catch (WrongRequestException ex) {
                System.out.println("ex: " + ex);
            }
            String mol;
            try {
                mol = getMolFromInchi(inchi_input);
                System.out.println(mol);
            } catch (WrongRequestException ex) {
                System.out.println("ex: " + ex);
            }
            try {
                String inchi = getINCHIFromInchiKey(inchi_key_input);
                System.out.println(inchi);
            } catch (WrongRequestException ex) {
                System.out.println("ex: " + ex);
            }
            try {

                mol = getMolFromInchiKey(inchi_key_input);
                System.out.println(mol);
            } catch (WrongRequestException ex) {
                System.out.println("ex: " + ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(ChemSpiderREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
