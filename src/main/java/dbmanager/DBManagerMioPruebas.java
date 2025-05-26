package dbmanager;

/*
USO PARA MIS PRUEBAS Y MANEJO DB's
PASAR TABLAS CREADAS A TABLAS CORRECTAS Y A DBMANAGER BUENO
 */

import GCMS.CompoundGC;
import GCMS.GCMS_Peaks;
import cems_project.CEMSCompound;
import cems_project.CEMSExperimentalConditions;
import cems_project.Compound;
import cems_project.Fragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import exceptions.WrongRequestException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utilities.FileIO.readStringFromFile;

public class DBManagerMioPruebas {

    private static final String dbname = "jdbc:mysql://localhost/gcms?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String dbuser = "root";
    private static final String dbpassword = "";


    //SAME AS INSERT COMPOUND DBMANAGER
    /**
     * Inserts a compound
     * @param compound
     */
    public static void insertintoCompound(Compound compound) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;
        int compound_id = 0;
        String sql = "INSERT INTO compounds (compound_name, formula, mass) VALUES (?, ?, ?)";;
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            prep = connection.prepareStatement(sql);
            prep.setString(1, compound.getCompoundName());
            prep.setString(2, compound.getFormula());
            prep.setDouble(3, compound.getMonoisotopicMass());
            prep.executeUpdate(); //the information is inserted

            /*try (ResultSet rs = prep.getGeneratedKeys()) {
                if (rs.next()) {
                    compound_id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // It's important to close the statement when you are done with it
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //return compound_id;
    }

    //Same as CompoundIdentifiers but for my tables
    /**
     * Insert identifiers
     * @param c
     */
    public static void insertCompoundIdentifiers(Compound c) {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;
        String sql = "INSERT INTO identifiers (compound_id, inchi, inchi_key, smiles) VALUES (?, ?, ?, ?)";;
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            prep = connection.prepareStatement(sql);
            prep.setInt(1, c.getCompound_id());
            prep.setString(2, c.getINCHI());
            prep.setString(3, c.getINCHIKey());
            prep.setString(4, c.getIdentifiersOwn().getSmiles());

            prep.executeUpdate(); //the information is inserted
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            // It's important to close the statement when you are done with it
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //funcion equivalente en DBManager
    /**
     * Obtains the compound id from the inchi
     * @param inchi
     * @return compound id
     */
    public static int getCompoundIdFromInchi(String inchi) {
        Connection connection = null;
        Statement statement = null;
        //PreparedStatement prep=null;
        int compound_id = 0;

        String sql = "SELECT compound_id FROM identifiers WHERE inchi LIKE ?";
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, inchi);
            compound_id = getIntfromPrepStatement(ps);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return compound_id;
    }

    /**
     * Obtains from the name of a compound its id
     * @param name
     * @return id
     */
    public static int getCompoundIdfromName(String name) {
        Connection connection = null;
        Statement statement = null;
        //PreparedStatement prep=null;
        int compound_id = 0;

        String sql = "SELECT compound_id FROM compounds WHERE compound_name LIKE ?";
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            compound_id = getIntfromPrepStatement(ps);
            return compound_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return compound_id;
    }

