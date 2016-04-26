package my.database;
import java.util.*;
import java.sql.*;



public class DBUtil{

  //static vars for class.
  //should be moved to .config later
  private static String USER = "tda_usr";
  private static String PASSWORD = "La$a1mtns!!";
  private static String DRIVER = "com.mysql.jdbc.Driver";
  private static String DATABASE = "tda";
  private static String URL = "jdbc:mysql://ec2-52-37-36-222.us-west-2.compute.amazonaws.com:3306/" + DATABASE;


  //member vars
  private String query;
  private Connection conn;


  //constructor
  DBUtil(String query){

    this.query = query;

  }

  DBUtil(){
     
  }
  
  private void connectToDB(){

    // Load the Connector/J driver
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();

      // Establish connection to MySQL
      conn = DriverManager.getConnection(URL, USER, PASSWORD);
      //System.out.println("DATABASE CONNECTED");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }//end connect

  private void disconnectFromDB(){
    try {
      conn.close();
      //System.out.println("DATABASE DISCONNECTED");
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  private ResultSet query(){

    try{

      //create ps
      PreparedStatement ps 
        = this.conn.prepareStatement(this.query);
        //System.out.println(this.query);
        ps.execute();
        return ps.getResultSet();

    }catch(Exception e){

      e.printStackTrace();
      return null;
    }

  }//end query

    private String yearConvert(Integer year){
	String yearStr = Integer.toString(year);
	if(yearStr.length() == 2){
		yearStr = "20" + yearStr;
	}
	else if(yearStr.length() == 1){
		yearStr = "200" + yearStr;
	}
	return (yearStr);
		
    }
    
  public String getQuery(){
      return this.query;
  }
    
  public void setQuery(String q){
      this.query = q;
  }
    
    
  public ArrayList<String> execute (){

    try{

      //make connection
      this.connectToDB();

      //query
      ResultSet rs = this.query();

      //make ArrayList
      ArrayList<String> results = new ArrayList<String> ();

      //check for null
      if(rs != null){

        //convert rs to al
        while(rs.next()){
          if(this.query.contains("DISTINCT(year)")){
              results.add(this.yearConvert(rs.getInt(1)));
          }else{
              results.add(rs.getString(1));
          }

        }

      }else{
        results.add(null);
      }

      //close
      this.disconnectFromDB();

      return results;

    }catch(Exception e){
      System.out.println("hi");
      e.printStackTrace();
      return null;
    }

  }//end ex 
 
          

}//end class
