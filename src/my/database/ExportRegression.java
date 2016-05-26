package my.database;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.io.PrintWriter;


//handle export of regression table to csv
public class ExportRegression{

  private String [] [] data;
  private String [] cols;
  private String path;
  private ArrayList<Integer> indices;



  //constructor
  public ExportRegression(String [] [] data, String [] cols,String path){

    this.data = data;
    this.cols = cols;
    this.path = path;
  }

  //write object to csv file
  public void write(){
    
    try{
      //get the indices
      this.indices = this.getIndices();

      //make a writer for output
      PrintWriter w = 
        new PrintWriter(new FileWriter(this.path));

      //write the file header
      w.print(this.getHeader());

      //write data while the last index isn't at the end of the set
      while(this.indices.get(this.indices.size()-1) < this.data.length)
        this.writeRow(w);

      //close printwriter 
      w.close();

    }catch(Exception e){e.printStackTrace();}

  }

  //returns the first index of each loc
  private ArrayList<Integer> getIndices(){

    //make al to hold indices
    ArrayList<Integer> indices = new ArrayList<Integer> ();
    indices.add(0);

    //get first loc
    String  locLastAdded = this.data[0][0];

    //loop through all the rows
    for (int i = 0; i < data.length; i++){

      //if it is a new location...
      if(!data[i][0].equals(locLastAdded)){

        //add its index
        indices.add(i);

        //update locLastAdded
        locLastAdded = this.data[i][0];
      }

    }//end for rows

    return indices;

  }//end getIndices

  //get file header
  private String getHeader(){

    //make header Strings
    String firstLine = "Regression";
    String secondLine = ",";
    String thirdLine = "";

    //construct lines based on agg level
    switch (this.data[0].length){

      //Year
      case 7:
        thirdLine = "Month/Day/Year,";
        firstLine += "By Day";
        break;
      //Month
      case 6:
        thirdLine = "Month/Year,";
        firstLine += "By Month";
        break;
      //Day
      case 5: 
        thirdLine = "Year,";
        firstLine += "By Year";
        break;
      //All
      default:
        secondLine = "";
        break;

    }//end switch

    //get second and third lines
    for(Integer index : this.indices){
      secondLine += data[index][0] + ",,,";
      thirdLine += "Slope,Intercept,R^2,";
    }

    //now put it all together!
    return (firstLine + "\n\n" + secondLine 
        + '\n' + thirdLine+ '\n');
 
  }//end getHeader


  //get date parts
  private String getDate(int index){
    String [] row = this.data[index];
    String date = "";
    if(row.length > 4)
      date += row[1] + ",";
    if(row.length > 5)
      date = row[2] + "/" + date;
    if(row.length > 6)
      date = row[3] + "/" + date;
    
    return date; 
  }

  //write this.data to file
  private void writeRow(PrintWriter w) throws Exception{

    //local vars
    String outLine = "";//stores current line of output
    String date = "";//stores current date
     
    //get the date
    outLine += this.getDate(indices.get(0));

    //loop through the locations
    for(int i = 0; i < this.indices.size(); i++){

      //get the calcs (last three cols)
      String [] row = this.data[this.indices.get(i)];
      outLine += 
        row[row.length-3] + "," + row[row.length-2] 
        + "," + row[row.length-1] + ",";

      //replace any NaNs
      outLine.replace("N/A", "");

      //increment the current indices
      this.indices.set(i, this.indices.get(i) +1);

    }

    //print to file
    w.println(outLine);
 
  } 


}//end class
