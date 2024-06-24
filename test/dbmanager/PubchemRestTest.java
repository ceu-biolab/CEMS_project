/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbmanager;

import cems_project.Compound;
import cems_project.Identifier;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author maria
 */
public class PubchemRestTest {

    public PubchemRestTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Test of getIdentifiersFromInChIPC method, of class PubchemRest.
     */
    @Test
    public void testGetIdentifiersFromInChIPC_3args() throws Exception {
        System.out.println("getIdentifiersFromInChIPC");
        String inchi = "";
        int retries = 0;
        int sleep = 0;
        Identifier expResult = null;
        Identifier result = PubchemRest.getIdentifiersFromInChIPC(inchi, retries, sleep);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIdentifiersFromInChIPC method, of class PubchemRest.
     */
    @Test
    public void testGetIdentifiersFromInChIPC_String() throws Exception {
        System.out.println("getIdentifiersFromInChIPC");
        String inchi = "";
        Identifier expResult = null;
        Identifier result = PubchemRest.getIdentifiersFromInChIPC(inchi);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIdentifiersFromINCHIKEYPC method, of class PubchemRest.
     */
    @Test
    public void testGetIdentifiersFromINCHIKEYPC() throws Exception {
        System.out.println("getIdentifiersFromINCHIKEYPC");
        String inchi_key = "";
        Identifier expResult = null;
        Identifier result = PubchemRest.getIdentifiersFromINCHIKEYPC(inchi_key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPCIDFromInchiKey method, of class PubchemRest.
     */
    @Test
    public void testGetPCIDFromInchiKey() throws Exception {
        System.out.println("getPCIDFromInchiKey");
        String inchi_key = "";
        Integer expResult = null;
        Integer result = PubchemRest.getPCIDFromInchiKey(inchi_key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIdentifiersFromSMILESPC method, of class PubchemRest.
     */
    @Test
    public void testGetIdentifiersFromSMILESPC() throws Exception {
        System.out.println("getIdentifiersFromSMILESPC");
        String smiles = "";
        Identifier expResult = null;
        Identifier result = PubchemRest.getIdentifiersFromSMILESPC(smiles);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompoundFromName method, of class PubchemRest.
     */
    @Test
    public void testGetCompoundFromName() throws Exception {
        System.out.println("getCompoundFromName");
        String name = "";
        Compound expResult = null;
        Compound result = PubchemRest.getCompoundFromName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPCIDFromName method, of class PubchemRest.
     */
    @Test
    public void testGetPCIDFromName() throws Exception {
        System.out.println("getPCIDFromName");
        String name = "";
        int expResult = 0;
        int result = PubchemRest.getPCIDFromName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompoundFromPCID method, of class PubchemRest.
     */
    @Test
    public void testGetCompoundFromPCID() throws Exception {
        System.out.println("getCompoundFromPCID");
        int pc_id = 0;
        Compound expResult = null;
        Compound result = PubchemRest.getCompoundFromPCID(pc_id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParentCompoundFromPCID method, of class PubchemRest.
     */
    @Test
    public void testGetParentCompoundFromPCID() throws Exception {
        System.out.println("getParentCompoundFromPCID");
        int pc_id = 0;
        Compound expResult = null;
        Compound result = PubchemRest.getParentCompoundFromPCID(pc_id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompsFromNames method, of class PubchemRest.
     */
    @Test
    public void testGetCompsFromNames() {
        System.out.println("getCompsFromNames");
        List<Compound> simpleCompounds = null;
        List<Compound> expResult = null;
        List<Compound> result = PubchemRest.getCompsFromNames(simpleCompounds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParentsFromChildComps method, of class PubchemRest.
     */
    @Test
    public void testGetPInchisFromChildComps() {
        System.out.println("getPInchisFromChildComps");
        List<Compound> compounds = null;
        List<String> expResult = null;
        List<String> result = PubchemRest.getParentsFromChildComps(compounds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompoundFromInchi method, of class PubchemRest.
     */
    @Test
    public void testGetCompoundFromInchi() throws Exception {
        System.out.println("getCompoundFromInchi");
        String inchi = "";
        Compound expResult = null;
        Compound result = PubchemRest.getCompoundFromInchi(inchi);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCompsFromInchis method, of class PubchemRest.
     */
    @Test
    public void testGetCompsFromInchis() {
        System.out.println("getCompsFromInchis");
        List<Compound> compounds = null;
        List<Compound> expResult = null;
        List<Compound> result = PubchemRest.getCompsFromInchis(compounds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInchisFromInchis method, of class PubchemRest.
     */
    @Test
    public void testGetInchisFromInchis() {
        System.out.println("getInchisFromInchis");
        List<Compound> compounds = null;
        List<String> expResult = null;
        List<String> result = PubchemRest.getInchisFromInchis(compounds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class PubchemRest.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        PubchemRest.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
