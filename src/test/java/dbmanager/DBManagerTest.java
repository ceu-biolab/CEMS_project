package dbmanager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBManagerTest {

    @Test
    void getce_eff_mob_id() {
        DBManager dbManager = new DBManager();
        dbManager.connectToDB("compounds","root","alberto");
        Integer ce_compound_id = 180838;
        Integer eff_mob_exp_prop_id = 1;
        Integer expectedResult = 1;
        Integer actualResult = null;
        try {
            actualResult = dbManager.get_ce_eff_mob_id(ce_compound_id, eff_mob_exp_prop_id);
            assertEquals(expectedResult,actualResult);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}