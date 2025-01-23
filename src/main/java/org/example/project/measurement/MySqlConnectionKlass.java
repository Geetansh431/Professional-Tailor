package org.example.project.measurement;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlConnectionKlass {

    public static Connection doConnect(){
        Connection con=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/project24", "root", "LostClown#5384");
        }catch(Exception exp){
            exp.printStackTrace();
        }
        return con;
    }

    public static void main(String[] args) {
        if(doConnect()==null){
            System.out.println("Sorry");
        }else{
            System.out.println("Good");
        }
    }
}
