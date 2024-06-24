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
public class ConstantQueries {

    public static final String SELECTMETABOLITES = "Select * from metabolites";
    public static final String SELECTMETABOLITESFROMID = "Select * from metabolites where id = ?";

    public static final String SELECTFRAGMENTSFROMID = "Select * from fragments where id_met = ?";

    public static final String INSERTINTOMETABOLITES = "Insert into metabolites (COMPOUND_NAME, FORMULA, MONOISOTOPIC_MASS, M_Z, mt_COMPND, MT_METS, RMT_METS, MT_MES, RMT_MES) VALUES(?,?,?,?,?,?,?,?,?)";
    public static final String INSERTFRAGMENTSFROMID = "Insert into fragments (ID_MET, M_Z, INTENSITY) VALUES (?, ?, ?)";

    //Nuevos:
    public static final String INSERT_CE_EFF_MOB = "INSERT INTO ce_eff_mob(ce_compound_id, ce_exp_prop_id, cembio_id, eff_mobility) VALUES(?, ?, ?, ?)";
    public static final String INSERT_COMPOUNDS = "INSERT INTO compounds (compound_name, formula, mass) VALUES (?, ?, ?)";
    public static final String INSERT_COMP_IDENT = "INSERT INTO compound_identifiers (compound_id, inchi, inchi_key, smiles) VALUES (?, ?, ?, ?)";
    public static final String INSERT_COMP_HMDB = "INSERT INTO compounds_hmdb (hmdb_id, compound_id) VALUES (?, ?)";
    public static final String INSERT_COMP_PC = "INSERT INTO compounds_pc (pc_id, compound_id) VALUES (?, ?)";
    public static final String SELECT_CE_EFF_MOB_ID = "SELECT eff_mob_id FROM eff_mob WHERE compound_id = ? AND eff_mob_exp_prop_id = ?";
    public static final String SELECT_CE_EXP_PROP = "SELECT ce_exp_prop_id FROM ce_experimental_properties WHERE buffer = ? AND temperature = ? AND polarity = ?";
    public static final String INSERT_CE_EXP_PROP_META = "INSERT INTO ce_experimental_properties_metadata" +
            "(ce_eff_mob_id, experimental_mz, ce_identification_level, ce_sample_type," +
            "capillary_length, capillary_voltage, " +
            "rmt_ref_compound_id, absolute_MT, relative_MT, commercial, exp_eff_mob, ionization_mode) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    //Versión 2:

    public static final String INSERT_EFF_MOB = "INSERT INTO eff_mob(compound_id, eff_mob_exp_prop_id, ce_exp_prop_id, cembio_id, eff_mobility) VALUES(?, ?, ?, ?, ?)";
    public static final String SELECT_EFF_MOB_EXP_PROP = "SELECT eff_mob_exp_prop_id FROM eff_mob_experimental_properties WHERE buffer = ? AND temperature = ? AND polarity = ?";
    public static final String INSERT_CE_EXP_PROP = "INSERT INTO ce_experimental_properties" +
            "(eff_mob_exp_prop_id, ce_sample_type, capillary_length, capillary_voltage, ionization_mode," +
            "exp_label) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    public static final String INSERT_CE_EXP_PROP_METADATA = "INSERT INTO ce_experimental_properties_metadata" +
            "(ce_exp_prop_id, compound_id, experimental_mz, ce_identification_level," +
            "rmt_ref_compound_id, absolute_MT, relative_MT, commercial, exp_eff_mob) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_CE_EXP_PROP_ID = "SELECT ce_exp_prop_id FROM ce_experimental_properties WHERE eff_mob_exp_prop_id = ? AND ce_sample_type= ? AND capillary_length = ? AND capillary_voltage = ? AND ionization_mode = ? AND exp_label = ?";

    //Valores capilar y referencia (Antiguos)
    public static final int CAPILLARY_VOLTAGE = 30;
    public static final int CAPILLARY_LENGTH = 1000;
    public static final int REFERENCE_COMPOUND_ID_METS = 180838;
    public static final int REFERENCE_COMPOUND_ID_MES = 73414;


    public static final String INSERT_COMP_CE_PROD_ION = "INSERT INTO compound_ce_product_ion (ion_source_voltage, ce_product_ion_mz, ce_product_ion_intensity, ce_transformation_type, ce_eff_mob_id, compound_id_own) VALUES (?, ?, ?, 'fragment', ?, ?)";
    public static final int ION_SOURCE_VOLTAGE = 200;
}
