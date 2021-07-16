package com.example.java;

import classe.Connexion;
import classe.MailAPI;
import classe.Match;

import classe.MatchAPI;
import classe.MessageAPI;
import classe.NotifWeb;
import classe.Paris;
import classe.ParisArg;
import classe.Probleme;
import classe.Team;
import com.google.gson.Gson;
import java.io.IOException;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

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
   
   @Bean
        public RestTemplate restTemplate() {
        return new RestTemplate();
        }

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
    
  JSONArray getJSONArrayAPI(String url) throws JSONException{
       HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            
          ResponseEntity response = restTemplate.exchange(url, HttpMethod.GET,entity ,String.class);  
          
          JSONArray jsonarray = new JSONArray(response.getBody().toString());
          return jsonarray;
    }
    
   String transaction(String idUser,String type,float montant,int idParis,String description) throws JSONException{
            String url = "https://backend-node-mbds272957.herokuapp.com/api/parier";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
            
            MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>(); 
            body.add("token","c2FtYmF0cmFpemF5bWlub2ZhdHN5bWFoaXRh");
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
        
          int getDoublonProbleme(int idParis) throws SQLException{
            int val = 0;
            OracleConnection co = Connexion.getConnection();
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
                if(co!=null)
                    co.close();
            }
            return val;
        }
        
        
        void insertProbleme(int idParis) throws SQLException{
            OracleConnection oc = Connexion.getConnection();
            if(getDoublonProbleme(idParis)!=1){
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
                if(oc!=null)
                    oc.close();
            }
            }
            else{
                System.out.println("deja dans la base");
            }
            
        }
        
        static void insererTest() throws SQLException{
             
            OracleConnection connection = Connexion.getConnection();
           
            PreparedStatement statement = null;
            try{
                String query = "insert into TEST values('Finalisation',?)";
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
        
        
        
        void insererParis(OracleConnection connection,String idUser,int idMatch,int idTeam,String type,float montant,float odds,Date dateparis,int statut) throws SQLException{
            
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
                //String idUser,String type,float montant,String idParis,String description
                String description = "Parie entre "+m.getNomTeam1()+" et "+m.getNomTeam2()+" sur "+p.getType(); 
                String val = transaction(p.getIdUser(),"credit",p.getMontant(),0,description);
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
        
        
        ArrayList<Team> listeTeam = new ArrayList();
        void setlisteTeam(ArrayList<Team> t){
            listeTeam = t;
        }
        
        @GetMapping(path="/getallteam", produces = "application/json")
        @ResponseBody
        ArrayList<Team> getAllTeam() throws SQLException{
            
            if(listeTeam.isEmpty()){
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
           
                setlisteTeam(listeTeam);
            
            return listeTeam;
            
            }
            else
                return listeTeam;
        }
        
        
        ArrayList<Paris> getAllParisNonFinie(OracleConnection co) throws SQLException{
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
        
        Paris getParisById(OracleConnection co,int idParis) throws SQLException{
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
      
       void traitementParis(Paris p,String type,String description) throws SQLException, JSONException{
             OracleConnection co = Connexion.getConnection();
             co.setAutoCommit(false);
             try{
                 float montant = p.getOdds()*p.getMontant();
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
        
         void finaliser() throws SQLException, JSONException, MessagingException, AddressException, IOException{
            
            OracleConnection co = Connexion.getConnection();
            Date datenow = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            EmailController ec = new EmailController();
            ArrayList<Match> matchProbleme = new ArrayList();
            
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
                                                    sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                }
                                                else{
                                                    //paris perdant 
                                                    String description = "Malheuresement, Votre équipe a perdu pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"credit",description);
                                                    sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
                                                }
                                    }
                                    else if(nbrWinTeam2==nbrWinNeeded){
                                            System.out.println(m.getNomTeam2() +"Winner");
                                            //traitement pour team 2 Winner
                                            if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                    //paris gagnant 
                                                    String description = "Felicitation, Votre équipe a gagner pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"debit",description);
                                                    sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                             }
                                             else{
                                                    //paris perdant 
                                                    String description = "Malheuresement, Votre équipe a perdu pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                    traitementParis(listeParis.get(i),"credit",description);
                                                    sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
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
                                                            String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                            sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"credit",description);
                                                            sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
                                                        }
                                                    }
                                                    else{
                                                        //Team2 do the first Blood 
                                                        System.out.println(m.getNomTeam2()+ " do the first blood");
                                                        if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                                //paris gagnant 
                                                                String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"debit",description);
                                                                sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                        }
                                                        else{
                                                                //paris perdant 
                                                                String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"credit",description);
                                                                sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
                                                        }
                                                    }
                                                }
                                                else{
                                                    if(m.getIdTeam2()==idTeamWhoDoFB){
                                                        //Team2 do the first Blood 
                                                        System.out.println(m.getNomTeam2()+ " do the first blood");
                                                        if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                                //paris gagnant 
                                                                String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"debit",description);
                                                                sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                        }
                                                        else{
                                                                //paris perdant 
                                                                String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"credit",description);
                                                                sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
                                                        }
                                                    }
                                                    else{
                                                        //Team1 do the first Blood 
                                                        System.out.println(m.getNomTeam1()+ " do the first blood");
                                                        if(m.getIdTeam1()==listeParis.get(i).getIdTeam()){
                                                            //paris gagnant 
                                                            String description = "Felicitation, Votre équipe a fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"debit",description);
                                                                sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            String description = "Malheuresement, Votre équipe n'a pas fait le first blood sur map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                                traitementParis(listeParis.get(i),"credit",description);
                                                                sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
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
                                                            sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                             sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
                                                        }
                                                    }
                                                    else{
                                                        System.out.println(m.getNomTeam2()+" Winner map "+mapParier);
                                                        //Traitement pour team 2 Winnner 
                                                        if(m.getIdTeam2()==listeParis.get(i).getIdTeam()){
                                                            //paris gagnant 
                                                            String description = "Felicitation, Votre équipe a gagné sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                             sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                        }
                                                        else{
                                                            //paris perdant 
                                                            String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                              sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
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
                                                            sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                            }
                                                            else{
                                                                //paris perdant 
                                                                 String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                             sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
                                                            }
                                                    }
                                                    else{
                                                        System.out.println(m.getNomTeam1()+" Winner map "+mapParier);
                                                        //Traitement pour team 1 Winnner 
                                                        if(m.getIdTeam1()==listeParis.get(i).getIdTeam()){
                                                                //paris gagnant 
                                                                 String description = "Felicitation, Votre équipe a gagné sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                            traitementParis(listeParis.get(i),"debit",description);
                                                            sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Felicitation",description);
                                                            }
                                                            else{
                                                                //paris perdant 
                                                                   String description = "Malheuresement, Votre équipe a perdu sur la map "+mapParier+" pendant le match entre "+m.getNomTeam1()+" et "+m.getNomTeam2();
                                                             traitementParis(listeParis.get(i),"credit",description);
                                                             sendNotificationWebToAllDeviceForUser(listeParis.get(i).getIdUser(),"Malheuresement",description);
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
                                Boolean isMatchInserena = true;
                                for(int j=0;j<matchProbleme.size();j++){
                                    if(matchProbleme.get(j).getIdMatch() == m.getIdMatch()){
                                        isMatchInserena = false;
                                        break;
                                    }
                                }
                                
                                if(isMatchInserena){
                                    matchProbleme.add(m);
                                    
                                    System.out.println("mila mihetsika fa tsy hita paris an'olona");
                                }
                                
                                insertProbleme(listeParis.get(i).getIdParis());

                            } 
                        }
                    }
                    System.out.println("#################################################################################");
                }
                if(!matchProbleme.isEmpty()){
                    System.out.println("preparation envoie mail");
                    ArrayList<MailAPI> listeMail = getAllEmailAdmin();
                       System.out.println("Nombre liste mail "+listeMail.size());
                     for(int l=0;l<listeMail.size();l++){
                                   ec.sendEmail(matchProbleme,listeMail.get(l).getEmail());
                     }
                }
            }
            finally{
                if(co!=null){
                    co.close();
                }
            } 
            
        }
            
         
         
         @GetMapping(path="/getAllProbleme", produces = "application/json")
        @ResponseBody
         ArrayList<Probleme> getAllProbleme() throws SQLException{
             ArrayList<Probleme> pb = new ArrayList();
             OracleConnection oc = Connexion.getConnection();
              Statement statement = null;
                 try{
                    statement = oc.createStatement();
           
                    ResultSet resultSet = statement.executeQuery("select IDPARIS from PROBLEME where STATUT = 0");

                    while (resultSet.next()){
                        Paris paristemp = getParisById(oc,resultSet.getInt(1));
                        Match matchtemp = getMatchById(oc,paristemp.getIdMatch());
                        String nomTeamNalainy = "";
                        if(paristemp.getIdTeam()==matchtemp.getIdTeam1()){
                            nomTeamNalainy = matchtemp.getNomTeam1();
                        }
                        else 
                            nomTeamNalainy = matchtemp.getNomTeam2();
                        //int idParis, String nomTeam1, String nomTeam2, Date datematch, String type_paris, String nomTeamNalainy
                        Probleme temp = new Probleme(paristemp.getIdParis(),matchtemp.getNomTeam1(),matchtemp.getNomTeam2(),matchtemp.getDatematch(),paristemp.getType(),nomTeamNalainy);
                        pb.add(temp);
                    }
                 }
                 finally{
                     if(statement!=null)
                         statement.close();
                     oc.close();
                 }
             return pb;
         }
         
         
         
         
         
         @GetMapping(path="/getAllEmailAdmin", produces = "application/json")
        @ResponseBody
         ArrayList<MailAPI> getAllEmailAdmin() throws SQLException{
                 ArrayList<MailAPI> listeMail = new ArrayList();
                 OracleConnection co = Connexion.getConnection();
                 Statement statement = null;
                 try{
                    statement = co.createStatement();
           
                    ResultSet resultSet = statement.executeQuery("select EMAIL from EMAILADMIN");

                    while (resultSet.next()){
                        MailAPI m = new MailAPI(resultSet.getString(1));
                        listeMail.add(m);
                    }
                 }
                 finally{
                     if(statement!=null)
                         statement.close();
                     co.close();
                 }
                 return listeMail;
             } 
         
         
           public static boolean isEmailAdress(String email){
                 String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                Pattern pattern = Pattern.compile(regex);
                java.util.regex.Matcher matcher = pattern.matcher(email);
                return matcher.matches();
            }
        
          int getDoublonEmailAdmin(String email) throws SQLException{
            int val = 0;
            OracleConnection co = Connexion.getConnection();
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select EMAIL from EMAILADMIN where EMAIL='"+email+"' ");
           
            while (resultSet.next()){
                val++;
            }
            return val;
        }
         
          
          @PostMapping(value = "/insererEmailAdmin", consumes = "application/json", produces = "application/json")
          @ResponseBody
          MessageAPI insererEmailAdmin(@RequestBody MailAPI email) throws SQLException{
              MessageAPI message = new MessageAPI();
                 if (isEmailAdress(email.getEmail()) && getDoublonEmailAdmin(email.getEmail())!=1) {
                     OracleConnection connection = Connexion.getConnection();
                     Statement statement = null;
                     try {
                         statement = connection.createStatement();

                         statement.executeUpdate("insert into EMAILADMIN values('"+email.getEmail()+"')" );
                         message.setMessage("insertion reussi");
                     } finally {
                         if (statement != null) {
                             statement.close();
                         }
                         connection.close();
                     }
                 } 
                 else{
                     System.out.println("is not email valide");
                     message.setMessage("is not email valide or already in base");
                 }
                 return message;
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
          
          
          
          Calendar c = Calendar.getInstance(); 
             c.add(Calendar.DATE, 14);
            Date datenow = new java.sql.Date(c.getTimeInMillis());
            
            int j = 10;
            int t = 0;
            int taille = array.length() -1;
            if(j<taille){
                for(int i=0;i<j;i++){
              
              int sizeMarket = array.getJSONObject(i).getJSONArray("markets").length()-1;
              
              if (sizeMarket<0){
                  int test = j +1;
                  if(test<taille){
                  j++;
                  }
                 continue;
              }
              
              
              int idRivalry = array.getJSONObject(i).getInt("id");
              String tournois = array.getJSONObject(i).getJSONObject("tournament").getString("name");
              
              JSONArray arrayTeam = array.getJSONObject(i).getJSONArray("competitors");
              
              
              String nameTest = array.getJSONObject(i).getJSONArray("markets").getJSONObject(sizeMarket).getString("name");
              
             
             
              
              
              JSONArray  arrayOdds = array.getJSONObject(i).getJSONArray("markets").getJSONObject(sizeMarket).getJSONArray("outcomes");
              
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
                
            }
            else{
                for(int i=0;i<taille;i++){
              
              int sizeMarket = array.getJSONObject(i).getJSONArray("markets").length()-1;
              
              if (sizeMarket>0){
                 continue;
              }
              
              int idRivalry = array.getJSONObject(i).getInt("id");
              String tournois = array.getJSONObject(i).getJSONObject("tournament").getString("name");
              
              JSONArray arrayTeam = array.getJSONObject(i).getJSONArray("competitors");
              
              
              String nameTest = array.getJSONObject(i).getJSONArray("markets").getJSONObject(sizeMarket).getString("name");
              
             
             
              
              
              JSONArray  arrayOdds = array.getJSONObject(i).getJSONArray("markets").getJSONObject(sizeMarket).getJSONArray("outcomes");
              
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
        
        void setRestTemplate(RestTemplate rest){
            restTemplate = rest;
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
        
        String Bonjour(){
            return "bonjour, finalise les paris des clients terminer";
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
           
           
            int sendNotificationToWeb(String token,String idUser,String title,String message) throws JSONException{
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
            int val = json.getInt("success");
            return val;
       }
            
      
      void sendNotificationWebToAllDeviceForUser(String idUser,String title,String message) throws SQLException, JSONException{
                 ArrayList<NotifWeb> listeToken = getAllNotifWeb(idUser);
                 System.out.println("listeToken :"+listeToken.size());
                 for(int i =0;i<listeToken.size();i++){
                     int val = sendNotificationToWeb(listeToken.get(i).getToken(),idUser,title,message);
                     if(val==1)
                         System.out.println("Notification envoyé");
                 }
             }
      
        @PostMapping(value = "/insererNotifWeb", consumes = "application/json", produces = "application/json")
        @ResponseBody
        String insererNotifWeb(@RequestBody NotifWeb n) throws SQLException{
            OracleConnection co = Connexion.getConnection();
            String val = "";
            try{
                int i = getDoublonNotifWeb(co,n.getIdUser(),n.getToken());
                if(i==0){
                    insererNotifWeb(co,n.getIdUser(),n.getToken());
                    val = "New device detected";
                }
                else
                    val = "device already in database";
            }
            finally{
                co.close();
            }
            return val;
        }
        
        static ArrayList<MatchAPI> listeMatch = new ArrayList();
        void setListeMatch(ArrayList<MatchAPI> listeMatch){
            this.listeMatch = listeMatch;
        }
        
        void cleanListeMatch(){
            listeMatch = new ArrayList();
        }
        
        @GetMapping(path="/getallmatchtest", produces = "application/json")
        @ResponseBody
        ArrayList<MatchAPI> getAllMatchTest() throws SQLException, JSONException{
            
           if(listeMatch.isEmpty())
               listeMatch = getAllMatch();
        
          return listeMatch;
        }
        
        
        
        final static Logger logger = LoggerFactory.getLogger(JavaApplication.class);
	public static void main(String[] args) throws SchedulerException,JobExecutionException{
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
            
            private RestTemplate restTemplate = new RestTemplate();

            @Override
            public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                
                try {
                    JavaApplication j = new JavaApplication();
                    j.setRestTemplate(restTemplate);
                    j.finaliser();
                    j.cleanListeMatch();
                    j.setListeMatch(j.getAllMatch());
                    j.setlisteTeam(j.getAllTeam());
                    System.out.println(j.Bonjour());
                    insererTest();
                } catch (SQLException | JSONException | MessagingException | IOException ex) {
                    java.util.logging.Logger.getLogger(JavaApplication.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            
        }
}

