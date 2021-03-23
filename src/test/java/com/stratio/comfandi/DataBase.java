package com.stratio.comfandi;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

   public static void main(String[] args) {

       try (Connection con = DriverManager.getConnection(System.getenv().get("URL_POSTGRES") == null
				? "jdbc:postgresql://comfandi-poolpostgresmd5.comfandi.marathon.mesos:20120/comfandidc?prepareThreshold=0"
				: System.getenv().get("URL_POSTGRES"),
				System.getenv().get("USER_POSTGRES") == null ? "extpostgresuser" : System.getenv().get("USER_POSTGRES"),
				System.getenv().get("PASSW_POSTGRES") == null ? "QXpe64SdbEYeMQ5c"
						: System.getenv().get("PASSW_POSTGRES")
    		   );
               Statement st = con.createStatement();
               ResultSet rs = st.executeQuery("SELECT fecha_credito FROM public.comprasconcreditorotativo where fecha_credito < '80171021' limit 1 ")) {
           if (rs.next()) {
               System.out.println(rs.getString(1));
           }
       } catch (SQLException ex) {
          System.out.print(ex.getMessage()); 
       } finally {
       }
   }
}