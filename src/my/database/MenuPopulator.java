
/* a class to allow population
 * of jcombo boxes from database.
 */



import java.util.ArrayList;
import java.sql.*;

public class MenuPopulator{

  //static vars
  private static String LOC_QUERY = "SELECT CONCAT_WS(' - ',site_code,"
    + "site_name) from site_location;";

  //member vars
  private String type;
  private String query;

  //constructor
  MenuPopulator(String type){

    this.type = type;

    if(this.type == "loc"){

      this.query = LOC_QUERY;

    }//endif  

  }//end constructor

  //execute query
  private ArrayList<String> execute(){

    //make a dbutil
    DBUtil dbu = new DBUtil(this.query);

    //execute it
    return dbu.execute(); 
  }

  //toArray
  public String[] toArray(){

    //execute the query
    ArrayList<String> results = this.execute();

    //make array
    String [] retval = new String [results.size()];
    results.toArray(retval);

    return retval;

  }//end toArray 

}

