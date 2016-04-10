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

  public ExportCsv(String path, List<HashMap<String,Object>> data){

    this.path = path;
    this.data = data;

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
      w.println("Raw Data");

      //get the beginning index of the other locations
      while(count < this.data.size()){

        //Store location code
        String currentLoc = (String)data.get(count).get("SiteName");

        //add the index
        indices.add(count); 

        //skip all the other readings from this location
        while(count < this.data.size() && 
            currentLoc.equals(data.get(count).get("SiteName"))){

          //reassemble the datetime
          data.get(count).put("Datetime", data.get(count).get("Month") 
              + "/" + data.get(count).get("Day") + "/" 
              + data.get(count).get("Year") + " " 
              + data.get(count).get("Hour"));

          //increment
          count++;

        }
      }

      //get column names.
      String cols = "Datetime";
      for(int index : indices)
        cols += "," + data.get(index).get("SiteName");

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
