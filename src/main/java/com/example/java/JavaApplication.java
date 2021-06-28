package com.example.java;

import classe.Connexion;
import classe.Match;
import classe.MatchAPI;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
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
           Statement statement = null;
           Team val = new Team();
           
            try{
            
                statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

                ResultSet resultSet = statement.executeQuery("select IDTEAM,LOGO from Team where nom like '%"+nom+"%'");

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
           
           ResultSet resultSet = statement.executeQuery("select "+nom+".currval from DUAL;");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
        
        void insererMatch(OracleConnection connection,int idTeam1, int idTeam2, Date datematch) throws SQLException{
           
            Statement statement = null;
            try{
                statement = connection.createStatement();
           
                statement.executeQuery("insert into Match values(MATCH_SEQ.NEXTVAL,"+idTeam1+","+idTeam2+",TO_DATE('"+datematch+"','YYYY-MM-DD'))");
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
        
        
        
        Match getMatch(int idMatchRivalry) throws SQLException{
            String url = "https://www.rivalry.com/api/v1/matches/"+idMatchRivalry;
            String response = restTemplate.getForObject(url, String.class);  
            
            
            JSONObject json = new JSONObject(response);
            JSONArray array = json.getJSONObject("data").getJSONArray("competitors");
            
            
            String nomTeam = array.getJSONObject(0).getString("name");
            int idTeam1 = findidteambynom(nomTeam);
            if(idTeam1 == 0){
                return null;
            }
            
            nomTeam = array.getJSONObject(1).getString("name");
            int idTeam2 = findidteambynom(nomTeam);
            if(idTeam2 == 0){
                return null;
            }
            
            String[] arrOfStr = json.getJSONObject("data").getString("scheduled_at").split("T");
            System.out.println("date"+arrOfStr[0]);
            Date datematch = Date.valueOf(arrOfStr[0]);
            
            
            Match val = new Match(idTeam1,idTeam2,datematch);
            return val;
        }
        
        @Bean
        public RestTemplate restTemplate() {
        return new RestTemplate();
        }
        
        
        
        @PostMapping(value = "/parier", consumes = "application/json", produces = "application/json")
        @ResponseBody
        String parier(@RequestParam int idUser,@RequestParam int idMatch,@RequestParam String type,@RequestParam String nomTeam,@RequestParam float montant,@RequestParam float odds) throws SQLException{
            Match m = getMatch(idMatch);
            
            OracleConnection oc = Connexion.getConnection();
            try{
                insererMatch(oc,m.getIdTeam1(),m.getIdTeam2(),m.getDatematch());
                int idMatchTemp = getSequence(oc,"MATCH_SEQ");
                int idTeam = findteambynom(oc,nomTeam);
                Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                insererParis(oc,idUser,idMatchTemp,idTeam,type,montant,odds,datenow,0);
            }
            finally{
                 if(oc!=null){
                    oc.close();
                }
            }
            
            return "Match" +idUser+","+idMatch;
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

