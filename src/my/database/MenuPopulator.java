
/* a class to allow population
 * of jcombo boxes from database.
 */



package my.database;
import java.util.ArrayList;
import java.sql.*;

public class MenuPopulator{

  //static vars
  private static String LOC_QUERY = "SELECT CONCAT_WS(' - ',site_code,"
    + "site_name) from site_location ORDER BY site_code;";

  //member vars
  private String type;
  private String query;

  //constructor
  public MenuPopulator(String type){

    this.type = type;

    if(this.type == "loc"){

      this.query = LOC_QUERY;

    }//endif  

  }//end constructor

  //execute query
  public ArrayList<String> populate(){

    //make a dbutil
    DBUtil dbu = new DBUtil(this.query);

    //execute it
    return dbu.execute(); 
  }

}

