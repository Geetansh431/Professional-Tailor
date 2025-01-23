package org.example.project.enrollment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MySqlConnectionKlass {

    public static Connection doConnect(){
        int l=10,b=10;
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
