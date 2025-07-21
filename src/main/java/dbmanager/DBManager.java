/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbmanager;

import GCMS.CompoundGC;
import GCMS.GCMS_Peaks;
import cems_project.CEMSExperimentalConditions;
import cems_project.Fragment;
import cems_project.CEMSCompound;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import exceptions.WrongRequestException;
import experimental_properties.BufferType;
import experimental_properties.IonizationModeType;
import experimental_properties.PolarityType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utilities.FileIO.readStringFromFile;

/**
 * @author alberto.gildelafuent
 */

public class DBManager {

    protected Connection connection;
    protected Statement statement;
    /**
     * Method to connect to the database
     *
     * @param bd      JDBC String to coonect -> Example
     *                "jdbc:mysql://localhost/<DATABASE_NAME>/?useSSL=false&serverTimezone=UuseSSLTC
     * @param usuario username of the database -> Example -> Root
     * @param clave   "password of the database" -> example -> password
     */
    public void connectToDB(String bd, String usuario, String clave) {
        try {
            connection = DriverManager.getConnection(bd, usuario, clave);       //getConnection is a static method
            statement = this.connection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Close connection resource
     */
    public void closeConnectionResource() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Close Statement resource
     */
    public void closeStatementResource() {
        if (this.statement != null) {
            try {
                this.statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Close PrepareStatement resource
     */
    public void closePrepareStatementResource(PreparedStatement prep) {
        if (prep != null) {
            try {
                prep.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the compound id from the inchi of a compound
     *
     * @param inchi
     * @return compound_id
     */
    public int getCompoundIdFromInchi(String inchi) {
        String sql = "SELECT compound_id FROM compound_identifiers WHERE inchi LIKE ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, inchi);
            int compound_id = getInt(ps);
            return compound_id;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Return the compound id from the inchiKey of a compound
     *
     * @param inchiKey
     * @return compound_id
     */
    public int getCompoundIdFromInchiKey(String inchiKey) {
        String sql = "SELECT compound_id FROM compound_identifiers WHERE inchi_key LIKE ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, inchiKey);
            int compound_id = getInt(ps);
            return compound_id;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * Inserts the metabolite into the database
     *
     * @param m CEMSCompound
     * @param c CEMSExperimentalConditions
     */
    public void insertMetabolite(CEMSCompound m, CEMSExperimentalConditions c) {

            int compound_id = getCompoundIdFromInchi(m.getINCHI());
            if(compound_id == 0)
            {
                compound_id = getCompoundIdFromInchiKey(m.getINCHIKey());
                if (compound_id == 0) {
                    compound_id = insertCompound(m);
                    insertCompoundIdentifiers(compound_id, m.getINCHI());
                }
            }
            m.setCompound_id(compound_id);
            insertHMDB(compound_id, m.getRefHMDB());
            insertPC(compound_id, m.getRefPubChem());

            Integer eff_mob_id = get_eff_mob_id(m, c);

            // INSERT EFF MOB
            if (eff_mob_id == null) {
                eff_mob_id = insertEffMob(m, c);  //java.sql.SQLIntegrityConstraintViolationException: Duplicate entry
            }

            // INSERT METADATA WITH METH SULFONE AND PARACETAMOL
            insertCeExpPropMet(m, c);

            List<Fragment> fragments = m.getFragments();
            insertCompCeProdIon(compound_id, eff_mob_id, fragments);     //java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row

    }

    /**
     * Insert into the table compounds of the database the CEMSCompound
     *
     * @param m CEMSCompound
     * @return compound_id
     */
    public int insertCompound(CEMSCompound m) {

        int compound_id = 0;
        String sql = ConstantQueries.INSERT_COMPOUNDS;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, m.getCompoundName());
            ps.setString(2, m.getFormula());
            ps.setDouble(3, m.getMonoisotopicMass());
            ps.executeUpdate();                 //insertamos la info

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    compound_id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return compound_id;
    }

    /**
     * Insert into the table compound_identifiers of the database the
     * compound_id, inchi, inchi_key, smiles of the corresponding compound
     *
     * @param compound_id
     * @param inchi
     */
    public void insertCompoundIdentifiers(int compound_id, String inchi) {

        String sql = ConstantQueries.INSERT_COMP_IDENT;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, compound_id);
            ps.setString(2, inchi);
            String key = ChemSpiderREST.getINCHIKeyFromInchi(inchi);
            String smiles = ChemSpiderREST.getSMILESFromInchi(inchi);
            ps.setString(3, key);
            ps.setString(4, smiles);
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioe) {
            System.out.println("ex: " + ioe);
        } catch (WrongRequestException wre) {
            System.out.println(wre);
        }
    }

    /**
     * Insert on the table compounds_hmdb the compound_id and HMBD
     *
     * @param compound_id
     * @param HMDB
     */
    public void insertHMDB(int compound_id, String HMDB) {
        String sql = ConstantQueries.INSERT_COMP_HMDB;
        //"INSERT INTO compounds_hmdb (hmdb_id, compound_id) VALUES (?, ?)"
        try {
            if (HMDB != null) {
                //we just have to add the reference when it exists
                PreparedStatement ps = this.connection.prepareStatement(sql);
                ps.setString(1, HMDB);
                ps.setInt(2, compound_id);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getErrorCode() == 1062) { //code de Duplicate Entry
                System.out.println("No se inserta la referencia HMDB porque ya estaba metida.");
            }
        }
    }

    /**
     * Insert on the table compounds_pc the compound_id and pubchem id of the component
     *
     * @param compound_id
     * @param pc PubChem id
     */
    public void insertPC(int compound_id, Integer pc) {
        String sql = ConstantQueries.INSERT_COMP_PC;
        //"INSERT INTO compounds_pc (pc_id, compound_id) VALUES (?, ?)"

        try {

            if (pc != null) {
                PreparedStatement ps = this.connection.prepareStatement(sql);
                ps.setInt(1, pc);

                ps.setInt(2, compound_id);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getErrorCode() == 1062) { //code de Duplicate Entry
                System.out.println("No se inserta la referencia PubChem porque ya estaba metida.");
            }
        }
    }

    /**
     * @param eff_mob_exp_prop_id
     * @return the id of the ce ef mob exp properties and the compound
     */
    public Integer get_ce_exp_prop_id(int eff_mob_exp_prop_id, CEMSCompound m, CEMSExperimentalConditions c) {
        String sql = ConstantQueries.SELECT_CE_EXP_PROP_ID;
        Integer ce_exp_prop_id = 0;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, eff_mob_exp_prop_id);
            ps.setInt(2, m.getSampleTypeInt());
            ps.setInt(3, c.getCapillaryLength());
            ps.setInt(4, c.getVoltage());
            ps.setInt(5, IonizationModeType.MAPIONIZATIONMODETYPE.get(c.getIonizationMode()));
            ps.setString(6, c.getLabel());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ce_exp_prop_id = rs.getInt("ce_exp_prop_id");
                System.out.println("ID found: " + ce_exp_prop_id);
                rs.close();
                return ce_exp_prop_id;
            } else {
                rs.close();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ce_exp_prop_id;
    }

    /**
     * Selects the ce_eff_mob_id from the table eff_mob
     * using the compound_id and eff_mob_exp_prop_id
     *
     * @param m CEMSCompound
     * @param c CEMSExperimentalConditions
     * @return ce_eff_mob_id
     */
    public Integer get_eff_mob_id(CEMSCompound m, CEMSExperimentalConditions c) {
        String sql = ConstantQueries.SELECT_CE_EFF_MOB_ID;
        Integer ce_eff_mob_id = null;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, m.getCompound_id());
            ps.setInt(2, c.getEff_mob_exp_prop_id());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ce_eff_mob_id = rs.getInt(1);
                rs.close();
                return ce_eff_mob_id;
            } else {
                rs.close();
                return null;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getErrorCode() == 1062) { //code de Duplicate Entry
                System.out.println("No se inserta la información relativa a Effective Mobility porque ya estaba metida.");
            }
        }
        return null;
    }

    /**
     * Selects the eff_mob_exp_prop_id from the table eff_mob_experimental_properties
     * using temperature and polarity
     * It also sets the paramenter eff_mob_exp_prop_id of c
     *
     * @param c CEMSExperimentalConditions
     * @return eff_mob_exp_prop_id
     */
    public int get_eff_mob_exp_prop_id(CEMSExperimentalConditions c) {
        String sql = ConstantQueries.SELECT_EFF_MOB_EXP_PROP;
        Integer eff_mob_exp_prop_id = null;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, BufferType.MAPBUFFERTYPES.get(c.getBuffer()));
            ps.setInt(2, c.getTemperature());
            ps.setInt(3, PolarityType.MAPPOLARITYTYPE.get(c.getPolarity()));
            eff_mob_exp_prop_id = getInt(ps);
            c.setEff_mob_exp_prop_id(eff_mob_exp_prop_id);
            return eff_mob_exp_prop_id;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eff_mob_exp_prop_id;
    }

    /**
     * Looks for the ce_exp_prop_id of the table ce_experimental_properties using exp_label from c
     * It also sets the paramenter ce_exp_prop_id of c
     *
     * @param c CEMSExperimentalConditions
     * @return ce_exp_prop_id
     */
    public Integer getCeExpPropId_Label(CEMSExperimentalConditions c) {
        String sql = "SELECT ce_exp_prop_id FROM ce_experimental_properties WHERE exp_label LIKE ?";
        Integer ce_exp_prop_id = null;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, c.getLabel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ce_exp_prop_id = rs.getInt(1);
                c.setCe_exp_prop_id(ce_exp_prop_id);
                rs.close();
                return ce_exp_prop_id;
            } else {
                rs.close();
                return null;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getErrorCode() == 1062) { //code de Duplicate Entry
                System.out.println("No se inserta la información relativa a Effective Mobility porque ya estaba metida.");
            }
        }
        return null;
    }

