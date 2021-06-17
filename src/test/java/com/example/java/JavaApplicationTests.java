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
            
            String nom = "PSG.LGD";
            
           OracleConnection connection = Connexion.getConnection();
           
            
            Statement statement = connection.createStatement();
           
            ResultSet resultSet = statement.executeQuery("select IDTEAM from Team where nom='"+nom+"'");
            int id = 0; 
            while (resultSet.next())
                id = resultSet.getInt(1);
            
            System.out.println("id:"   +id);
	}

}
