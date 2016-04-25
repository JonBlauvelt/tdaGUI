package my.database;
import java.util.GregorianCalendar;
import java.util.Date;



//converts date to hours since first temp reading
public class TempTime{

  private static long MILLIS_SINCE_EPOCH 
    = new GregorianCalendar(2002,8,9,0,0,0).getTimeInMillis();

  private static int MILLIS_PER_HOUR = 3600000;

  private long hours;


  public TempTime(int year, int month, int day, int hour){

    GregorianCalendar cal = 
      new GregorianCalendar(month-1, day, 
          year, hour, 0, 0);
    this.hours = (cal.getTimeInMillis() 
        - MILLIS_SINCE_EPOCH)/MILLIS_PER_HOUR;
  }

  public TempTime(String year, String month, String day, String hour){

    this(new Integer(year).intValue(),new Integer(month).intValue(),new Integer(day).intValue(),new Integer(hour).intValue());
  }

//public TempTime(String year, String month, String day){
//  this(year,month,day, "0");
//}
//
//public TempTime(String year, String month){
//  this(year,month,"0", "0");
//}
//
//public TempTime(String year){
//  this(year,"0","0", "0");
//}
//
    //hours accessor
  public long getHours(){return this.hours;}

}
