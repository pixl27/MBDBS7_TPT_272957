package com.example.java;

import classe.Connexion;
import classe.Match;
import classe.MatchAPI;
import classe.NotifWeb;
import classe.Paris;
import classe.Team;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
      
      void insererNotifWeb(OracleConnection connection,String idUser,String token) throws SQLException{
           
            Statement statement = null;
            try{
                statement = connection.createStatement();
           
                statement.executeQuery("insert into NOTIFWEB values(SEQUENCE_NOTIFWEB.NEXTVAL,'"+idUser+"','"+token+"') ");
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
    }
      
      int getDoublonNotifWeb(OracleConnection co,String idUser,String token) throws SQLException{
            int val = 0;
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDNOTIF from NOTIFWEB where IDUSER='"+idUser+"' and  TOKEN='"+token+"' ");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
      
        void insererNotifWeb(String idUser,String token) throws SQLException{
            OracleConnection co = Connexion.getConnection();
            try{
                int i = getDoublonNotifWeb(co,idUser,token);
                if(i==0){
                    insererNotifWeb(co,idUser,token);
                }
            }
            finally{
                co.close();
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
        //"type":"debit","iduser":"60d995cb5f11d836229bd7e0","montant":100000,"idparis":81,"description":"cadeau admin","datehistorique":"2021-07-03T21:00:00.000+00:00"
         String transaction(String idUser,String type,float montant,int idParis,String description) throws JSONException{
            String url = "https://backend-node-mbds272957.herokuapp.com/api/parier";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();   
            body.add("iduser",idUser);
            body.add("idparis", String.valueOf(idParis));
            body.add("datehistorique", dateFormat.format(datenow));
            body.add("description", description);
            body.add("type", type);
            body.add("montant",String.valueOf(montant));
            
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            
            HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);

            ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            JSONObject json = new JSONObject(response.getBody().toString());
            String val = json.getString("message");
            return val;
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
           
           String sendNotificationToWeb(String token,String idUser,String title,String message) throws JSONException{
            String url = "https://fcm.googleapis.com/fcm/send";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("Authorization", "key=AAAAl8TtRzE:APA91bEO6IgPg8_LkuRvUmwSyVRLjKA8IRknrBn6_QHuYGha-Q5pAF-Rs6a-1_K-ddM8izqy08471B53jrFhLj9q2zhlVtCSoiA0W3skF2m6Ff2AXMr8pjwpMjiaHSiM-MqQHe7aStXN");
            headers.add("Content-Type","application/json");
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();  
            
            JSONObject data = new JSONObject()
                    .put("idUser",idUser)
                    .put("title", title)
                    .put("body", message);
            
            String jsonBody = new JSONObject()
                  .put("to", token)
                  .put("data", data)
                  .toString();
            
            
            System.out.println("jsonBody "+jsonBody);
            
            

            
            
            HttpEntity<?> httpEntity = new HttpEntity<Object>(jsonBody, headers);

            ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            JSONObject json = new JSONObject(response.getBody().toString());
            String val = json.getString("success");
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
                Paris temp = new Paris(res.getInt(1),res.getString(2),res.getInt(3),res.getInt(4),res.getString(5),res.getFloat(6),res.getFloat(7),res.getDate(8),res.getInt(9));
                val.add(temp);
            }
            return val;
        }
         
         void setStatusParis(OracleConnection co,int idParis) throws SQLException{
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
         
          ArrayList<JSONObject> getMatchOpenDota(Match m) throws JSONException, SQLException{
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
          
          
        
        ArrayList<JSONObject> getAllMatchQuiConcorde(String nomOpposingteam,Date datematch,JSONArray arrayMatch) throws JSONException, SQLException{
            ArrayList<JSONObject> val = new ArrayList();
            int size = arrayMatch.length()-1;
            int idAdversaire = 0;
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
                     idAdversaire = arrayMatch.getJSONObject(i).getInt("opposing_team_id");
                    }
                }
                else{
                    System.out.println("Date passé, Break");
                    break;
                }
            }
            //inserer la team adversaire dans notre base de donnée 
            if(idAdversaire!=0){
                OracleConnection co = Connexion.getConnection();
                try{
                int test = getDoublonTeam(co,idAdversaire);

                if(test == 0){
                    System.out.println("La team"+nomOpposingteam+"n'est pas encore dans la base de donnée");
                    System.out.println("Insertion de "+nomOpposingteam+" avec comme ID "+idAdversaire);
                    Team t = getTeamOPENDOTA(idAdversaire);
                    insererTeam(co,t);
                }
                }
                finally{
                    if(co!=null)
                        co.close();
                }
            }
            return val;
        }
        
          void insererTeam(OracleConnection connection,Team t) throws SQLException{
           
            PreparedStatement statement = null;
            try{
                
                String req = "insert into Team values(?,?,?,?)";
                
                
                statement = connection.prepareStatement(req);
                statement.setInt(1, t.getIdTeam());
                statement.setString(2, t.getNom());
                statement.setString(3,t.getTag());
                statement.setString(4,t.getLogo());
           
                statement.executeQuery();
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
        }
        
        Team getTeamOPENDOTA(int idTeam) throws JSONException{
            
            String url = "https://api.opendota.com/api/teams/"+idTeam;
            JSONObject teamJson = getJSONAPI(url);
            int id = teamJson.getInt("team_id");
            //int idTeam, String nom, String tag, String logo
            String nom = teamJson.getString("name");
            String tag = teamJson.getString("tag");
            String logo = teamJson.getString("logo_url");
            Team val = new Team(id,nom,tag,logo);
            return val;
        }
        
        int getDoublonTeam(OracleConnection co,int idTeam) throws SQLException{
            
            int val = 0;
            Statement statement = null;
            try{
            statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM from TEAM where IDTEAM="+idTeam+"");
                while (resultSet.next()){
                    val = resultSet.getInt(1);
                }
            }
            finally{
                if(statement!=null)
                    statement.close();
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
          
         
          int getWhoDoTheFB(long idMatch) throws JSONException{
              int val = 0;
              String url = "https://api.opendota.com/api/matches/"+idMatch;
              System.out.println("url get Match Open Dota "+url);
              JSONObject match = getJSONAPI(url);
              JSONArray listeOfPlayer = match.getJSONArray("players");
              Boolean isRadiant = false;
              for(int i=0;i<listeOfPlayer.length();i++){
                  if(!listeOfPlayer.getJSONObject(i).isNull("firstblood_claimed")){
                    int isDoFb = listeOfPlayer.getJSONObject(i).getInt("firstblood_claimed");
                    if(isDoFb==1){
                        System.out.println("player "+listeOfPlayer.getJSONObject(i).getString("name"));
                        isRadiant = listeOfPlayer.getJSONObject(i).getBoolean("isRadiant");
                        break;
                    }
                  }
              }
              if(isRadiant){
                  JSONObject team  = match.getJSONObject("radiant_team");
                  val = team.getInt("team_id");
              }
              else{
                  JSONObject team  = match.getJSONObject("dire_team");
                  val = team.getInt("team_id");
              }
              return val;
          }
        
          
          
         void traitementParis(Paris p,String type,String description) throws SQLException, JSONException{
             OracleConnection co = Connexion.getConnection();
             co.setAutoCommit(false);
             try{
                 float montant = p.getOdds()*p.getMontant();
                 System.out.println("Montant "+montant);
                 if("credit".compareTo(type)==0){
                     montant = 0;
                 }
                 System.out.println("idUser "+p.getIdUser());
                 System.out.println("Montant "+montant);
                 System.out.println("type "+type);
                 System.out.println("idParis "+p.getIdParis());
                 System.out.println("desctiption "+description);
                 transaction(p.getIdUser(),type,montant,p.getIdParis(),description);
                 setStatusParis(co,p.getIdParis());
                 co.commit();
             }
             finally{
                     co.close();
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
                                    int nbrWinNeeded = (m.getNbrMap()/2)+1;
                                    int nbrWinTeam1 = 0;
                                    int nbrWinTeam2 = 0;
                                    for(int y=0;y<listeMatch.size();y++){
                                        if(listeMatch.get(y).getBoolean("radiant_win")==listeMatch.get(y).getBoolean("radiant")){
                                            if(m.getIdTeam1()!=0)
                                                nbrWinTeam1++;
                                            else
                                                nbrWinTeam2++;
                                        }
                                        else{
                                            if(m.getIdTeam1()!=0)
                                                nbrWinTeam2++;
                                            else
                                                nbrWinTeam1++;
                                        }
                                    }
                                    
                                    System.out.println("nbrWinNeeded "+nbrWinNeeded);
                                    System.out.println("nbrWinTeam1 "+nbrWinTeam1);
                                    System.out.println("nbrWinTeam2 "+nbrWinTeam2);
                                    
                                    if(nbrWinTeam1==nbrWinNeeded){
                                            System.out.println(m.getNomTeam1() +"Winner");
                                            //traitement pour team 1 Winner
                                                if(m.getIdTeam1()==listeParis.get(i).getIdTeam()){
                                                    //paris gagnant 
                                                    String description = "Felicitation, Votre équipe a gagner pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"debit",description);
                                                }
                                                else{
                                                    //paris perdant 
                                                    String description = "Malheuresement, Votre équipe a perdu pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"credit",description);
                                                }
                                    }
                                    else if(nbrWinTeam2==nbrWinNeeded){
                                            System.out.println(m.getNomTeam2() +"Winner");
                                            //traitement pour team 2 Winner
                                            if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                    //paris gagnant 
                                                    String description = "Felicitation, Votre équipe a gagner pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"debit",description);
                                             }
                                             else{
                                                    //paris perdant 
                                                    String description = "Malheuresement, Votre équipe a perdu pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"credit",description);
                                             }
                                    }
                                    else
                                            System.out.println("En attente.....");
                                            //Next fa mbola tsy vita 
                                    
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
                                            if(listeMatch.size()>=mapParier){
                                                int indiceMatch = mapParier -1;
                                                long idMatchOpenDota = listeMatch.get(indiceMatch).getLong("match_id");
                                                System.out.println("idMatchOPENDOTA "+idMatchOpenDota);
                                                int idTeamWhoDoFB = getWhoDoTheFB(idMatchOpenDota);
                                                if(m.getIdTeam1()!=0){
                                                    if(m.getIdTeam1()==idTeamWhoDoFB){
                                                        //Team1 do the first Blood 
                                                        System.out.println(m.getNomTeam1()+ " do the first blood");
                                                        if(m.getIdTeam1()==listeParis.get(i).getIdTeam()){
                                                            //paris gagnant 
                                                            System.out.println("ato");
                                                            String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            System.out.println("ato2");
                                                            String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"credit",description);
                                                        }
                                                    }
                                                    else{
                                                        //Team2 do the first Blood 
                                                        System.out.println("ato3");
                                                        System.out.println(m.getNomTeam2()+ " do the first blood");
                                                        if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                                //paris gagnant 
                                                                System.out.println("ato4");
                                                                String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"debit",description);
                                                        }
                                                        else{
                                                                //paris perdant 
                                                                System.out.println("ato5");
                                                                String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"credit",description);
                                                        }
                                                    }
                                                }
                                                else{
                                                    if(m.getIdTeam2()==idTeamWhoDoFB){
                                                        //Team2 do the first Blood 
                                                        System.out.println(m.getNomTeam2()+ " do the first blood");
                                                        if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                                //paris gagnant 
                                                                System.out.println("ato6");
                                                                String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"debit",description);
                                                        }
                                                        else{
                                                                //paris perdant 
                                                                System.out.println("ato 7");
                                                                String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"credit",description);
                                                        }
                                                    }
                                                    else{
                                                        //Team1 do the first Blood 
                                                        System.out.println(m.getNomTeam1()+ " do the first blood");
                                                        if(m.getIdTeam1()==listeParis.get(i).getIdTeam()){
                                                            System.out.println("ato8");
                                                            //paris gagnant 
                                                            String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"debit",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            System.out.println("ato 9");
                                                            String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"credit",description);
                                                        }
                                                    }
                                                }
                                                
                                            }
                                        }
                                        else{
                                            //traitement  pour winner map ?
                                            if(listeMatch.size()>=mapParier){
                                                
                                                int indiceMatch = mapParier -1;
                                                if(listeMatch.get(indiceMatch).getBoolean("radiant_win")==listeMatch.get(indiceMatch).getBoolean("radiant")){
                                                    if(m.getIdTeam1()!=0){
                                                        System.out.println(m.getNomTeam1()+" Winner map "+mapParier);
                                                        //Traitement pour team 1 Winner
                                                        if(m.getIdTeam1()==listeParis.get(i).getIdTeam()){
                                                            //paris gagnant 
                                                            String description = "Felicitation, Votre équipe a gagné sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                        }
                                                    }
                                                    else{
                                                        System.out.println(m.getNomTeam2()+" Winner map "+mapParier);
                                                        //Traitement pour team 2 Winnner 
                                                        if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                            //paris gagnant 
                                                            String description = "Felicitation, Votre équipe a gagné sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                        }
                                                    }
                                                        
                                                }
                                                else{
                                                    if(m.getIdTeam1()!=0){
                                                        System.out.println(m.getNomTeam2()+" Winner map "+mapParier);
                                                        //Traitement pour team 2 Winner
                                                        if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                                //paris gagnant 
                                                                 String description = "Felicitation, Votre équipe a gagné sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                            }
                                                            else{
                                                                //paris perdant 
                                                                 String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                            }
                                                    }
                                                    else{
                                                        System.out.println(m.getNomTeam1()+" Winner map "+mapParier);
                                                        //Traitement pour team 1 Winnner 
                                                        if(m.getIdTeam1()==listeParis.get(i).getIdTeam()){
                                                                //paris gagnant 
                                                                 String description = "Felicitation, Votre équipe a gagné sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                            }
                                                            else{
                                                                //paris perdant 
                                                                   String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                        }
                                                    }
                                                        
                                                }
                                            }
                                            else {
                                                //Check sode efa vita le match
                                                int nbrWinNeeded = (m.getNbrMap() / 2) + 1;
                                                int nbrWinTeam1 = 0;
                                                int nbrWinTeam2 = 0;
                                                for (int y = 0; y < listeMatch.size(); y++) {
                                                    if (listeMatch.get(y).getBoolean("radiant_win") == listeMatch.get(y).getBoolean("radiant")) {
                                                        if (m.getIdTeam1() != 0) {
                                                            nbrWinTeam1++;
                                                        } else {
                                                            nbrWinTeam2++;
                                                        }
                                                    } else {
                                                        if (m.getIdTeam1() != 0) {
                                                            nbrWinTeam2++;
                                                        } else {
                                                            nbrWinTeam1++;
                                                        }
                                                    }
                                                }
                                                //ra efa vita le match
                                                if(nbrWinTeam1==nbrWinNeeded || nbrWinTeam2==nbrWinNeeded){
                                                    //Paris perdu par le parieur
                                                    String description = "Malheuresement, le map "+mapParier+" n'a pas eu lieu pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"credit",description);
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                        else{
                            
                            System.out.println("empty match");
                            long diffInMillies = Math.abs(m.getDatematch().getTime() - datenow.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            System.out.println("difference de jour "+diff);
                            if(diff>2){
                                //Envoie mail
                                System.out.println("mila mihetsika fa tsy hita paris an'olona");
                                
                            }
                             
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
          
          
          
             ArrayList<NotifWeb> getAllNotifWeb(String idUser) throws SQLException{
                        OracleConnection co = Connexion.getConnection();
                   ArrayList<NotifWeb> val = new ArrayList();
                   Statement statement = null;

                   try{
                       statement = co.createStatement();

                        ResultSet resultSet = statement.executeQuery("select TOKEN from NOTIFWEB where IDUSER ='"+idUser+"' ");
                           while (resultSet.next()){
                               NotifWeb temp = new NotifWeb(idUser,resultSet.getString(1));
                               //int idMatch, int idTeam1, int idTeam2, Date datematch, int nbrMap, String nomTeam1, String nomTeam2
                               val.add(temp);
                           }
                   }
                   finally{
                       if(statement!=null){
                           statement.close();
                       }
                       if(co!=null){
                           co.close();
                       }
                   }


                   return val;
            }
             
             void sendNotificationWebToAllDeviceForUser(String idUser,String title,String message) throws SQLException, JSONException{
                 ArrayList<NotifWeb> listeToken = getAllNotifWeb(idUser);
                 System.out.println("listeToken :"+listeToken.size());
                 for(int i =0;i<listeToken.size();i++){
                     String val = sendNotificationToWeb(listeToken.get(i).getToken(),idUser,title,message);
                     if(val.compareTo("1")==0)
                         System.out.println("Notification envoyé");
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
                   //finaliser();
                   String idUser = "60d995cb5f11d836229bd7e0";
                   String token = "c48tnT0oOtkVlOBCL2XdIu:APA91bGgR2cvJPVaXsE1gGF0Vypya4Z7jYp4sSY12-FPZQdeFMtO87nd06t5IPnZW5_9-sEvmdJDv_DUyCeaO7iheRqArzRDOxgwAw18BAvw8d6w2QFDWTfJvXbMJTyWjYBojbiRf7E_";      
                   String title = "Malheuresement";
                   String message = "Hello";
                   //insererNotifWeb(idUser,token);
                   
                   //String token,String idUser,String title,String message
                   //String val = sendNotificationToWeb(token,idUser,"Malheuresement","Hello");
                    //System.out.println("Success: "+val);
                    
                    sendNotificationWebToAllDeviceForUser(idUser,title,message);
                   
                 }
	


}