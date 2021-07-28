/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import oracle.jdbc.OracleConnection;

/**
 *
 * @author tolot
 */
public class MailAPI {
    String email;
    
    
    public MailAPI(){
        
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MailAPI(String email) {
        this.email = email;
    }
    
      public static boolean isEmailAdress(String email){
                 String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
                Pattern pattern = Pattern.compile(regex);
                java.util.regex.Matcher matcher = pattern.matcher(email);
                return matcher.matches();
            }
      
      public static int getDoublonEmailAdmin(String email) throws SQLException{
            int val = 0;
            OracleConnection co = Connexion.getConnection();
            Statement statement = null;
            try{
            statement = co.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select EMAIL from EMAILADMIN where EMAIL='"+email+"' ");
           
            while (resultSet.next()){
                val++;
            }
            }
            finally{
                if(statement!=null)
                    statement.close();
                co.close();
            }
            return val;
        }
    
    
}
