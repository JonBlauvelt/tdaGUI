package my.database;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Double;


public class RegressionSet{

  //instance vars
  private List<HashMap<String,Object>> data;
  private String agg_lev;
  private ArrayList<String[]> calcTable; 

  //constructor
  public RegressionSet(List<HashMap<String,Object>> data, 
      String agg_lev){

    this.data = data;
    this.agg_lev = agg_lev;
    this.calcTable = new ArrayList<String[]> (); 
    this.calculate();
  }

  //calculate regression for data sets
  private void calculate(){

    //initialize row index
    int index = 0;
    
    //initialize 
    HashMap<String,Object> firstRowInSet;
    HashMap<String,Object> currRow;

    //loop until we are out of data
    while(index < data.size()){

      //make regression object
      SimpleRegression singleReg = new SimpleRegression();

      //remember the first row and get curr row
      firstRowInSet = this.data.get(index);
      currRow = this.data.get(index);
        
      //loop until end of set
      while(index < data.size() 
          && this.sameSet(firstRowInSet, currRow)){

        currRow = this.data.get(index);

        //get time
        double hours = 
          (double) new TempTime(
              (int)currRow.get("Year"),
              (int)currRow.get("Month"),
              (int)currRow.get("Day"), 
              (String)currRow.get("Hour")).getHours();

        //add row to regression object
        singleReg.addData(hours, 
            new Double((float)currRow.get("Temp")).doubleValue());

        //increment
        index++;

      }

      //add regression data as row of return al
      ArrayList<String> calcRow = new ArrayList<String>();
      calcRow.add((String)firstRowInSet.get("SiteName"));
      for(String datePart : this.getDateParts())
        calcRow.add(firstRowInSet.get(datePart).toString());
      calcRow.add(formatCalc(singleReg.getSlope()));
      calcRow.add(formatCalc(singleReg.getIntercept()));
      calcRow.add(formatCalc(singleReg.getRSquare()));
      calcTable.add(calcRow.toArray(new String [calcRow.size()]));


    }//end while

  }//end calc

  //convert double into string with max 6 digits after decimal
  private static String formatCalc(double calc){

    //Convert to String
    String strCalc = new Double(calc).toString();
    
    //If there are more than 6 digits after decimal...
    if(strCalc.substring(strCalc.indexOf(".")+1).length() > 6){

      //shorten it
      strCalc = 
        strCalc.substring(0,strCalc.indexOf(".") + 7);
    }

    return strCalc;
  
  }


  //are these datetimes in the same set?
  private boolean sameSet(HashMap<String,Object> row, 
      HashMap<String,Object> otherRow) {

    boolean same = true;

    if(this.agg_lev == "Year")
      same = (same && (row.get("Year") == otherRow.get("Year")));
    if(this.agg_lev == "Month")
      same = (same && (row.get("Month") == otherRow.get("Month")));
    if(this.agg_lev == "Day")
      same = (same && (row.get("Day") == otherRow.get("Day")));

    return same;
  }

  //returns an AL of the applicable date part
  private ArrayList<String> getDateParts(){
    
    ArrayList<String> dateParts = new ArrayList<String> (); 

    //add date cols (if applicable)
    if(this.agg_lev != "Default (ALL)")
      dateParts.add("Year");
    if(this.agg_lev == "Month" || this.agg_lev == "Day")
      dateParts.add("Month");
    if(this.agg_lev == "Day")
      dateParts.add("Day");

    return dateParts;
  
  } 

  //returns a 2d array to populate jtable using default model
  public String[][] getModel(){

    return this.calcTable.toArray(new String [calcTable.size()][]); 

  }

  //returns array of column names (to pass to jTable.setModel)
  public String [] getCols(){
    
    ArrayList<String> cols = new ArrayList<String> ();

    //add loc col
    cols.add("Site Name");

    //add any applicable date cols 
    for (String datePart : this.getDateParts())
      cols.add(datePart);

    //add regression calc cols
    cols.add("Slope");
    cols.add("Intercept");
    cols.add("R^2");

    return cols.toArray(new String [cols.size()]);
  }



}//end class




