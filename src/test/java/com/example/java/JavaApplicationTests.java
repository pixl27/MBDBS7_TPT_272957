package com.example.java;

import classe.Connexion;
import classe.Team;
import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import oracle.jdbc.OracleConnection;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaApplicationTests {

	@Test
	void contextLoads() throws SQLException {
            
            OracleConnection connection = Connexion.getConnection();
           
            
            Statement statement = connection.createStatement();
           
           ResultSet resultSet = statement.executeQuery("select * from Team order by IDTEAM FETCH FIRST 10 ROWS ONLY");
           ArrayList<Team> listeTeam = new ArrayList(); 
            while (resultSet.next()){
                Team temp = new Team(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
                listeTeam.add(temp);
            }
                
            String json = new Gson().toJson(listeTeam);
            
            System.out.println(json);
	}

}
