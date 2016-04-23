package my.database;
import java.util.GregorianCalendar;
import java.util.Date;



//converts date to hours since first temp reading
public class TempTime{

  private static long MILLIS_SINCE_EPOCH 
    = new GregorianCalendar(2002,8,9,0,0,0).getTimeInMillis();

  private static int MILLIS_PER_HOUR = 3600000;

  private long hours;


  public TempTime(int year, int month, int day, String time){

    GregorianCalendar cal = 
      new GregorianCalendar(month-1, day, 
          year, getIntHours(time), 0, 0);
    this.hours = (cal.getTimeInMillis() 
        - MILLIS_SINCE_EPOCH)/MILLIS_PER_HOUR;
  }
  
  //converts string hh:00 to int
  private static int getIntHours(String time){

    String hours = time.substring(0,time.indexOf(":")); 

    return new Integer(hours).intValue();
  
  
  }

  public long getHours(){return this.hours;}

}



