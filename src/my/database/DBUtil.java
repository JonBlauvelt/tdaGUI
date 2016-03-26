import java.util.*;
import java.sql.*;



public class DBUtil{

  //static vars for class.
  //should be moved to .config later
  private static String USER = "tda_usr";
  private static String PASSWORD = "La$a1mtns!!";
  private static String DRIVER = "com.mysql.jdbc.Driver";
  private static String DATABASE = "tda";
  private static String URL = "jdbc:mysql://52.36.196.165:3306/" + DATABASE;


  //member vars
  private String query;
  private Connection conn;


  //constructor
  DBUtil(String query){

    this.query = query;

  }


  private void connectToDB(){

    // Load the Connector/J driver
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();

      // Establish connection to MySQL
      conn = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("DATABASE CONNECTED");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }//end connect

  private void disconnectFromDB(){
    try {
      conn.close();
      System.out.println("DATABASE DISCONNECTED");
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  private ResultSet query(){

    try{

      //create ps
      PreparedStatement ps 
        = this.conn.prepareStatement(this.query);

      //return result set
      return ps.executeQuery();

    }catch(Exception e){

      e.printStackTrace();
      return null;
    }

  }//end query

  public ArrayList<String> execute (){

    try{

      //make connection
      this.connectToDB();

      //query
      ResultSet rs = this.query();

      //make ArrayList
      ArrayList<String> results = new ArrayList<String> ();

      //convert rs to al
      while(rs.next()){

        results.add(rs.getString(1));

      }

      //close
      this.disconnectFromDB();

      return results;

    }catch(Exception e){
      e.printStackTrace();
      return null;
    }

  }//end ex 

}//end class