    /**
     * Inserts into derivatization methods table the derivatization type of the compound
     * Only if it does not exist
     * @param compoundgc
     */
    public static void insertDerivatizationMethod(CompoundGC compoundgc){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            String derType = compoundgc.getDertype().toString();
            if(getDerivatizationMethodIdfromDerType(derType)==0){ //IF IS 0 THEN IT DOESN'T EXIST IN TABLE
                String sql = "INSERT INTO derivatization_methods (derivatization_type) VALUES (?)";
                prep = connection.prepareStatement(sql);
                prep.setString(1, derType);

                prep.executeUpdate();
                prep.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // It's important to close the statement when you are done with it
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Inserts into gc-ri-rt the information (RT, RI, compound id, derivatization id, gc_column_id) of a compound
     * @param compoundgc
     */
    public static void insertRIRT(CompoundGC compoundgc){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            //String sql = "INSERT INTO gc_ri_rt (ri, rt, compound_id, derivatization_method_id) VALUES (?, ?, ?, ?)";
            String sql = "INSERT INTO gc_ri_rt (compound_id, derivatization_method_id, gc_column_id, ri, rt) VALUES (?, ?, ?, ?, ?)";
            prep = connection.prepareStatement(sql);

            prep.setDouble(1, compoundgc.getCompound_id());

            String dertype = compoundgc.getDertype().toString();
            prep.setDouble(2, getDerivatizationMethodIdfromDerType(dertype));

            String columnType = compoundgc.getGcColumn().toString();
            int gc_column_id = getGCColumnIdfromGCColumnType(columnType);
            prep.setDouble(3, gc_column_id);

            prep.setDouble(4, compoundgc.getRI());
            prep.setDouble(5, compoundgc.getRT());

            prep.executeUpdate();
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Inserts into compounds derivatization methods table the information (compound id and derivatization id) of a compound
     * NOT ANYMORE
     * @param compoundgc
     */
    /*public static void insertCompoundsDerMethods(CompoundGC compoundgc){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            String sql = "INSERT INTO compounds_derivatizationmethods (compound_id, derivatization_method_id) VALUES (?, ?)";
            prep = connection.prepareStatement(sql);
            prep.setDouble(1, compoundgc.getCompound_id());

            String dertype = compoundgc.getDertype().toString();
            prep.setDouble(2, getDerivatizationMethodIdfromDerType(dertype));
            prep.executeUpdate();
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }*/

    /**
     * Look if the compound is in the table
     * returnn 0 if it is not there
     * NOT ANYMORE
     * @param id compound
     * @return id compound
     */
    /*public static int getCompoundsIdDerMethods(int id){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            String sql = "SELECT compound_id FROM compounds_derivatizationmethods WHERE compound_id LIKE ?";
            prep = connection.prepareStatement(sql);

            prep.setInt(1, id);

            try (ResultSet rs = prep.executeQuery()) {
                if (rs.next()) {
                    id = rs.getInt("compound_id");
                } else {
                    id = 0;
                }
            }
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return id;
    }*/

    /**
     * inserts gccolumn into gc_column table
     * @param compoundgc
     */
    public static void insertGCColumn(CompoundGC compoundgc){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            String gcCol = compoundgc.getGcColumn().toString();
            //String sql = "INSERT INTO gc_column (gc_ri_rt_id, gc_column_name) VALUES (?, ?)";
            //if(getDerivatizationMethodIdfromDerType(gcCol)==0){
            if(getGCColumnIdfromGCColumnType(gcCol)==0){//GcColumn is not in the tables --> insert
                String sql = "INSERT INTO gc_column (gc_column_name) VALUES (?)";
                prep = connection.prepareStatement(sql);

                //prep.setDouble(1, getgcrirtIdfromCompoundId(compoundgc.getCompound_id()));
                prep.setString(1, gcCol);

                prep.executeUpdate();
                prep.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gets the id of gccolumn type from its name
     * @param name
     * @return gccolumn id
     */
    public static int getGCColumnIdfromGCColumnType(String name) {
        Connection connection = null;
        Statement statement = null;
        //PreparedStatement prep=null;
        int column_id = 0;

        String sql = "SELECT gc_column_id FROM gc_column WHERE gc_column_name LIKE ?";
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            column_id = getIntfromPrepStatementGcColumnName(ps);
            return column_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return column_id;
    }

    /**
     * From a PreparedStatement obtains an int (gc_column_id)
     * returns 0 if it does not exist
     * @param ps
     * @return gc_column_id
     */
    private static int getIntfromPrepStatementGcColumnName(PreparedStatement ps) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB)
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("gc_column_id");
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     *
     * inserts peaks into gcms_peaks table
     * @param compoundgc
     */
    public static void insertGCMSPeaks(CompoundGC compoundgc, GCMS_Peaks gcms_peaks){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            String sql = "INSERT INTO gcms_peaks (gcms_spectrum_id, mz, intensity) VALUES (?, ?, ?)";
            prep = connection.prepareStatement(sql);

            prep.setDouble(1, getgcmsSpectrumIdfromCompoundId(compoundgc.getCompound_id()));
            prep.setDouble(2, gcms_peaks.getMz());
            prep.setDouble(3, gcms_peaks.getIntensity());
            prep.executeUpdate();
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gets the id of derivatization method from its name
     * returns 0 if it does not exists
     * @param name
     * @return derivatization method id
     */
    public static int getDerivatizationMethodIdfromDerType(String name) {
        Connection connection = null;
        Statement statement = null;
        //PreparedStatement prep=null;
        int der_id = 0;

        String sql = "SELECT derivatization_method_id FROM derivatization_methods WHERE derivatization_type LIKE ?";
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            der_id = getIntfromPrepStatementDerType(ps);
            return der_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return der_id;
    }

    /**
     * Gets the id of gc_ri_rt from the compound id
     * @param cid
     * @return gc_ri_rt id
     */
    public static int getgcrirtIdfromCompoundId(int cid) {
        Connection connection = null;
        Statement statement = null;
        int gcrirt_id = 0;

        String sql = "SELECT gc_ri_rt_id FROM gc_ri_rt WHERE compound_id LIKE ?";
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cid);
            gcrirt_id = getIntfromPrepStatementgccolumn(ps);
            return gcrirt_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return gcrirt_id;
    }

    /**
     * Gets the id of gcmsSpectrum from the compound id
     * @param cid
     * @return gcmsSpectrumId
     */
    private static int getgcmsSpectrumIdfromCompoundId(int cid) {
        Connection connection = null;
        Statement statement = null;
        //PreparedStatement prep=null;
        int gcspectrum_id = 0;

        String sql = "SELECT gcms_spectrum_id FROM gcms_spectrum WHERE compound_id LIKE ?";
        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cid);
            gcspectrum_id = getIntfromPrepStatementSpectrum(ps);
            return gcspectrum_id;

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return gcspectrum_id;
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
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("gcms_spectrum_id");
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * Inserts into gcms_spectrum the information (compound id and derivatization id) of a compound
     * @param compoundgc
     */
    public static void insertgcmsSpectrum(CompoundGC compoundgc){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            String sql = "INSERT INTO gcms_spectrum (compound_id, derivatization_method_id) VALUES (?, ?)";
            prep = connection.prepareStatement(sql);
            ////prep.setDouble(1, getCompoundIdfromName(compoundgc.getCompoundName())); //Since the name could give errors
            //prep.setDouble(1, getCompoundIdFromInchi(compoundgc.getINCHI()));
            prep.setDouble(1, compoundgc.getCompound_id());
            String dertype = compoundgc.getDertype().toString();
            prep.setDouble(2, getDerivatizationMethodIdfromDerType(dertype));
            prep.executeUpdate();
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * NOT IN USE - Early stages of the project
     * Uploades the info of a compound into a database. In this case the database is tfg_pruebas.compound_gcms
     * Solo sube la info nombre, RT, RI
    // * @param compoundgc
     */
    /*public static void insertGCCompoundintoCompoundgcms(CompoundGC compoundgc){
        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            String sql = "INSERT INTO compound_gcms (name, rt, ri) VALUES (?, ?, ?)";
            prep = connection.prepareStatement(sql);
            prep.setString(1, compoundgc.getCompoundName());
            prep.setDouble(2, compoundgc.getRT());
            prep.setDouble(3, compoundgc.getRI());
            prep.executeUpdate();
            prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // It's important to close the statement when you are done with it
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }*/

    /**
     * From a PreparedStatement obtains an int (compound id)
     * @param ps
     * @return compound id
     */
    private static int getIntfromPrepStatement(PreparedStatement ps) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB)
        try {
            ResultSet rs = ps.executeQuery();   //iterator
            if (rs.next()) {
                id = rs.getInt("compound_id");
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * From a PreparedStatement obtains an int (derivatization method id)
     * @param ps
     * @return derivatization method id
     */
    private static int getIntfromPrepStatementDerType(PreparedStatement ps) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB)
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("derivatization_method_id");
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /**
     * From a PreparedStatement obtains an int (gc_ri_rt id)
     * @param ps
     * @return compound id
     */
    private static int getIntfromPrepStatementgccolumn(PreparedStatement ps) {
        int id = 0;
        // Be aware that the connection should be initialized (calling the method connectToDB)
        try {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("gc_ri_rt_id");
            }
            rs.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    /*public static void main(String[] args) {

        System.out.println("PRUEBAS");

        //DBManager db = new DBManager();
        //String filename = "resources/connectionData.pass";  //este file contiene los datos de acceso a la database (notación JSON: clave-valor)

        /*String dbname = "jdbc:mysql://localhost/pruebas_tfg?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String dbuser = "root";
        String dbpassword = "LaRambla_SQL";*
        //db.connectToDB("jdbc:mysql://localhost/" + dbName + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", dbUser, dbPassword);

        Connection connection = null;
        Statement statement = null;
        PreparedStatement prep=null;

        try {
            connection = DriverManager.getConnection(dbname, dbuser, dbpassword);
            statement = connection.createStatement();

            CompoundGC compoundgc = null;
            Compound compoundInfo = null;

            try {
                compoundInfo = PubchemRest.getCompoundFromName("Nonane");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("INFO");
            compoundgc = new CompoundGC(compoundInfo.getCompound_id(),
                    compoundInfo.getCompoundName(),
                    compoundInfo.getCasId(), compoundInfo.getFormula(), compoundInfo.getMonoisotopicMass(), compoundInfo.getCompound_status(),
                    compoundInfo.getCompound_type(), compoundInfo.getLogP(), compoundInfo.getIdentifiersOwn(), compoundInfo.getIdentifiersParent(),
                    816.0, 5.903);

            //System.out.println("COMPOUND: "+compoundInfo);
            /*String sql = "INSERT INTO compound_gcms (name, rt, ri) VALUES (?, ?, ?)";
            prep = connection.prepareStatement(sql);
            prep.setString(1, compoundgc.getCompoundName());
            prep.setDouble(2, compoundgc.getRT());
            prep.setDouble(3, compoundgc.getRI());*
            System.out.println("METODO");
            //Se puede ver si o no esta el dertype y en funcion de ello subirlo o no
            //insertDerivatizationMethod(compoundgc); //Como actualemnte solo hay un metodo solo hay que añadirlo una vez

            //insertCompoundsDerMethods(compoundgc);
            //insertRIRT(compoundgc);
            //insertGCColumn(compoundgc); //no completo
            //insertgcmsSpectrum(compoundgc); //no completo
            //insertGCMSPeaks(compoundgc);//no completo

            //prep.executeUpdate();
           //prep.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        } /*finally {
            // It's important to close the statement when you are done with it
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }*

        System.out.println("ACABADO");

    }*/

    /*//MAIN TO INSERT COMPOUNDS THAT WASN'T On DB
    public static void main(String[] args) {

        try {
            /*
            Compound compoundInfo2 = PubchemRest.getCompoundFromName("Cyclohexanone, 4-hydroxy-4-methyl-\n");
            System.out.println("COMPOUN 2: "+compoundInfo2);

            insertintoCopiaCompound(compoundInfo2); //c2 inserted
            int id2 = getCompoundIdfromName(compoundInfo2.getCompoundName());
            compoundInfo2.setCompound_id(id2);
            System.out.println("ID2: "+compoundInfo2.getCompound_id());
            System.out.println("inchi2: "+compoundInfo2.getINCHI());
            System.out.println("inchi22: "+compoundInfo2.getIdentifiersOwn().getInchi());
            System.out.println("key2: "+compoundInfo2.getINCHIKey());
            System.out.println("key22: "+compoundInfo2.getIdentifiersOwn().getInchi_key());
            System.out.println("smiles22: "+compoundInfo2.getIdentifiersOwn().getSmiles());
            insertCompoundIdentifiers(compoundInfo2);
            *
            /*Compound 3 inserted
            Compound compoundInfo3 = PubchemRest.getCompoundFromName("1H-Pyrrole-2,5-dione, 3-ethyl-4-methyl-");
            System.out.println("COMPOUN 3: "+compoundInfo3);

            insertintoCopiaCompound(compoundInfo3);
            int id3 = getCompoundIdfromName(compoundInfo3.getCompoundName());
            compoundInfo3.setCompound_id(id3);
            System.out.println("ID3: "+compoundInfo3.getCompound_id());
            System.out.println("inchi3: "+compoundInfo3.getINCHI());
            System.out.println("inchi33: "+compoundInfo3.getIdentifiersOwn().getInchi());
            System.out.println("key3: "+compoundInfo3.getINCHIKey());
            System.out.println("key33: "+compoundInfo3.getIdentifiersOwn().getInchi_key());
            System.out.println("smiles33: "+compoundInfo3.getIdentifiersOwn().getSmiles());
            insertCompoundIdentifiers(compoundInfo3);
            *

            //Compound 4 inserted
            /*Compound compoundInfo4 = PubchemRest.getCompoundFromName("Caryophylla-4(12),8(13)-dien-5.alpha.-ol\n");
            System.out.println("COMPOUN 4: "+compoundInfo4);

            insertintoCopiaCompound(compoundInfo4);
            int id4 = getCompoundIdfromName(compoundInfo4.getCompoundName());
            compoundInfo4.setCompound_id(id4);
            System.out.println("ID4: "+compoundInfo4.getCompound_id());
            System.out.println("inchi4: "+compoundInfo4.getINCHI());
            System.out.println("inchi44: "+compoundInfo4.getIdentifiersOwn().getInchi());
            System.out.println("key4: "+compoundInfo4.getINCHIKey());
            System.out.println("key44: "+compoundInfo4.getIdentifiersOwn().getInchi_key());
            System.out.println("smiles44: "+compoundInfo4.getIdentifiersOwn().getSmiles());
            insertCompoundIdentifiers(compoundInfo4);
            *

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }*/
}