    /**
     * Inserts into the table eff_mob the
     * compound_id from m
     * eff_mob_exp_prop_id, ce_exp_prop_id from c
     * cembio_id, eff_mobility
     *
     * @param m CEMSCompound
     * @param c CEMSExperimentalConditions
     * @return eff_mob_id
     */
    public int insertEffMob(CEMSCompound m, CEMSExperimentalConditions c) {
        //returns el id que se acaba de insertar (ce_eff_mob_id)
        String sql = ConstantQueries.INSERT_EFF_MOB;
        //INSERT INTO ce_eff_mob(ce_compound_id, eff_mob_exp_prop_id, cembio_id, eff_mobility) VALUES(?, ?, ?, ?)"
        int eff_mob_id = 0;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, m.getCompound_id());
            ps.setInt(2, c.getEff_mob_exp_prop_id());
            ps.setInt(3, c.getCe_exp_prop_id());

            //tenemos que hacer un get del cembio_id
            Integer cembio_id = null;
            String query = "Select MAX(cembio_id) from eff_mob";
            ResultSet rset = statement.executeQuery(query);
            if (rset.next()) {
                cembio_id = rset.getInt(1);
                //System.out.println(cembio_id);
            }
            rset.close();
            cembio_id++;    //el id insertado es el siguiente al ultimo que se ha leído
            ps.setDouble(4, cembio_id);

