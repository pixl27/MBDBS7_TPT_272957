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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author tolot
 */
public class Match {

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getIdTeam1() {
        return idTeam1;
    }

    public void setIdTeam1(int idTeam1) {
        this.idTeam1 = idTeam1;
    }

    public int getIdTeam2() {
        return idTeam2;
    }

    public void setIdTeam2(int idTeam2) {
        this.idTeam2 = idTeam2;
    }

    public Date getDatematch() {
        return datematch;
    }

    public void setDatematch(Date datematch) {
        this.datematch = datematch;
    }

    public int getNbrMap() {
        return nbrMap;
    }

    public void setNbrMap(int nbrMap) {
        this.nbrMap = nbrMap;
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

    public Match() {
    }
    
   int idMatch;
   int idTeam1;
   int idTeam2;
   Date datematch;
   int nbrMap;
   String nomTeam1;
   String nomTeam2;

    public Match(int idMatch, int idTeam1, int idTeam2, Date datematch, int nbrMap, String nomTeam1, String nomTeam2) {
        this.idMatch = idMatch;
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
        this.datematch = datematch;
        this.nbrMap = nbrMap;
        this.nomTeam1 = nomTeam1;
        this.nomTeam2 = nomTeam2;
    }
    
    public static Match getMatchById(OracleConnection co,int id) throws SQLException{
            Match val = new Match();
            Statement statement = null;
            
            try{
                statement = co.createStatement();
           
                 ResultSet resultSet = statement.executeQuery("select * from Match where idMatch ="+id);
                    while (resultSet.next()){
                        //int idMatch, int idTeam1, int idTeam2, Date datematch, int nbrMap, String nomTeam1, String nomTeam2
                        val = new Match(resultSet.getInt(1),resultSet.getInt(2),resultSet.getInt(3),resultSet.getDate(4),resultSet.getInt(5),resultSet.getString(6),resultSet.getString(7));
                    }
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
             
            
            return val;
            
            
        }
    
    public static int getNbrMatchProbleme(OracleConnection co) throws SQLException{
             int val = 0;
                try(Statement statement = co.createStatement()) {

                ResultSet resultSet = statement.executeQuery("select count(distinct(idMatch)) from Paris where statut=2");

                while (resultSet.next()){
                    val = resultSet.getInt(1);
                }
                }
                
                return val;
         }
    
    public static ArrayList<JSONObject> getAllMatchQuiConcorde(int idOpposingteam,Date datematch,JSONArray arrayMatch) throws JSONException{
            ArrayList<JSONObject> val = new ArrayList();
            int size = arrayMatch.length()-1;
            for(int i= 0;i<size;i++){
                int unixTimestamp = arrayMatch.getJSONObject(i).getInt("start_time");
                System.out.println("unix timestamp "+unixTimestamp);
                Date datematchAPI = new Date(unixTimestamp*1000L);
                System.out.println("Start time match :"+datematchAPI);
                System.out.println("Date match base "+datematch);
                int idOpposingteamAPI = arrayMatch.getJSONObject(i).getInt("opposing_team_id");
                if(datematch.compareTo(datematchAPI)<=0){
                    System.out.println("Date concorde");
                    if(idOpposingteam==idOpposingteamAPI){
                     System.out.println("adversaire concorde");
                    val.add(arrayMatch.getJSONObject(i));
                    }
                }
                else{
                    System.out.println("Date passé, Break");
                    break;
                }
            }
            return val;
        }
    
     public static ArrayList<JSONObject> getAllMatchQuiConcorde(String nomOpposingteam,Date datematch,JSONArray arrayMatch) throws JSONException{
            ArrayList<JSONObject> val = new ArrayList();
            int size = arrayMatch.length()-1;
            for(int i= 0;i<size;i++){
                int unixTimestamp = arrayMatch.getJSONObject(i).getInt("start_time");
                Date datematchAPI = new Date(unixTimestamp*1000L);
                System.out.println("Start time match :"+datematchAPI);
                System.out.println("Date match base "+datematch);
                String nameOpposingteamAPI = arrayMatch.getJSONObject(i).getString("opposing_team_name");
                System.out.println("Nom adversaire "+nameOpposingteamAPI);
                System.out.println("Nom normal "+nomOpposingteam);
                if(datematch.compareTo(datematchAPI)<=0){
                    System.out.println("Date concorde");
                    if(comparerNom(nomOpposingteam,nameOpposingteamAPI)){
                     System.out.println("adversaire concorde");
                    val.add(arrayMatch.getJSONObject(i));
                    }
                }
                else{
                    System.out.println("Date passé, Break");
                    break;
                }
            }
            return val;
        }
        
        private static Boolean comparerNom(String nom1,String nom2){
            int taille1 = nom1.length();
            int taille2 = nom2.length();

             if(taille1>taille2){
                   return nom1.contains(nom2);
              }
              else{
                   return nom2.contains(nom1);
              }
        }
        
           public static  int getNbrMatch(OracleConnection oc) throws SQLException{
              int val = 0;
                try(Statement statement = oc.createStatement()) {

                ResultSet resultSet = statement.executeQuery("select count(*) from Match");

                while (resultSet.next()){
                    val = resultSet.getInt(1);
                }
                }
                
                return val;
         }
           
           public static int getDoublonMatch(OracleConnection co,MatchAPI match) throws SQLException{
            int val = 0;
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDMATCH from MATCH where IDTEAMRADIANT="+match.getIdTeam1()+" and IDTEAMDIRE="+match.getIdTeam2()+" and DATEMATCH=TO_DATE('"+match.getDatematch()+"','YYYY-MM-DD') and BO="+match.getNbrMap()+" ");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
   
   
}
