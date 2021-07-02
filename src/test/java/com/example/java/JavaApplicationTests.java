package com.example.java;

import classe.Connexion;
import classe.Match;
import classe.MatchAPI;
import classe.Paris;
import classe.Team;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.TimeUnit;



import oracle.jdbc.OracleConnection;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
class JavaApplicationTests {
      @Autowired
                private RestTemplate restTemplate;
      
        @Bean
        public RestTemplate restTemplate() {
        return new RestTemplate();
        }
        
        
        private int getRowCount(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }

        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException exp) {
        } finally {
            try {
                resultSet.beforeFirst();
            } catch (SQLException exp) {
            }
        }

        return 0;
    }
    
        int getVraiTeam(ArrayList<Team> list){
            int val = 0;
            int timeMax = 0;
            for(int i =0;i<list.size();i++){
                 int startTimeTemp = list.get(i).getIdTeam();
                 if(startTimeTemp>timeMax){
                     val = i;
                     timeMax = startTimeTemp;
                 }
            }
            return val;
        }
    
        Team findTeambynomV2(String nom) throws SQLException{
            OracleConnection connection = Connexion.getConnection();
            
            
           PreparedStatement statement = null;
           Team val = new Team();
           
            try{
            
                String req ="select IDTEAM,LOGO from Team where nom like ? ";
                
                statement = connection.prepareStatement(req,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                
                statement.setString(1,"%" + nom + "%");
                
                ResultSet resultSet = statement.executeQuery();

                int nbrRow = getRowCount(resultSet);
                System.out.println("nbrRow:"+nbrRow);
                if(nbrRow==1){
                    while (resultSet.next()){
                        val.setIdTeam(resultSet.getInt(1));
                        val.setLogo(resultSet.getString(2));
                    }
                }
                if(nbrRow>1){
                    System.out.println("Team de ce nom bobaka");
                    ArrayList<Team> arrayTeamTemp = new ArrayList();
                    while (resultSet.next()){
                        Team temp = new Team();
                        temp.setIdTeam(resultSet.getInt(1));
                        temp.setLogo(resultSet.getString(2));
                        arrayTeamTemp.add(temp);
                    }
                    int indiceVrai = getVraiTeam(arrayTeamTemp);
                    val = arrayTeamTemp.get(indiceVrai);
                }
            }
            finally{
                if(statement!=null){
                statement.close();
            }
                if(connection!=null){
                    connection.close();
                }
            }
            
            return val;
        }

         int findteambynom(String nom) throws SQLException{
            OracleConnection connection = Connexion.getConnection();
           Statement statement = null;
           int id = 0; 
            try{
            
            statement = connection.createStatement();
           
             ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom like '%"+nom+"%'");
            
            
            while (resultSet.next())
                id = resultSet.getInt(1);
            }
            finally{
                if(statement!=null){
                statement.close();
            }
                if(connection!=null){
                    connection.close();
                }
            }
            
            return id;
        }
         
    
            
          JSONObject getJSONAPI(String url) throws JSONException{
         HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          JSONObject json = new JSONObject(response.getBody().toString());
          return json;
    }
    
    JSONArray getJSONArrayAPI(String url) throws JSONException{
         HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          
          JSONArray jsonarray = new JSONArray(response.getBody().toString());
          return jsonarray;
    }
    
    
    
     void insererParis(OracleConnection connection,int idUser,int idMatch,int idTeam,String type,float montant,float odds,Date dateparis,int statut) throws SQLException{
            
            Statement statement = null;
            try{
                statement = connection.createStatement();
           
                statement.executeQuery("insert into PARIS values(PARIS_SEQ.NEXTVAL,"+idUser+","+idMatch+","+idTeam+",'"+type+"',"+montant+","+odds+",TO_DATE('"+dateparis+"','YYYY-MM-DD'),"+statut+")");
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
            
        }
         
     int getStartTime(int idTeam) throws JSONException{
          String url = "https://api.opendota.com/api/teams/"+idTeam+"/matches";
          JSONArray array = getJSONArrayAPI(url);
          int val = array.getJSONObject(0).getInt("start_time");
          return val;
    }
     
      int getSequence(OracleConnection co,String nom) throws SQLException{
            int val = 0;
            Statement statement = co.createStatement();
           
           ResultSet resultSet = statement.executeQuery("select "+nom+".currval from DUAL");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
      
      void insererMatch(OracleConnection connection,MatchAPI match) throws SQLException{
           
            Statement statement = null;
            try{
                statement = connection.createStatement();
           
                statement.executeQuery("insert into Match values(MATCH_SEQ.NEXTVAL,"+match.getIdTeam1()+","+match.getIdTeam2()+",TO_DATE('"+match.getDatematch()+"','YYYY-MM-DD'),"+match.getNbrMap()+",'"+match.getNomTeam1()+"','"+match.getNomTeam2()+"')");
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
    }
      
        int getDoublonMatch(OracleConnection co,MatchAPI match) throws SQLException{
            int val = 0;
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDMATCH from MATCH where IDTEAMRADIANT="+match.getIdTeam1()+" and IDTEAMDIRE="+match.getIdTeam2()+" and DATEMATCH=TO_DATE('"+match.getDatematch()+"','YYYY-MM-DD') and BO="+match.getNbrMap()+" ");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
        
           String transaction(String type,float montant) throws JSONException{
            String url = "https://backend-node-mbds272957.herokuapp.com/api/parier";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();     
            body.add("type", type);
            body.add("solde",String. valueOf(montant));
            
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            
            HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);

            ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            JSONObject json = new JSONObject(response.getBody().toString());
            String val = json.getString("message");
            return val;
        }
      
        MatchAPI getmatchbyIdRivalryWithOutOdds(int idRivalry) throws SQLException, JSONException{
            MatchAPI val = new MatchAPI();
            String url = "https://www.rivalry.com/api/v1/matches/"+idRivalry;
            JSONObject json =  getJSONAPI(url);
            JSONObject data = json.getJSONObject("data");
            //ID
            int id = data.getInt("id");
            
            //NOM
            String nomTeam1 = data.getJSONArray("competitors").getJSONObject(0).getString("name");
            String nomTeam2 = data.getJSONArray("competitors").getJSONObject(1).getString("name");
            
            //ID
            Team team1 = findTeambynomV2(nomTeam1);
            int idTeam1 = team1.getIdTeam();
            Team team2 = findTeambynomV2(nomTeam2);
            int idTeam2 = team2.getIdTeam();
            
            //Date
            String[] arrOfStr = data.getString("scheduled_at").split("T");
            Date datematch = Date.valueOf(arrOfStr[0]);
            
            //Logo
            String logoTeam1 = team1.getLogo();
            String logoTeam2 = team2.getLogo();
            
            //Time
            String time = data.getString("scheduled_at");
            
            //Tournois 
            String tournois = data.getJSONObject("tournament").getString("name");
            
            
            //NbrMap
            int nbrMap = getNbrMap(data);
            
            //Odds
            int sizeMarket = data.getJSONArray("markets").length()-1;
            JSONArray outcomes = data.getJSONArray("markets").getJSONObject(sizeMarket).getJSONArray("outcomes");
            float odds1 = 0;
            float odds2 = 0;
            if(outcomes.length()>0){
                odds1 = (float) outcomes.getJSONObject(0).getDouble("odds");
                odds2 = (float) outcomes.getJSONObject(1).getDouble("odds");
            }
            
            
            
            
            
            val = new MatchAPI(idTeam1,idTeam2,id,datematch,nomTeam1,nomTeam2,odds1,odds2,logoTeam1,logoTeam2,time,tournois,nbrMap);
            
            
            
            return val;
        }
        
         public int getNbrMap(JSONObject data) throws JSONException{
            int nbrMap = 1;
              while(nbrMap<=100){
                  String temp = "Map "+nbrMap+" - Winner";
                  int indice = nbrMap-1;
                  String name = data.getJSONArray("markets").getJSONObject(indice).getString("name");
                  if(temp.equals(name)){
                      nbrMap++;
                  }
                  else{
                      
                      break;
                  }
              }
              nbrMap = nbrMap-1;
              return nbrMap;
        }
        
         ArrayList<Paris> getAllParisNonFinie(OracleConnection co) throws SQLException{
            ArrayList<Paris> val = new ArrayList();
            Statement statement = co.createStatement();
            String req = "select * from Paris where statut=0";
            
             ResultSet res = statement.executeQuery(req);
             while (res.next()){
                 //int idParis, int idUser, int idMatch, int idTeam, String type, float montant, float odds, Date dateparis, int statut
                Paris temp = new Paris(res.getInt(1),res.getInt(2),res.getInt(3),res.getInt(4),res.getString(5),res.getFloat(6),res.getFloat(7),res.getDate(8),res.getInt(9));
                val.add(temp);
            }
            return val;
        }
      
         Match getMatchById(OracleConnection co,int id) throws SQLException{
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
         
          ArrayList<JSONObject> getMatchOpenDota(Match m) throws JSONException{
            JSONArray arrayMatch = null;
            ArrayList<JSONObject> val = new ArrayList();
            if(m.getIdTeam1()!=0){
                String url = "https://api.opendota.com/api/teams/"+m.getIdTeam1()+"/matches";
                arrayMatch = getJSONArrayAPI(url);
                if(m.getIdTeam2()!=0){
                    val = getAllMatchQuiConcorde(m.getIdTeam2(),m.getDatematch(),arrayMatch);
                }
                else{
                    System.out.println("ATTOOOOO");
                    val = getAllMatchQuiConcorde(m.getNomTeam2(),m.getDatematch(),arrayMatch);
                }
            }
            else{
                System.out.println("ATTOOOOO 2");
                String url = "https://api.opendota.com/api/teams/"+m.getIdTeam2()+"/matches";
                arrayMatch = getJSONArrayAPI(url);
                val = getAllMatchQuiConcorde(m.getNomTeam1(),m.getDatematch(),arrayMatch);
            }
            return val;
        }
          
          ArrayList<JSONObject> getAllMatchQuiConcorde(int idOpposingteam,Date datematch,JSONArray arrayMatch) throws JSONException{
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
        
        ArrayList<JSONObject> getAllMatchQuiConcorde(String nomOpposingteam,Date datematch,JSONArray arrayMatch) throws JSONException{
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
        
          Boolean comparerNom(String nom1,String nom2){
            int taille1 = nom1.length();
            int taille2 = nom2.length();

             if(taille1>taille2){
                   return nom1.contains(nom2);
              }
              else{
                   return nom2.contains(nom1);
              }
        }
        
         
          void finaliser() throws SQLException, JSONException{
            
            OracleConnection co = Connexion.getConnection();
            Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            try{
                ArrayList<Paris> listeParis = getAllParisNonFinie(co);
                for(int i=0;i<listeParis.size();i++){
                    System.out.println("idParis "+listeParis.get(i).getIdParis());
                    Match m = getMatchById(co,listeParis.get(i).getIdMatch());
                    System.out.println("idMatch "+listeParis.get(i).getIdMatch());
                    //si date est passée
                    if(m.getDatematch().compareTo(datenow)<=0){
                        ArrayList<JSONObject> listeMatch =  getMatchOpenDota(m);
                        System.out.println("Match hita "+listeMatch.size());
                        if(!listeMatch.isEmpty()){
                                if(listeParis.get(i).getType().compareTo("map_overall")==0){
                                    //traitement pour winner overall
                                    System.out.println("overall");
                                }
                                else{
                                    String[] array = listeParis.get(i).getType().split("_");
                                    int mapParier = Integer.parseInt(array[1]);
                                    if(listeMatch.size()>=mapParier){
                                        System.out.println("Type Paris "+listeParis.get(i).getType());
                                        if(listeParis.get(i).getType().contains("fb")){
                                            //MANEMANY ETO FA TSY AIKO MIJERY OE IZA FIRST BLOOD
                                            System.out.println("map "+mapParier+" firstblood");
                                        }
                                        else{
                                            //traitement  pour winner map ?
                                            System.out.println("map "+mapParier);
                                        }
                                    }
                                }
                        }
                        else{
                            long diffInMillies = Math.abs(m.getDatematch().getTime() - datenow.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            System.out.println("difference de jour "+diff);
                            if(diff>1){
                                //Envoie mail
                                System.out.println("mila mihetsika fa tsy hita paris an'olona");
                            }
                             System.out.println("empty");
                             
                        }
                    }
                    System.out.println("#################################################################################");
                }
            }
            finally{
                if(co!=null){
                    co.close();
                }
            } 
            
        }
         
	@Test
	void contextLoads() throws SQLException, JSONException{
                    /*
                    OracleConnection co = Connexion.getConnection();
                    try{
                        ArrayList<Paris> val = getAllParisNonFinie(co);
                        System.out.println("taille "+val.size());
                        System.out.println("date "+val.get(1).getDateparis());
                    }
                    finally{
                        if(co!=null){
                            co.close();
                        }
                    }*/
                   finaliser();

                    
                   
                 }
	


}