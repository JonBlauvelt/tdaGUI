package my.database;
import java.util.GregorianCalendar;
import java.util.Date;



//converts date to hours since first temp reading
public class TempTime{

  private static long MILLIS_SINCE_EPOCH 
    = new GregorianCalendar(2002,8,9,0,0,0).getTimeInMillis();

  private static int MILLIS_PER_HOUR = 3600000;

  private long hours;


  public TempTime(int year, int month, int day, int hours){

    GregorianCalendar cal = 
      new GregorianCalendar(month-1, day, 
          year, hours, 0, 0);
    this.hours = (cal.getTimeInMillis() 
        - MILLIS_SINCE_EPOCH)/MILLIS_PER_HOUR;
  }
  
    //hours accessor
  public long getHours(){return this.hours;}

}
