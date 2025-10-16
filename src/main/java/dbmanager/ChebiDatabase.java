/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbmanager;

/**
 *
 * @author Alberto Gil de la Fuente
 */

import cems_project.Identifier;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.ChebiException;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static constants.Constants.CHEBI_COMPOUND_ENDPOINT;

/**
 * Class to perform queries against the chebi Database to retrieve information
 * of compounds with the chebi Identifier
 *
 * @author Alberto Gil de la Fuente
 * @version $Revision: 5.1.1.1 $
 * @since Build 4.1.0.0 07-nov-2019
 */
public abstract class ChebiDatabase {

    private static String getOrNull(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }

    /**
     * Get the identifiers from chebi ID using the chebi REST API
     * @param chebiId
     * @return
     * @throws ChebiException
     * @throws IOException
     */
    public static Identifier getIdentifierFromChebiID(int chebiId) throws ChebiException, IOException {
        String chebi_endpoint = CHEBI_COMPOUND_ENDPOINT + chebiId;

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        // Parse JSON
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        JsonObject structure = obj.getAsJsonObject("default_structure");

        if (structure == null) {
            throw new ChebiException("No default_structure found for ChEBI ID " + chebiId);
        }

        // Extract fields
        String smiles = getOrNull(structure, "smiles");
        String inchi = getOrNull(structure, "standard_inchi");
        String inchiKey = getOrNull(structure, "standard_inchi_key");

        return new Identifier(inchi, inchiKey, smiles);

    }

    /**
     * Returns the ASCII name of the chebi ID using the CHEBI REST API
     *
     * @param chebiId
     * @return
     * @throws ChebiException
     * @throws IOException
     */
    public static String getAsciiName(int chebiId) throws ChebiException, IOException {
        String chebi_endpoint = CHEBI_COMPOUND_ENDPOINT + chebiId;

        Content content = Request.get(chebi_endpoint)
                .connectTimeout(Timeout.ofMilliseconds(5000))
                .responseTimeout(Timeout.ofMilliseconds(5000))
                .execute()
                .returnContent();

        String json = content.asString();

        // Parse JSON
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        String ascii_name = obj.get("ascii_name").getAsString();

        if (ascii_name == null) {
            throw new ChebiException("No compound found found for ChEBI ID " + chebiId);
        }
        return ascii_name;
    }

//    public static String getInChIKeyFromChebID(int chebId) throws ChebiWebServiceFault_Exception {
//        try {
//            Entity entity;
//            entity = client.getCompleteEntity("CHEBI:" + Integer.toString(chebId));
//            return entity.getInchiKey();
//        } catch (ChebiWebServiceFault_Exception ex) {
//            Logger.getLogger(ChebiDatabase.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public static String getFormulaFromChebID(int chebId) throws ChebiException, ChebiWebServiceFault_Exception {
//        try {
//            Entity entity;
//            entity = client.getCompleteEntity("CHEBI:" + Integer.toString(chebId));
//            List<DataItem> formulas = entity.getFormulae();
//            if (formulas.size() == 1) {
//                return formulas.get(0).getData();
//            } else if (formulas.isEmpty()) {
//                throw new ChebiException("Formula not available in chebi");
//            } else {
//                System.out.println("chebId: " + chebId);
//                for (DataItem formula : formulas) {
//                    System.out.println("DATA:" + formula.getData());
//                    System.out.println("SOURCE:" + formula.getSource());
//                    System.out.println("Type:" + formula.getType());
//                }
//                return formulas.get(0).getData();
//            }
//        } catch (ChebiWebServiceFault_Exception ex) {
//            Logger.getLogger(ChebiDatabase.class.getName()).log(Level.SEVERE, null, ex);
//            throw ex;
//        }
//    }
//
//    public static Double getMonoIsotopicMassFromChebID(int chebId) throws ChebiWebServiceFault_Exception {
//        try {
//            Entity entity;
//            entity = client.getCompleteEntity("CHEBI:" + Integer.toString(chebId));
//            Double mass = Double.parseDouble(entity.getMonoisotopicMass());
//            return mass;
//        } catch (ChebiWebServiceFault_Exception ex) {
//            Logger.getLogger(ChebiDatabase.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//    }
//
//    public static String getHMDBLink(int chebId) {
//        try {
//            Entity entity;
//            entity = client.getCompleteEntity("CHEBI:" + Integer.toString(chebId));
//            List<DataItem> synonyms = entity.getDatabaseLinks();
//            for (DataItem synonim : synonyms) {
//                System.out.println("DATA:" + synonim.getData());
//                System.out.println("SOURCE:" + synonim.getSource());
//                System.out.println("Type:" + synonim.getType());
//                if (synonim.getType().equals("HMDB accession")) {
//                    return synonim.getData();
//                }
//            }
//        } catch (ChebiWebServiceFault_Exception ex) {
//            Logger.getLogger(ChebiDatabase.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
//        return null;
//    }
//
//    /**
//     *
//     * @param identifiers
//     * @return chebId if it was found.
//     * @throws exceptions.ChebiException
//     */
//    public static Integer getChebiFromIdentifiers(Identifier identifiers) throws ChebiException {
//        Integer chebiIdResult = null;
//        String smiles = identifiers.getSmiles();
//        String inchi_key = identifiers.getInchi_key();
//        String inchi = identifiers.getInchi();
//        if (smiles == null || inchi_key == null) {
//            throw new ChebiException("Wrong identifier sent to chebi");
//        }
//        try {
//            LiteEntityList querySMILESResult = client.getStructureSearch(smiles, StructureType.SMILES, StructureSearchCategory.SIMILARITY, 100, 0.90F);
//            List<LiteEntity> querySMILESList = querySMILESResult.getListElement();
//            for (LiteEntity chebiEntity : querySMILESList) {
//                String chebId = chebiEntity.getChebiId();
//                Entity fullEntity;
//                fullEntity = client.getCompleteEntity(chebId);
//                String inchi_key_from_chebi = fullEntity.getInchiKey();
//                String inchi__from_chebi = fullEntity.getInchi();
//                if ((inchi_key_from_chebi != null && inchi_key_from_chebi.equals(inchi_key)) || (inchi__from_chebi != null && inchi__from_chebi.equals(inchi))) {
//                    return ChebiDatabase.getChebiNumber(chebId);
//                }
//            }
//        } catch (ChebiWebServiceFault_Exception ex) {
//            System.out.println("CHEBI STRUCTURE WRONG: " + identifiers);
//            Logger.getLogger(ChebiDatabase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        throw new ChebiException("ChebId not found");
//    }

    public static Integer getChebiNumber(String chebiId) {
        return Integer.parseInt(chebiId.replaceAll("CHEBI:", ""));
    }
}
