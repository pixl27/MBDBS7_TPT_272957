/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import oracle.jdbc.OracleConnection;

/**
 *
 * @author tolot
 */
public class NotifWeb {
    String idUser;
    String token;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public NotifWeb(String idUser, String token) {
        this.idUser = idUser;
        this.token = token;
    }
    
      public static void insererNotifWeb(OracleConnection connection,String idUser,String token) throws SQLException{
           
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
      
      public static int getDoublonNotifWeb(OracleConnection co,String idUser,String token) throws SQLException{
            int val = 0;
            Statement statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDNOTIF from NOTIFWEB where IDUSER='"+idUser+"' and  TOKEN='"+token+"' ");
           
            while (resultSet.next()){
                val = resultSet.getInt(1);
            }
            return val;
        }
      
       public static ArrayList<NotifWeb> getAllNotifWeb(String idUser) throws SQLException{
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
    
}
