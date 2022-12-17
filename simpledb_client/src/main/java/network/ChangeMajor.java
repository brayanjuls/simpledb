package network;
import java.sql.*;

import simpledb.jdbc.network.NetworkDataSource;
import simpledb.jdbc.network.NetworkDriver;

public class ChangeMajor {
   public static void main(String[] args) {
      Driver d = new NetworkDriver();
      NetworkDataSource dataSource = new NetworkDataSource();
      dataSource.setServer("localhost");

      String url = "jdbc:simpledb://localhost";

      try (Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement()) {
         String cmd = "update STUDENT set MajorId=30 "
               + "where SName = 'amy'";
         stmt.executeUpdate(cmd);
         System.out.println("Amy is now a drama major.");
      }
      catch(SQLException e) {
         e.printStackTrace();
      }
   }
}
