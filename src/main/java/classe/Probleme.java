/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.OracleConnection;

/**
 *
 * @author tolot
 */
public class Probleme {
    
    int idParis;
    String nomTeam1;
    String nomTeam2;
    Date datematch;
    String type_paris;
    String nomTeamNalainy;

    public int getIdParis() {
        return idParis;
    }

    public void setIdParis(int idParis) {
        this.idParis = idParis;
    }

    public String getNomTeam1() {
        return nomTeam1;
    }

    public void setNomTeam1(String nomTeam1) {
        this.nomTeam1 = nomTeam1;
    }

    public String getNomTeam2() {
        return nomTeam2;
    }

    public void setNomTeam2(String nomTeam2) {
        this.nomTeam2 = nomTeam2;
    }

    public Date getDatematch() {
        return datematch;
    }

    public void setDatematch(Date datematch) {
        this.datematch = datematch;
    }

    public String getType_paris() {
        return type_paris;
    }

    public void setType_paris(String type_paris) {
        this.type_paris = type_paris;
    }

    public String getNomTeamNalainy() {
        return nomTeamNalainy;
    }

    public void setNomTeamNalainy(String nomTeamNalainy) {
        this.nomTeamNalainy = nomTeamNalainy;
    }

    public Probleme() {
    }

    public Probleme(int idParis, String nomTeam1, String nomTeam2, Date datematch, String type_paris, String nomTeamNalainy) {
        this.idParis = idParis;
        this.nomTeam1 = nomTeam1;
        this.nomTeam2 = nomTeam2;
        this.datematch = datematch;
        this.type_paris = type_paris;
        this.nomTeamNalainy = nomTeamNalainy;
    }
    
     private static int getDoublonProbleme(OracleConnection co,int idParis) throws SQLException{
            int val = 0;
            
             Statement statement = null;
            try{
            statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select * from PROBLEME where IDPARIS="+idParis+" ");
           
            while (resultSet.next()){
                val++;
            }
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
                
            }
            return val;
        }
     
      public static void insertProbleme(OracleConnection oc,int idParis) throws SQLException{
           
            if(getDoublonProbleme(oc,idParis)!=1){
                PreparedStatement statement = null;
                 try{
                
                String req = "insert into PROBLEME values(SEQ_PROBLEME.NEXTVAL,?,0)";

                statement = oc.prepareStatement(req);
                statement.setInt(1, idParis);

                statement.executeQuery();
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
            }
            else{
                System.out.println("deja dans la base");
            }
            
        }
    
    
}