            if (m.getEff_mobility() == null) {
                ps.setNull(5, java.sql.Types.NULL);
            } else {
                ps.setDouble(5, m.getEff_mobility());   //CAN BE NULL
            }
            ps.executeUpdate();
            System.out.println("Insertamos la EffectiveMobility");

            //ahora tenemos que obtener el ce_eff_mob_id que se acaba de insertar
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    eff_mob_id = rs.getInt(1);
                    // ceSystem.out.println("Last ce_eff_mob_id: " + ce_eff_mob_id);       //este id es el que se manda a el insert de los fragments
                }
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getErrorCode() == 1062) { //code de Duplicate Entry
                System.out.println("No se inserta la información relativa a Effective Mobility porque ya estaba metida.");
            }
        }
        return eff_mob_id;
    }

    /**
     * Inserts into the table ce_experimental_properties the values of
     * eff_mob_exp_prop_id, ce_sample_type, capillary_length, capillary_voltage, ionization_mode, exp_label
     *
     * @param c CEMSExperimentalConditions
     * @return ceExpProperties id
     * @throws SQLException
     */
    public int insertCeExpProp(CEMSExperimentalConditions c) throws SQLException {
        String sql = ConstantQueries.INSERT_CE_EXP_PROP;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //cuidado con los valores que son null
            //insertamos lo referente a la metionina sulfona

            // ce_eff_mob_id, experimental_mz, ce_identification_level, ce_sample_type," +
            // "capillary_length, capillary_voltage, " +
            // "bge_compound_id, absolute_MT, relative_MT, commercial, exp_eff_mob, ionization_mode)
            ps.setInt(1, c.getEff_mob_exp_prop_id());
            ps.setInt(2, c.getSampleType_int());
            if (c.getCapillaryLength() == null) {
                ps.setNull(3, java.sql.Types.NULL);
            } else {
                ps.setInt(3, c.getCapillaryLength());
            }
            if (c.getVoltage() == null) {
                ps.setNull(4, java.sql.Types.NULL);
            } else {
                ps.setInt(4, c.getVoltage());
            }
            if (IonizationModeType.MAPIONIZATIONMODETYPE.get(c.getIonizationMode()) == null) {
                ps.setNull(5, java.sql.Types.NULL);
            } else {
                ps.setInt(5, IonizationModeType.MAPIONIZATIONMODETYPE.get(c.getIonizationMode()));
            }
            ps.setString(6, c.getLabel());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int ceExpProperties = rs.getInt(1);
                    c.setCe_exp_prop_id(ceExpProperties);
                    return ceExpProperties;
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            System.out.println("Insertamos las ce_experimental_properties");
        } catch (SQLException ex) {
            throw ex;
        }
        throw new SQLException("check the Insertion of: " + c);
    }

    /**
     * Inserts into the table ce_experimental_properties the values of
     * ce_exp_prop_id, compound_id, experimental_mz, ce_identification_level,
     * rmt_ref_compound_id, absolute_MT, relative_MT, commercial, exp_eff_mob
     *
     * @param m CEMSCompound
     * @param c CEMSExperimentalConditions
     */
    public void insertCeExpPropMet(CEMSCompound m, CEMSExperimentalConditions c) {

        String sql = ConstantQueries.INSERT_CE_EXP_PROP_METADATA;
        //"INSERT INTO ce_experimental_properties_metadata(ce_eff_mob_id, experimental_mz, capillary_voltage, capillary_length, bge_compound_id, absolute_MT, relative_MT) VALUES (?, ?, ?, ?, ?, ?, ?)"
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);

            //cuidado con los valores que son null
            //insertamos lo referente a la metionina sulfona

            // ce_eff_mob_id, experimental_mz, ce_identification_level, ce_sample_type," +
            // "capillary_length, capillary_voltage, " +
            // "bge_compound_id, absolute_MT, relative_MT, commercial, exp_eff_mob, ionization_mode)
            ps.setInt(1, c.getCe_exp_prop_id());
            ps.setInt(2, m.getCompound_id());
            if (m.getExperimentalMZ() == null) {
                ps.setNull(3, java.sql.Types.NULL);
            } else {
                ps.setDouble(3, m.getExperimentalMZ());
            }
            ps.setInt(4, m.getIdentificationLevel());
            if (c.getRef_compound_id_RMT() == null) {
                ps.setNull(5, java.sql.Types.NULL);
            } else {
                ps.setInt(5, c.getRef_compound_id_RMT());
            }
            if (m.getMT() == null) {
                ps.setNull(6, java.sql.Types.NULL);
            } else {
                ps.setDouble(6, m.getMT());
            }
            if (m.getRMT() == null) {
                ps.setNull(7, java.sql.Types.NULL);
            } else {
                ps.setDouble(7, m.getRMT());
            }
            ps.setNull(8, java.sql.Types.NULL);
            ps.setDouble(9, m.getEff_mobility());
            ps.executeUpdate();

            System.out.println("Insertamos las ce_experimental_properties_metadata");
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Inserts into the table compound_ce_product_ion:
     * ion_source_voltage, ce_product_ion_mz, ce_product_ion_intensity, ce_transformation_type,
     * ce_eff_mob_id, compound_id_own
     *
     * @param compound_id
     * @param ce_eff_mob_id
     * @param fragments List<Fragment>
     */
    public void insertCompCeProdIon(int compound_id, int ce_eff_mob_id, List<Fragment> fragments) {
        String sql = ConstantQueries.INSERT_COMP_CE_PROD_ION;
        //"INSERT INTO compound_ce_product_ion (ion_source_voltage, ce_product_ion_mz, ce_product_ion_intensity,  ce_product_ion_type, ce_eff_mob_id, compound_id_own) VALUES (?, ?, ?, 'fragment', ?, ?)"

        Double intensity;
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);

            for (int i = 0; i < fragments.size(); i++) {
                ps.setInt(1, ConstantQueries.ION_SOURCE_VOLTAGE);
                ps.setDouble(2, fragments.get(i).getM_Z());

                intensity = fragments.get(i).getIntensity();

                if (intensity == null) {
                    ps.setNull(3, java.sql.Types.NULL);
                } else {
                    ps.setDouble(3, intensity);
                }

                ps.setInt(4, ce_eff_mob_id);
                ps.setInt(5, compound_id);
                ps.executeUpdate();
                System.out.println("Insertamos los fragments");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("\n");
        } catch (NullPointerException npe) {
            //System.out.println("El compuesto con ID " + id + " No ha insertado el fragmento con m/z = " + m_z + " porque es null");
        }

    }

    //---------------------------------------------------------------------------------------------------------

    /**
     * It executes the query and returns the first int returned by the query.
     *
     * @param query
     * @return the ID of the query or 0 if the result is null
     */
    public int getInt(String query) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB)

        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();   //es un iterador

            if (rs.next()) {
                id = rs.getInt(1);  //este uno es que accede a la primera columna de la query que yo le paso
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * @param ps PreparedStatement
     * @return id
     */
    public int getInt(PreparedStatement ps) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB)

        try {
            ResultSet rs = ps.executeQuery();   //es un iterador

            if (rs.next()) {
                id = rs.getInt(1);  //este uno es que accede a la primera columna de la query que yo le paso
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * TO DO
     *
     * @param mz
     * @param tolerance
     * @return
     */
    public int getIdsFromMZ(Double mz, Double tolerance) {

        return 0;
    }

    //habrá que hacer también el resto de gets en función de lo que necesitemos

    /**
     * It executes the query and returns the first String returned by the query
     *
     * @param query
     * @return the String of the query or ??? if the result is null
     */
    public String getString(String query) {
        String word = null;
        try {
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                word = rs.getString(1);
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return word;
    }

    /**
     * It executes the query and returns the list of Fragments returned by the
     * query.
     *
     * @return the list of fragments or null
     */
    private List<Fragment> getFragments(int id) {

        String query = ConstantQueries.SELECTFRAGMENTSFROMID;
        List<Fragment> fragments = new LinkedList<>();
        try {
            PreparedStatement statement2 = this.connection.prepareStatement(query);
            statement2.setInt(1, id);   //para evitar la sql injection
            try {
                ResultSet rs = statement2.executeQuery();
                while (rs.next()) {
                    double m_z = rs.getDouble("m_z");
                    double intensity = rs.getDouble("intensity");
                    fragments.add(new Fragment(m_z, intensity));
                    //System.out.println("frag: " + fragments);
                }
                rs.close();
                return fragments;
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * TO DO It executes the query and returns the list of Fragments returned by
     * the mz and its tolerance
     *
     * @param mz        mz of the fragments to find
     * @param tolerance tolerance in ppm
     * @return the list of fragments or null
     */
    private List<Fragment> getFragmentsFromMZRange(float mz, float tolerance) {

        // hay que crear la query para introducir la mz y la tolerancia por parametros
        // delta es el calculo de las ppm del mz
        String query = "Select * from fragments where mz < mz+delta and mz > mz-delta";
        List<Fragment> fragments = new LinkedList<>();

        try {
            statement.execute(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                //fragments.add(new Fragment(rs.getDouble(3)));   //la columna que contiene el m_z es la tres
                //mejor separarlo
                double m_z = rs.getDouble("M_z");
                fragments.add(new Fragment(m_z));
                //System.out.println("frag: " + fragments);
            }
            rs.close();
            return fragments;
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    /**
     * @param id which is the one from the last metabolite inserted
     * @throws Exception
     */
    public void insertFragments(int id, List<Fragment> fragments)/*throws Exception*/ {

        String query = ConstantQueries.INSERTFRAGMENTSFROMID;
        Double m_z = null;
        Double intensity = null;
        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, id);

            for (int i = 0; i < fragments.size(); i++) {

                m_z = fragments.get(i).getM_Z();
                ps.setDouble(2, m_z);

                intensity = fragments.get(i).getIntensity();
                if (intensity == null) {

                    ps.setNull(3, java.sql.Types.NULL);
                } else {
                    ps.setDouble(3, intensity);
                }
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException npe) {
            System.out.println("El compuesto con ID " + id + " No ha insertado el fragmento con m/z = " + m_z + " porque es null");
        }

    }

    /**
     * SHOULD NOT BE USED..!!! It does not use preparedStaments so it is
     * vulnerable to SQL Injection
     *
     * @param query
     * @return the ID of the query or 0 if the result is null
     */
    private int exampleQueryToGetTheLastGeneratedIdFromAnInsert(String query) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB

        try {

            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            try (ResultSet provRS = statement.getGeneratedKeys()) {
                if (provRS.next()) {
                    id = provRS.getInt(1);
                    System.out.println("Last id: " + id);
                }
                provRS.close();

            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("\n GENERATED KEY of " + actualizacion + " : " + id);
        return id;
    }

    /**
     * Inserts a compound TODO
     * @param compound
     */
    public int insertIntoCompounds(CompoundGC compound) {
        PreparedStatement prep=null;
        int compound_id = 0;

        String sql = "INSERT INTO compounds (cas_id, compound_name, formula, mass, charge_type, charge_number, " +
                "formula_type, compound_type, compound_status, formula_type_int, logP) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prep.setString(1, compound.getCasId()); //could be null
            prep.setString(2, compound.getCompoundName());
            prep.setString(3, compound.getFormula());
            //prep.setDouble(4, compound.getMonoisotopicMass());
            prep.setObject(4, compound.getMonoisotopicMass(), java.sql.Types.DOUBLE);
            prep.setInt(5, compound.getCharge_type());
            prep.setInt(6, compound.getCharge_number());
            prep.setString(7, compound.getFormula_type());
            prep.setInt(8, compound.getCompound_type());
            prep.setInt(9, compound.getCompound_status());
            prep.setInt(10, compound.getFormula_type_int());
            prep.setObject(11, compound.getLogP(), java.sql.Types.DOUBLE);
            /*Double logP = compound.getLogP();
            if(logP!=null) {
                prep.setDouble(11, compound.getLogP()); //could be null
            } else{
                prep.setNull(11, 2);
            }*/
            prep.executeUpdate();

            try (ResultSet rs = prep.getGeneratedKeys()) {
                if (rs.next()) {
                    compound_id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally { //ITS IMPORTANT TO CLOSE PREPARESTATEMENT
            closePrepareStatementResource(prep);
        }
        return compound_id;
    }

    /**
     * Insert identifiers
     * @param c
     */
    public void insertIntoCompoundIdentifiers(CompoundGC c) {
        //Connection connection = null;
        //Statement statement = null;
        PreparedStatement prep=null;
        String sql = "INSERT INTO compound_identifiers (compound_id, inchi, inchi_key, smiles) VALUES (?, ?, ?, ?)";;
        try {
            //connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            //statement = connection.createStatement();

            prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prep.setInt(1, c.getCompound_id());
            prep.setString(2, c.getINCHI());
            prep.setString(3, c.getINCHIKey());
            prep.setString(4, c.getIdentifiersOwn().getSmiles());

            prep.executeUpdate(); //the information is inserted
            //prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            // It's important to close the statement when you are done with it
            closePrepareStatementResource(prep);
        }
    }

    /**
     * Inserts into derivatization methods table the derivatization type of the compound
     * Only if it does not exist
     * @param compoundgc
     */
    public void insertDerivatizationMethod(CompoundGC compoundgc){
        //Connection connection = null;
        //Statement statement = null;
        PreparedStatement prep=null;

        try {
            String derType = compoundgc.getDertype().toString();
            if(getDerivatizationMethodIdfromDerType(derType)==0){ //IF IS 0 THEN IT DOESN'T EXIST IN TABLE
                String sql = "INSERT INTO derivatization_methods (derivatization_type) VALUES (?)";
                prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                prep.setString(1, derType);

                prep.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepareStatementResource(prep);
        }
    }

    /**
     * Gets the id of derivatization method from its name
     * @param name
     * @return derivatization method id
     */
    public int getDerivatizationMethodIdfromDerType(String name) {
        PreparedStatement prep=null;
        int der_id = 0;

        String sql = "SELECT derivatization_method_id FROM derivatization_methods WHERE derivatization_type LIKE ?";
        try {
            prep = this.connection.prepareStatement(sql);
            prep.setString(1, name);
            der_id = getIntDerIdfromPrepStatementDerType(prep);
            return der_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            closePrepareStatementResource(prep);
        }
        return der_id;
    }

    /**
     * From a PreparedStatement obtains an int (derivatization method id)
     * @param ps
     * @return derivatization method id
     */
    private int getIntDerIdfromPrepStatementDerType(PreparedStatement ps) {
        int idDer = 0;
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                //idDer = rs.getInt("derivatization_id");
                idDer = rs.getInt("derivatization_method_id");
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idDer;
    }

    /**
     * inserts gccolumn into gc_column table
     * @param compoundgc
     */
    public void insertGCColumn(CompoundGC compoundgc){
        PreparedStatement prep=null;

        try {
            String gcCol = compoundgc.getGcColumn().toString();
            //String sql = "INSERT INTO gc_column (gc_ri_rt_id, gc_column_name) VALUES (?, ?)";
            //if(getDerivatizationMethodIdfromDerType(gcCol)==0){
            if(getGCColumnIdfromGCColumnType(gcCol)==0){//GcColumn is not in the tables --> insert
                String sql = "INSERT INTO gc_column (gc_column_name) VALUES (?)";
                prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                prep.setString(1, gcCol);

                prep.executeUpdate();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepareStatementResource(prep);
        }
    }

    /**
     * Gets the id of gccolumn type from its name
     * @param name
     * @return gccolumn id
     */
    public int getGCColumnIdfromGCColumnType(String name) {
        PreparedStatement prep=null;
        int column_id = 0;

        String sql = "SELECT gc_column_id FROM gc_column WHERE gc_column_name LIKE ?";
        try {
            prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            prep.setString(1, name);
            column_id = getIntColIdfromPrepStatementGcColumnName(prep);
            return column_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            closePrepareStatementResource(prep);
        }
        return column_id;
    }

    /**
     * From a PreparedStatement obtains an int (gc_column_id)
     * @param ps
     * @return gc_column_id
     */
    private int getIntColIdfromPrepStatementGcColumnName(PreparedStatement ps) {
        int idColumnGC = 0;
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idColumnGC = rs.getInt("gc_column_id");
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return idColumnGC;
    }

    /**
     * Inserts into gc-ri-rt the information (RI, compound id, derivatization id, gc_column_id) of a compound
     * @param compoundgc
     */
    public void insertRIRT(CompoundGC compoundgc){
        //Connection connection = null;
        //Statement statement = null;
        PreparedStatement prep=null;

        try {
            //connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            //statement = connection.createStatement();

            //String sql = "INSERT INTO gc_ri_rt (ri, rt, compound_id, derivatization_method_id) VALUES (?, ?, ?, ?)";
            //String sql = "INSERT INTO gc_ri_rt (compound_id, derivatization_method_id, gc_column_id, ri, rt) VALUES (?, ?, ?, ?, ?)";
            String sql = "INSERT INTO gc_ri_rt (compound_id, derivatization_method_id, gc_column_id, ri) VALUES (?, ?, ?, ?)";
            prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            prep.setDouble(1, compoundgc.getCompound_id());

            String dertype = compoundgc.getDertype().toString();
            int derivatizationMethodId = getDerivatizationMethodIdfromDerType(dertype);
            prep.setDouble(2, derivatizationMethodId);

            String columnType = compoundgc.getGcColumn().toString();
            int gc_column_id = getGCColumnIdfromGCColumnType(columnType);
            prep.setDouble(3, gc_column_id);

            prep.setDouble(4, compoundgc.getRI());
            //prep.setDouble(5, compoundgc.getRT()); //default null
            //prep.setObject(11, compound.getLogP(), java.sql.Types.DOUBLE);


            prep.executeUpdate();
            //prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepareStatementResource(prep);
        }
    }

    /**
     * Gets the id of gc_ri_rt from the compound id
     * @param cid compound id
     * @return gc_ri_rt id
     */
    public int getgcrirtIdfromCompoundId(int cid) {
        //Connection connection = null;
        //Statement statement = null;
        PreparedStatement ps = null;

        int gcrirt_id = 0;

        String sql = "SELECT gc_ri_rt_id FROM gc_ri_rt WHERE compound_id LIKE ?";
        try {
            //connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            //statement = connection.createStatement();

            ps = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, cid);
            gcrirt_id = getIntGCColumnIdfromPrepStatementgccolumn(ps);
            return gcrirt_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            closePrepareStatementResource(ps);
        }
        return gcrirt_id;
    }

    /**
     * From a PreparedStatement obtains an int (gc_ri_rt id)
     * @param ps
     * @return compound id
     */
    private int getIntGCColumnIdfromPrepStatementgccolumn(PreparedStatement ps) {
        int gcColumnId = 0;
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                gcColumnId = rs.getInt("gc_ri_rt_id");
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gcColumnId;
    }

    /**
     * Inserts into gcms_spectrum the information (compound id and derivatization id) of a compound
     * @param compoundgc
     */
    public int insertgcmsSpectrum(CompoundGC compoundgc){
        PreparedStatement prep = null;
        int spectrum_id =0;

        try {
            //int size_GCMS_Spectra = compoundgc.getGcmsSpectrum().size();
            //for (int i = 0; i < size_GCMS_Spectra; i++) {
                String sql = "INSERT INTO gcms_spectrum (compound_id, derivatization_method_id) VALUES (?, ?)";
                prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                prep.setDouble(1, compoundgc.getCompound_id());
                String dertype = compoundgc.getDertype().toString();
                prep.setDouble(2, getDerivatizationMethodIdfromDerType(dertype));

                prep.executeUpdate();

                spectrum_id = getIntfromPrepStatementSpectrum(prep);
                //return spectrum_id;todo cambiado pero no comprobado!!!
            //}
        }  catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepareStatementResource(prep);
            //return spectrum_id;
        }
        return spectrum_id;
    }

    /**
     * From a PreparedStatement obtains an int (gcms spectrum id)
     * @param ps
     * @return spectrum id
     */
    private static int getIntfromPrepStatementSpectrum(PreparedStatement ps) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB)
        try {
            //ResultSet rs = ps.executeQuery();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                //id = rs.getInt("gcms_spectrum_id");
                id = rs.getInt(1);
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * Inserts peaks into gcms_peaks table
     * @param spectrum_id
     * @param gcms_peaks
     */
    public void insertGCMSPeaks(int spectrum_id, GCMS_Peaks gcms_peaks){
        //Connection connection = null;
        //Statement statement = null;
        PreparedStatement prep=null;

        try {
            //connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            //statement = connection.createStatement();

            String sql = "INSERT INTO gcms_peaks (gcms_spectrum_id, mz, intensity) VALUES (?, ?, ?)";
            prep = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            //prep.setDouble(1, getgcmsSpectrumIdfromCompoundId(compoundgc.getCompound_id()));
            prep.setDouble(1, spectrum_id);
            prep.setDouble(2, gcms_peaks.getMz());
            prep.setDouble(3, gcms_peaks.getIntensity());
            prep.executeUpdate();
            //prep.close();

        }  catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closePrepareStatementResource(prep);
        }
    }



    public static void main(String[] args) {

        DBManager db = new DBManager();
        String filename = "resources/connectionData.pass";  //este file contiene los datos de acceso a la database (notación JSON: clave-valor)
        try {
            Gson gson = new Gson();
            String readJSONStr = readStringFromFile(filename);
            JsonElement element = gson.fromJson(readJSONStr, JsonElement.class);
            JsonObject jsonObj = element.getAsJsonObject();
            String dbName = jsonObj.get("db_name").getAsString();
            String dbUser = jsonObj.get("db_user").getAsString();
            String dbPassword = jsonObj.get("db_password").getAsString();

            // Here you can check the values obtained
            //System.out.println("DB_NAME: " + dbName + " DBUser: " + dbUser + " DBPassword: " + dbPassword);
            db. connectToDB("jdbc:mysql://localhost/" + dbName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", dbUser, dbPassword);

            //GET INT
            int id = db.getInt("select compound_id from compounds where formula = \"C5H11NO4\"");
            System.out.println(id);

            //GET STRING
            //String word = db.getString("Select \"asd\"");
            //System.out.println(word);
            //GETMETABOLITO
            //Metabolito m1 = db.getMetabolito("Select * from metabolites where ID = 2");
            //Metabolito m1 = db.getMetabolito("Select * from metabolites");
            //System.out.println("Metabolito 1:\n" + m1);
            //GETFRAGMENTOS
            //List<Fragment> fragments = db.getFragments(2);
            //System.out.println("Fragments: \n" + fragments);
            //GETMETABOLITOS
            //List<Metabolito> metabs = db.getMetabolitos("select * from metabolites where id = 1");
            //List<Metabolito> metabs = db.getMetabolitos("select * from metabolites");
            //System.out.println("Estos son los metabolitos almacenados: \n" + metabs);
            //List<Fragment> fragments = db.getFragments(2);
            //System.out.println("Estos son los fragmentos almacenados: \n" + fragments);
            // SI NO INSERTA NADA, AUTO_GENERATED KEYS DEVUELVE 0
            //int id_updated = db.exampleQueryToGetTheLastGeneratedIdFromAnInsert("update prueba set f1 = 2 where id=2");
            //System.out.println(id_updated);
            //int id_inserted = db.exampleQueryToGetTheLastGeneratedIdFromAnInsert("insert into prueba (f1) values (1)");
            //System.out.println(id_inserted);
            //INSERT METABOLITES
//            List<Fragment> fragments = new LinkedList();
//            fragments.add(new Fragment(78.9594));
//            fragments.add(new Fragment(96.9671));
//            fragments.add(new Fragment(138.9802));
//            Metabolito metabolito = new Metabolito("Fructose 1,6 Biphosphate", "C6H14O12P2", 339.9960, 338.9887, 9.739, 27.135, 0.36, 20.187, 0.48, fragments);
//            db.insertMetabolito(metabolito);
            //db.insertFragments(1, fragments);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ioe) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }
}
