package desmoj.tutorial1.EventsExample;

import java.sql.*;

public class DBHandler {

    private Connection con;
    private Statement st;

    DBHandler(String DatabaseName) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://BATTAH;user=user;password=1234;database=" + DatabaseName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void InsertPatient(Patient patient) {
        String cmd = "Insert Into LOSPredictions (HospID,Sex,ResID,Age,Diag1,ADM_FRACTURE_TYPE,ADM_FRAGILITY,CHO,SimYear)"
                + " values("
                + patient.Hospital + ","
                + patient.Sex + ","
                + patient.ResID + ","
                + patient.Age + ","
                + "'" + patient.Diag1 + "',"
                + patient.FracType + ","
                + patient.Fragility + ","
                + "'" + GlobalObjects.simCHO + "',"
                + GlobalObjects.simYear
                + ")";
        try {
            st = con.createStatement();
            st.execute(cmd);
            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
