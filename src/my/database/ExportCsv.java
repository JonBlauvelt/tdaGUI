package my.database;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.io.PrintWriter;

public class ExportCsv{

  private String path;
  private List<HashMap<String,Object>> data;
  private String header;
  private String agg;
  private String datetimeHeader;
  private String siteKey;
  
  //constructor
  public ExportCsv(String path, List<HashMap<String,Object>> data, String type){

    this.path = path;
    this.data = data;
    this.header = type;
    this.agg = getAgg();
    
    //update the site key and header
    if(this.header.equals("Raw Data")){
      this.siteKey = "SiteName";
      this.header += '\n';
    }else{
      this.siteKey = "Site Name";
      this.header += " By " + this.agg + '\n'; 
    }

  }

  //determine the aggregation level
  private String getAgg(){

    String agg = "";
    if(this.data.get(0).containsKey("Hour")){
      agg = "Hour";
      this.datetimeHeader = "Datetime";
    }else if(this.data.get(0).containsKey("Day")){
      agg = "Day";
      this.datetimeHeader = "Date";
    }else if(this.data.get(0).containsKey("Month")){
      agg = "Month";
      this.datetimeHeader = "Month/Year";
    }else{
      agg = "Year";
      this.datetimeHeader = "Year";
    }

    return agg;
      
  } 

  public void write(){

    try{
      //make a writer for output
      PrintWriter w = 
        new PrintWriter(new FileWriter(this.path));

      //make an al to store the beginning index for each loc
      ArrayList<Integer> indices = new ArrayList<Integer>();

      //loop counter
      int count = 0;

      //write the header
      w.println(this.header);

      //get the beginning index of the other locations
      while(count < this.data.size()){

        //Store location code
        String currentLoc = (String)data.get(count).get(siteKey);

        //add the index
        indices.add(count); 

        //skip all the other readings from this location
        while(count < this.data.size() && 
            currentLoc.equals(data.get(count).get(this.siteKey))){

          //reassemble the datetime
          String datetime = data.get(count).get("Year").toString(); 
          if(this.agg.equals("Day") || this.agg.equals("Hour"))
            datetime = data.get(count).get("Day").toString() + "/" + datetime;
          if(!this.agg.equals("Year"))
            datetime = data.get(count).get("Month").toString() + "/" + datetime;
          if(this.agg.equals("Hour"))
            datetime += data.get(count).get("Hour").toString();

          data.get(count).put("Datetime",datetime);

          //increment
          count++;
        }
      }

      //get column names.
      String cols = datetimeHeader;
      for(int index : indices)
        cols += "," + data.get(index).get(this.siteKey);

      //write column headers to file 
      w.println(cols);
      
      //keep going until the current index for the last loc
      //is the last index in the data array
      while(indices.get(indices.size() - 1) < data.size()){ 

        
        //add datetime
        String outLine = (String) data.get(indices.get(0)).get("Datetime");

        //loop through locations
        for (int i = 0; i<indices.size(); i++){
          
          //get temp
          String temp = data.get(indices.get(i)).get("Temp").toString();

          //increment index
          indices.set(i,indices.get(i) + 1);

          //check for null
          if(temp == null)
            temp = "";

          //add to output line
          outLine = outLine + "," +  temp;

        }

        //write to file
        w.println(outLine);
      }
      w.close();
    }catch(Exception e){e.printStackTrace();}
  }
}//end class
