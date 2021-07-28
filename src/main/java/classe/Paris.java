/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import oracle.jdbc.OracleConnection;

/**
 *
 * @author tolot
 */
public class Paris {
    
     int idParis;
    String idUser;
    int idMatch;
    int idTeam;
    String type;
    float montant;
    float odds;
    Date dateparis;
    int statut;

    public Paris() {
    }

    public int getIdParis() {
        return idParis;
    }

    public void setIdParis(int idParis) {
        this.idParis = idParis;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public float getOdds() {
        return odds;
    }

    public void setOdds(float odds) {
        this.odds = odds;
    }

    public Date getDateparis() {
        return dateparis;
    }

    public void setDateparis(Date dateparis) {
        this.dateparis = dateparis;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }
    
   

    public Paris(int idParis, String idUser, int idMatch, int idTeam, String type, float montant, float odds, Date dateparis, int statut) {
        this.idParis = idParis;
        this.idUser = idUser;
        this.idMatch = idMatch;
        this.idTeam = idTeam;
        this.type = type;
        this.montant = montant;
        this.odds = odds;
        this.dateparis = dateparis;
        this.statut = statut;
    }
    
    
     public static void insererParis(OracleConnection connection,String idUser,int idMatch,int idTeam,String type,float montant,float odds,Date dateparis,int statut) throws SQLException{
            
            Statement statement = null;
            try{
                statement = connection.createStatement();
           
                statement.executeQuery("insert into PARIS values(PARIS_SEQ.NEXTVAL,'"+idUser+"',"+idMatch+","+idTeam+",'"+type+"',"+montant+","+odds+",TO_DATE('"+dateparis+"','YYYY-MM-DD'),"+statut+")");
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
            
        }
     
    
     public static ArrayList<Paris> getAllParisNonFinie(OracleConnection co) throws SQLException{
            ArrayList<Paris> val = new ArrayList();
            Statement statement = co.createStatement();
            String req = "select * from Paris where statut=0 order by dateparis";
            
             ResultSet res = statement.executeQuery(req);
             while (res.next()){
                 //int idParis, int idUser, int idMatch, int idTeam, String type, float montant, float odds, Date dateparis, int statut
                Paris temp = new Paris(res.getInt(1),res.getString(2),res.getInt(3),res.getInt(4),res.getString(5),res.getFloat(6),res.getFloat(7),res.getDate(8),res.getInt(9));
                val.add(temp);
            }
            return val;
       }
     
     public static Paris getParisById(OracleConnection co,int idParis) throws SQLException{
            Paris p = new Paris();
            Statement statement = co.createStatement();
            String req = "select * from Paris where IDPARIS="+idParis;
            
             ResultSet res = statement.executeQuery(req);
             while (res.next()){
                 //int idParis, int idUser, int idMatch, int idTeam, String type, float montant, float odds, Date dateparis, int statut
                 p = new Paris(res.getInt(1),res.getString(2),res.getInt(3),res.getInt(4),res.getString(5),res.getFloat(6),res.getFloat(7),res.getDate(8),res.getInt(9));
                
            }
            return p;
       }
     
       public static int getNbrParis(OracleConnection co) throws SQLException{
             int val = 0;

                try(Statement statement = co.createStatement()) {

                ResultSet resultSet = statement.executeQuery("select count(*) from Paris");

                while (resultSet.next()){
                    val = resultSet.getInt(1);
                }
                }
                
                return val;
         }
       
       
       public static  void setStatusParis(OracleConnection co,int idParis) throws SQLException{
             Statement statement = null;
            try{
                statement = co.createStatement();
           
                statement.executeUpdate("update PARIS set STATUT=1 where IDPARIS="+idParis);
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
         }
       
      public static  void setParisToProbleme(OracleConnection co,int idParis) throws SQLException{
          Statement statement = null;
            try{
                statement = co.createStatement();
           
                statement.executeUpdate("update PARIS set STATUT=2 where IDPARIS="+idParis);
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
         }
}
