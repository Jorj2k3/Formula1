package com.example.demo4;

import java.sql.Connection;
import java.sql.DriverManager;

public class dataBase {

    public static Connection connectDB(String dbname, String user, String pass){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname, user, pass);
            if(conn != null){
                System.out.println("Connection established");
            }
            else{
                System.out.println("Connection Failed");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return conn;
    }
}
