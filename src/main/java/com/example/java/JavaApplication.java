package com.example.java;

import classe.Connexion;
import classe.Match;

import classe.MatchAPI;
import classe.Paris;
import classe.ParisArg;
import classe.Team;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.repeatSecondlyForever;
import static org.quartz.TriggerBuilder.newTrigger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.OracleConnection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;





@Controller
@SpringBootApplication
@CrossOrigin
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class JavaApplication {
     // The recommended format of a connection URL is the long format with the
    // connection descriptor.
   @Autowired
private RestTemplate restTemplate;

        @RequestMapping("/")
        @ResponseBody
        String home(){
            return "x";
        }
        
        @RequestMapping("/hello")
        @ResponseBody
        String hello(){
            return "Heelo man";
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
    
    JSONObject getJSONAPI(String url){
         HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          JSONObject json = new JSONObject(response.getBody().toString());
          return json;
    }
    
    String transaction(String type,float montant){
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
    
    JSONArray getJSONArrayAPI(String url){
         HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          
          JSONArray jsonarray = new JSONArray(response.getBody().toString());
          return jsonarray;
    }
    
    int getStartTime(int idTeam){
          String url = "https://api.opendota.com/api/teams/"+idTeam+"/matches";
          JSONArray array = getJSONArrayAPI(url);
          int val = array.getJSONObject(0).getInt("start_time");
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
        
        Team findteambynom(String nom) throws SQLException{
            OracleConnection connection = Connexion.getConnection();
           Statement statement = null;
           Team val = new Team();
           
            try{
            
                statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("select IDTEAM,LOGO from Team where nom like '%"+nom+"%'");

                
                    while (resultSet.next()){
                        val.setIdTeam(resultSet.getInt(1));
                        val.setLogo(resultSet.getString(2));
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
        
        int findidteambynom(String nom) throws SQLException{
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
        
        int findteambynom(OracleConnection co,String nom) throws SQLException{
           
           
            
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom like '%"+nom+"%'");
            int id = 0; 
            while (resultSet.next())
                id = resultSet.getInt(1);
            
            return id;
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
           
            PreparedStatement statement = null;
            try{
                
                String req = "insert into Match values(MATCH_SEQ.NEXTVAL,?,?,?,?,?,?)";
                
                
                statement = connection.prepareStatement(req);
                statement.setInt(1, match.getIdTeam1());
                statement.setInt(2, match.getIdTeam2());
                statement.setDate(3, match.getDatematch());
                statement.setInt(4, match.getNbrMap());
                statement.setString(5,match.getNomTeam1());
                statement.setString(6,match.getNomTeam2());
           
                statement.executeQuery();
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
            }
        }
        
        static void insererTest() throws SQLException{
             
            OracleConnection connection = Connexion.getConnection();
           
            PreparedStatement statement = null;
            try{
                String query = "insert into TEST values('tous les 3 HEURES',?)";
                statement = connection.prepareStatement(query);
                statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement.executeQuery();
            }
            finally{
                if(statement!=null){
                    statement.close();
                }
                if(connection!=null){
                    connection.close();
                }
            }
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
        
        
        
       
        
        @Bean
        public RestTemplate restTemplate() {
        return new RestTemplate();
        }
        
      
        
        
        
        @PostMapping(value = "/parier", consumes = "application/json", produces = "application/json")
        @ResponseBody
        String parier(@RequestBody ParisArg p) throws SQLException{
            MatchAPI m = getmatchbyIdRivalry(p.getIdMatch());
            String message = "";
            
            OracleConnection oc = Connexion.getConnection();
            oc.setAutoCommit(false);
            try{
                int idDoublon = getDoublonMatch(oc,m);
                Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                if(idDoublon==0){
                    insererMatch(oc,m);
                    int idMatchTemp = getSequence(oc,"MATCH_SEQ");
                    insererParis(oc,p.getIdUser(),idMatchTemp,p.getIdTeamParier(),p.getType(),p.getMontant(),p.getOdds(),datenow,0);
                }
                else{
                    insererParis(oc,p.getIdUser(),idDoublon,p.getIdTeamParier(),p.getType(),p.getMontant(),p.getOdds(),datenow,0);
                }
                String val = transaction("credit",p.getMontant());
                if(val.compareToIgnoreCase("updated")==0){
                    oc.commit();
                }
                else{
                    message = "erreur de payement";
                }
            }
            finally{
                 if(oc!=null){
                    oc.close();
                }
                 
            }
            message = "updated";
            return message;
        }
        
        
        @GetMapping(path="/getallteam", produces = "application/json")
        @ResponseBody
        ArrayList<Team> getAllTeam() throws SQLException{
            
           OracleConnection connection = Connexion.getConnection();
           Statement statement = null;
           ArrayList<Team> listeTeam = new ArrayList(); 
            try{
                statement = connection.createStatement();
           
           ResultSet resultSet = statement.executeQuery("select * from Team");
           
            while (resultSet.next()){
                Team temp = new Team(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                listeTeam.add(temp);
            }
            }finally{
                if(statement!=null){
                    statement.close();
                }
                if(connection!=null){
                    connection.close();
                }
            }
           
                
            
            return listeTeam;
        }
        
        
        ArrayList<Paris> getAllParisNonFinie(OracleConnection co) throws SQLException{
            ArrayList<Paris> val = new ArrayList();
            Statement statement = co.createStatement();
            String req = "select * from Paris where statut=0 order by dateparis";
            
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
        
        
        
        int getDoublonMatch(OracleConnection co,MatchAPI match) throws SQLException{
            int val = 0;
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDMATCH from MATCH where IDTEAMRADIANT="+match.getIdTeam1()+" and IDTEAMDIRE="+match.getIdTeam2()+" and DATEMATCH=TO_DATE('"+match.getDatematch()+"','YYYY-MM-DD') and BO="+match.getNbrMap()+" ");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
        
        @GetMapping(path="/getallmatch", produces = "application/json")
        @ResponseBody
        ArrayList<MatchAPI> getAllMatch() throws SQLException, JSONException{
            
           
             
           ArrayList<MatchAPI> val = new ArrayList<>();
          String url = "https://www.rivalry.com/api/v1/matches?game_id=3";
          
          HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          JSONObject json = new JSONObject(response.getBody().toString());
          
          
          JSONArray array = json.getJSONArray("data");
          
          
          
          Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
          for(int i=0;i<array.length();i++){
              int idRivalry = array.getJSONObject(i).getInt("id");
              String tournois = array.getJSONObject(i).getJSONObject("tournament").getString("name");
              
              JSONArray arrayTeam = array.getJSONObject(i).getJSONArray("competitors");
              
              JSONArray arrayOdds = array.getJSONObject(i).getJSONArray("markets").getJSONObject(0).getJSONArray("outcomes");
              
              int nbrMap = 1;
              while(nbrMap<=100){
                  String temp = "Map "+nbrMap+" - Winner";
                  int indice = nbrMap-1;
                  String name = array.getJSONObject(i).getJSONArray("markets").getJSONObject(indice).getString("name");
                  if(temp.equals(name)){
                      nbrMap++;
                  }
                  else{
                      
                      break;
                  }
              }
              nbrMap = nbrMap-1;
              
              float odds1 = 0;
              float odds2 = 0;
              if(arrayOdds.length()>0){
                  odds1 = (float) arrayOdds.getJSONObject(0).getDouble("odds");
                  odds2 = (float) arrayOdds.getJSONObject(1).getDouble("odds");
              }
              
              
            
            
            String nomTeam1 = arrayTeam.getJSONObject(0).getString("name");
            Team team1 = findTeambynomV2(nomTeam1);
            int idTeam1 = team1.getIdTeam();
            String logoTeam1 = team1.getLogo();
            
            
            
            String nomTeam2 = arrayTeam.getJSONObject(1).getString("name");
            Team team2 = findTeambynomV2(nomTeam2);
            int idTeam2 = team2.getIdTeam();
            String logoTeam2 = team2.getLogo();
            
            String time = array.getJSONObject(i).getString("scheduled_at");
            
            String[] arrOfStr = array.getJSONObject(i).getString("scheduled_at").split("T");
            
            Date datematch = Date.valueOf(arrOfStr[0]);
            
              
              
              //si tous les equipes sont presentent dans notre BD
              if(idTeam1!=0 || idTeam2!=0){
                 
                  if(datematch.compareTo(datenow)<=0){
                     
                      //int idTeam1, int idTeam2, int idMatchRivalry, Date datematch, String nomTeam1, String nomTeam2, float odds1, float odds2, String logo, String time, String tournois
                      MatchAPI temp = new MatchAPI(idTeam1,idTeam2,idRivalry,datematch,nomTeam1,nomTeam2,odds1,odds2,logoTeam1,logoTeam2,time,tournois,nbrMap);
                      
                      val.add(temp);
                     
                  }
                  else{
                      break;
                  }
              }
             
          }
          return val;
          
        }
        
        int getIndiceMarketByName(JSONArray market,String name){
            int val = 0;
            int size = market.length()-1;
            //reverse boucle, optimisation
            for(int i=size;i>0;i--){
                String nom = market.getJSONObject(i).getString("name");
                System.out.println("nom dans API"+nom);
                System.out.println("nom ako"+name);
                if(nom.compareToIgnoreCase(name)==0){
                    System.out.println("tafiditra");
                    val = i;
                    break;
                }
            }
            return val;
        }
        
        MatchAPI getmatchbyIdRivalryWithOutOdds(int idRivalry) throws SQLException{
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
        
        @GetMapping(path="/getmatchbyIdRivalry", produces = "application/json")
        @ResponseBody
        MatchAPI getmatchbyIdRivalry(@RequestParam int idRivalry) throws SQLException{
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
            
            
            //Odds MAP 1
            JSONArray outcomesMap1 = data.getJSONArray("markets").getJSONObject(0).getJSONArray("outcomes");
            float odd1_map1 = 0;
            float odd2_map1 = 0;
            if(outcomesMap1.length()>0){
                odd1_map1 = (float) outcomesMap1.getJSONObject(0).getDouble("odds");
                odd2_map1 = (float) outcomesMap1.getJSONObject(1).getDouble("odds");
            }
            
            //Odds MAP 2
            float odd1_map2 = 0;
            float odd2_map2 = 0;
            if(nbrMap>1){
                JSONArray outcomesMap2 = data.getJSONArray("markets").getJSONObject(1).getJSONArray("outcomes");
                if(outcomesMap2.length()>0){
                    odd1_map2 = (float) outcomesMap2.getJSONObject(0).getDouble("odds");
                    odd2_map2 = (float) outcomesMap2.getJSONObject(1).getDouble("odds");
                }
            }

            //Odds MAP 3
            float odd1_map3 = 0;
            float odd2_map3 = 0;
            if(nbrMap>2){
                JSONArray outcomesMap3 = data.getJSONArray("markets").getJSONObject(2).getJSONArray("outcomes");
                if(outcomesMap3.length()>0){
                    odd1_map3 = (float) outcomesMap3.getJSONObject(0).getDouble("odds");
                    odd2_map3 = (float) outcomesMap3.getJSONObject(1).getDouble("odds");
                }
            }
            
            //Odds MAP 4
            float odd1_map4 = 0;
            float odd2_map4 = 0;
            if(nbrMap>3){
                JSONArray outcomesMap4 = data.getJSONArray("markets").getJSONObject(3).getJSONArray("outcomes");
                if(outcomesMap4.length()>0){
                    odd1_map4 = (float) outcomesMap4.getJSONObject(0).getDouble("odds");
                    odd2_map4 = (float) outcomesMap4.getJSONObject(1).getDouble("odds");
                }
            }
            
            //Odds MAP 5
            float odd1_map5 = 0;
            float odd2_map5 = 0;
            if(nbrMap>4){
                JSONArray outcomesMap5 = data.getJSONArray("markets").getJSONObject(4).getJSONArray("outcomes");
                if(outcomesMap5.length()>0){
                    odd1_map5 = (float) outcomesMap5.getJSONObject(0).getDouble("odds");
                    odd2_map5 = (float) outcomesMap5.getJSONObject(1).getDouble("odds");
                }
            }
            
            //odds fb map1
            String nameinAPI_map1 = "Map 1 Team to Draw First Blood";
            int indice_fb_map1 = getIndiceMarketByName(data.getJSONArray("markets"),nameinAPI_map1);
            float odd1_fb_map1 = 0;
            float odd2_fb_map1 = 0;
            if(indice_fb_map1!=0){
                JSONArray outcomesTemp = data.getJSONArray("markets").getJSONObject(indice_fb_map1).getJSONArray("outcomes");
                if(outcomesTemp.length()>0){
                    odd1_fb_map1 = (float) outcomesTemp.getJSONObject(0).getDouble("odds");
                    odd2_fb_map1 = (float) outcomesTemp.getJSONObject(1).getDouble("odds");
                }
            }
            
            
            //odds fb map2
            String nameinAPI_map2 = "Map 2 Team to Draw First Blood";
            int indice_fb_map2 = getIndiceMarketByName(data.getJSONArray("markets"),nameinAPI_map2);
            float odd1_fb_map2 = 0;
            float odd2_fb_map2 = 0;
            if(indice_fb_map2!=0){
                JSONArray outcomesTemp = data.getJSONArray("markets").getJSONObject(indice_fb_map2).getJSONArray("outcomes");
                if(outcomesTemp.length()>0){
                    odd1_fb_map2 = (float) outcomesTemp.getJSONObject(0).getDouble("odds");
                    odd2_fb_map2 = (float) outcomesTemp.getJSONObject(1).getDouble("odds");
                }
            }
            
            //odds fb map3
            String nameinAPI_map3 = "Map 3 Team to Draw First Blood";
            int indice_fb_map3 = getIndiceMarketByName(data.getJSONArray("markets"),nameinAPI_map3);
            float odd1_fb_map3 = 0;
            float odd2_fb_map3 = 0;
            if(indice_fb_map3!=0){
                JSONArray outcomesTemp = data.getJSONArray("markets").getJSONObject(indice_fb_map3).getJSONArray("outcomes");
                if(outcomesTemp.length()>0){
                    odd1_fb_map3 = (float) outcomesTemp.getJSONObject(0).getDouble("odds");
                    odd2_fb_map3 = (float) outcomesTemp.getJSONObject(1).getDouble("odds");
                }
            }
            
            //odds fb map4
            String nameinAPI_map4 = "Map 4 Team to Draw First Blood";
            int indice_fb_map4 = getIndiceMarketByName(data.getJSONArray("markets"),nameinAPI_map4);
            float odd1_fb_map4 = 0;
            float odd2_fb_map4 = 0;
            if(indice_fb_map4!=0){
                JSONArray outcomesTemp = data.getJSONArray("markets").getJSONObject(indice_fb_map4).getJSONArray("outcomes");
                if(outcomesTemp.length()>0){
                    odd1_fb_map4 = (float) outcomesTemp.getJSONObject(0).getDouble("odds");
                    odd2_fb_map4 = (float) outcomesTemp.getJSONObject(1).getDouble("odds");
                }
            }
            
            //odds fb map5
            String nameinAPI_map5 = "Map 5 Team to Draw First Blood";
            int indice_fb_map5 = getIndiceMarketByName(data.getJSONArray("markets"),nameinAPI_map5);
            float odd1_fb_map5 = 0;
            float odd2_fb_map5 = 0;
            if(indice_fb_map5!=0){
                JSONArray outcomesTemp = data.getJSONArray("markets").getJSONObject(indice_fb_map5).getJSONArray("outcomes");
                if(outcomesTemp.length()>0){
                    odd1_fb_map5 = (float) outcomesTemp.getJSONObject(0).getDouble("odds");
                    odd2_fb_map5 = (float) outcomesTemp.getJSONObject(1).getDouble("odds");
                }
            }
            
            
            
            val = new MatchAPI(idTeam1,idTeam2,id,datematch,nomTeam1,nomTeam2,odds1,odds2,logoTeam1,logoTeam2,time,tournois,nbrMap);
            
            val.setOdd1_map1(odd1_map1);
            val.setOdd1_map2(odd1_map2);
            val.setOdd1_map3(odd1_map3);
            val.setOdd1_map4(odd1_map4);
            val.setOdd1_map5(odd1_map5);
            
            val.setOdd2_map1(odd2_map1);
            val.setOdd2_map2(odd2_map2);
            val.setOdd2_map3(odd2_map3);
            val.setOdd2_map4(odd2_map4);
            val.setOdd2_map5(odd2_map5);
            
            val.setOdd1_fb_map1(odd1_fb_map1);
            val.setOdd1_fb_map2(odd1_fb_map2);
            val.setOdd1_fb_map3(odd1_fb_map3);
            val.setOdd1_fb_map4(odd1_fb_map4);
            val.setOdd1_fb_map5(odd1_fb_map5);
            
            val.setOdd2_fb_map1(odd2_fb_map1);
            val.setOdd2_fb_map2(odd2_fb_map2);
            val.setOdd2_fb_map3(odd2_fb_map3);
            val.setOdd2_fb_map4(odd2_fb_map4);
            val.setOdd2_fb_map5(odd2_fb_map5);
            
            return val;
        }
        
        public int getNbrMap(JSONObject data){
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
        
        @GetMapping(path="/getallmatchtest", produces = "application/json")
        @ResponseBody
        ArrayList<MatchAPI> getAllMatchTest() throws SQLException, JSONException{
            
           
             
           ArrayList<MatchAPI> val = new ArrayList<>();
          String url = "https://www.rivalry.com/api/v1/matches?game_id=3";
          
          HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          JSONObject json = new JSONObject(response.getBody().toString());
          
          
          JSONArray array = json.getJSONArray("data");
          
          
          
          Calendar c = Calendar.getInstance(); 
             c.add(Calendar.DATE, 14);
            Date datenow = new java.sql.Date(c.getTimeInMillis());
          for(int i=0;i<10;i++){
              int idRivalry = array.getJSONObject(i).getInt("id");
              String tournois = array.getJSONObject(i).getJSONObject("tournament").getString("name");
              
              JSONArray arrayTeam = array.getJSONObject(i).getJSONArray("competitors");
              
              int sizeMarket = array.getJSONObject(i).getJSONArray("markets").length()-1;
              String nameTest = array.getJSONObject(i).getJSONArray("markets").getJSONObject(sizeMarket).getString("name");
             
            
              JSONArray arrayOdds = array.getJSONObject(i).getJSONArray("markets").getJSONObject(sizeMarket).getJSONArray("outcomes");
              
              int nbrMap = 1;
              while(nbrMap<=100){
                  String temp = "Map "+nbrMap+" - Winner";
                  int indice = nbrMap-1;
                  String name = array.getJSONObject(i).getJSONArray("markets").getJSONObject(indice).getString("name");
                  if(temp.equals(name)){
                      nbrMap++;
                  }
                  else{
                      
                      break;
                  }
              }
              nbrMap = nbrMap-1;
              
              float odds1 = 0;
              float odds2 = 0;
              if(arrayOdds.length()>0){
                  odds1 = (float) arrayOdds.getJSONObject(0).getDouble("odds");
                  odds2 = (float) arrayOdds.getJSONObject(1).getDouble("odds");
              }
              
              
            
            
            String nomTeam1 = arrayTeam.getJSONObject(0).getString("name");
            Team team1 = findTeambynomV2(nomTeam1);
            int idTeam1 = team1.getIdTeam();
            String logoTeam1 = team1.getLogo();
            
            
            
            String nomTeam2 = arrayTeam.getJSONObject(1).getString("name");
            Team team2 = findTeambynomV2(nomTeam2);
            int idTeam2 = team2.getIdTeam();
            String logoTeam2 = team2.getLogo();
            
            String time = array.getJSONObject(i).getString("scheduled_at");
            
            String[] arrOfStr = array.getJSONObject(i).getString("scheduled_at").split("T");
            
            Date datematch = Date.valueOf(arrOfStr[0]);
            
              
              
              //si tous les equipes sont presentent dans notre BD
              if(idTeam1!=0 || idTeam2!=0){
                 
                  
                     if(datematch.compareTo(datenow)<=0){
                      //int idTeam1, int idTeam2, int idMatchRivalry, Date datematch, String nomTeam1, String nomTeam2, float odds1, float odds2, String logo, String time, String tournois
                      MatchAPI temp = new MatchAPI(idTeam1,idTeam2,idRivalry,datematch,nomTeam1,nomTeam2,odds1,odds2,logoTeam1,logoTeam2,time,tournois,nbrMap);
                      
                      val.add(temp);
                     }
                     else{
                         break;
                     }
                  
              }
             
          }
          return val;
          
        }
        
        
        
        final static Logger logger = LoggerFactory.getLogger(JavaApplication.class);
	public static void main(String[] args) throws SchedulerException {
		SpringApplication.run(JavaApplication.class, args);
                
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        scheduler.start();

        JobDetail jobDetail = newJob(HelloJob.class).build();

        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(repeatSecondlyForever(1800))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
              
	}
        
        public static class HelloJob implements Job {

            @Override
            public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                
                try {
                    insererTest();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(JavaApplication.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
}

