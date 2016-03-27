
package my.database;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *Contains a program to take a csv filename as input
 *and convert it into the proper format for file insert
 *into temp database. 
 */

public class DataParser {

  private String inFilename;
  private String outFilename;
  private String locId;
  private String locCode;

  private static String LOC_QUERY = "SELECT site_id FROM "
    + "site_location WHERE site_code = '%s';";

  private static String DEV_QUERY = "SELECT device_id FROM " 
    + "device_info WHERE device_serial = '%s';";

  private static String LOC_INSERT = "INSERT INTO site_location "
    + "(site_name, site_code, site_lat, "
    + "site_long) VALUES ('%s','%s','%s','%s');";

  private static String DEV_INSERT = "INSERT INTO device_info "
    + "(device_serial) VALUES ('%s');"; 

  private static String LOAD_DATA = "LOAD DATA LOCAL INFILE '%s' INTO " 
    + "TABLE temp_readings FIELDS TERMINATED "
    + "BY ',' LINES TERMINATED BY '\n' "
    + "(month,day, year,hour,temp,site_id, "
    + "device_id);";

  //constructor for new loc 
  public DataParser(String filename, String code, String desc, 
      String lat, String lon){

    //add new loc to table
    new DBUtil(String.format(LOC_INSERT, desc, code, lat, 
          lon)).execute();

    //update loc
    locCode = code;

    init(filename);

  }

  //constructor for old loc
  public DataParser(String filename, String locInfo){

    this.locCode = locInfo.substring(0,3);   
    init(filename);

  }

  //performs standard initialization tasks
  private void init(String filename){

    // infile
    this.inFilename = filename;

    //make outfile name
    this.outFilename = 
      inFilename.substring(0,filename.lastIndexOf(".")) + "Out.csv";

    //set loc id
    ArrayList<String> locIdAL = 
      new DBUtil(String.format(LOC_QUERY, this.locCode)).execute();
    this.locId = locIdAL.get(0);
  }


  //parses file and writes to easily insertable csv
  public String[][] parse(){

    try{

      //make an arrayList
      ArrayList<String[]> table = new ArrayList<String[]> ();

      //make a new scanner object to read from file
      Scanner s = new Scanner(new FileReader(this.inFilename));

      //make a writer for output
      PrintWriter w = 
        new PrintWriter(new FileWriter(this.outFilename));

      //get first line
      String header = s.nextLine();

      //parse out logger serial
      String [] headerParts = header.split(" ");
      String serial = headerParts[7];

      //get deviceId
      ArrayList<String> devList = 
        new DBUtil(String.format(DEV_QUERY,serial)).execute();

      //make var to store device id;
      String devId;

      //existing device
      if(devList.size() != 0)
        devId = devList.get(0);

      //handle new device
      else{ 
        
        //add device
        new DBUtil(String.format(DEV_INSERT, serial)).execute();

        //get devId
        devId = 
          (new DBUtil(String.format(DEV_QUERY,serial)).execute()).get(0);
      }

      //process temps to the file's end
      while (s.hasNext()){

        //get the full line
        String line = s.nextLine();

        //fix terminating comma
        if(line.endsWith(","))
          line = line + "\\N";  

        //split on commas for processing
        String [] lineArray = line.split(",");
        String [] tableRow = line.split(",");

        //add to the return table
        table.add(tableRow);

        //check for a datetime in this row 
        if (!lineArray[0].equals(" ") 
            && !lineArray[0].equals("")){

          //format datetime
          lineArray[0] = lineArray[0].replace('/',',');
          lineArray[0] = lineArray[0].replace(' ',',');
          lineArray[0] = lineArray[0].replace(":00","");

          //form new line with datetime
          String newLine = lineArray[0] + "," + lineArray[1] + ","
            + this.locId + "," + devId;

          //write to the outfile
          w.println(newLine); 

            }//end if valid datetime

      }//end while


      //close the scanner and printwriter
      s.close();
      w.close();

    String [][] tableArry = new String [table.size()][];
    return table.toArray(tableArry);

    }catch(Exception e){
      e.printStackTrace(); 
      System.out.println("it's broken");
      return null;
    }

  }

  //load and delete the outfile
  public void addToDb (){

    //load data infile
    new DBUtil(String.format(LOAD_DATA,this.outFilename)).execute();

    //delete outfile
    this.cancel();
  }

  public void cancel(){
    try{
      //delete file 
      Files.deleteIfExists(Paths.get(this.outFilename)); 
    }catch(Exception e){
      e.printStackTrace();
    }
  }

}//end class
