
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
  
  private static String YEAR_QUERY = "SELECT DISTINCT(year) from temp_readings"
    + " order by year";
  
  //member vars
  private String type;
  private String query;
  DBUtil dbu;

  //constructor
  public MenuPopulator(String type){

    this.type = type;

    if(this.type == "loc"){

      this.query = LOC_QUERY;

    }else if(this.type == "years"){
        
      this.query = YEAR_QUERY;
      
    }//endif  

  }//end constructor
  
  public MenuPopulator(){
      
  }
  
  public ArrayList<String> populateLoggerYear(){
      ArrayList<String> years = new MenuPopulator("years").populate();
      ArrayList<String> logYear = new ArrayList<String>();
      String logYearStr;
      for(int i =0; i < years.size(); i++){
          if((i+1 != years.size())){
              logYearStr = "July 1, " + years.get(i) + " to June 30, " + years.get(i+1);
              logYear.add(logYearStr);
          }
          logYearStr = "";
      }
     return logYear; 
  }
  
  //execute query
  public ArrayList<String> populate(){

    //make a dbutil
    dbu = new DBUtil(this.query);

    //execute it
    return dbu.execute(); 
  }

}

