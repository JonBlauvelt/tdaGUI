/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.database;

/**
 *
 * @author chao
 */
import java.util.*;
import java.sql.*;

public class StatisticalAnalysis {
	String viewName;
	String aggregateVal;
	Connection conn;
	PreparedStatement calcStatement;
	ResultSet calcRS;
	QueryRawData QRD = new QueryRawData();
	
	public StatisticalAnalysis(String vn, String av, Connection conn){
		this.viewName = vn;
		this.aggregateVal = av;
		this.conn = conn;
	}
	
	public String[] getTableCols(String av){
		ArrayList<String> tableList = new ArrayList<String>();
                tableList.add("Site Name");
                tableList.add("Temp");
		switch(av){
		case "Year": tableList.add("Year");
			break;
		case "Month": tableList.add("Month"); tableList.add("Year"); 
			break;  
		case "Day": tableList.add("Day"); tableList.add("Month"); tableList.add("Year"); 
			break;
		//case "hour": colVals = "hour, day, month, year";
		//	break;
		default:
		}
		String[] tableArr = tableList.toArray(new String[tableList.size()]);
		return tableArr;
	}
        
        public String[] getTableCols(String av, String calc){
		ArrayList<String> tableList = new ArrayList<String>();
                tableList.add("Site Name");
                if(calc.contains("hilo")){
                    tableList.add("Max Temp");
                    tableList.add("Min Temp");
                }else{
                    tableList.add("Temp");
                }
		switch(av){
		case "Year": tableList.add("Year");
			break;
		case "Month": tableList.add("Month"); tableList.add("Year"); 
			break;  
		case "Day": tableList.add("Day"); tableList.add("Month"); tableList.add("Year"); 
			break;
		//case "hour": colVals = "hour, day, month, year";
		//	break;
		default:
		}
		String[] tableArr = tableList.toArray(new String[tableList.size()]);
		return tableArr;
	}
	
        private String getGroupBy(String av){
            String colVals = "";
		switch(av){
			case "Year": colVals = "Year,";
				break;
			case "Month": colVals = "Month, Year,";
				break;  
			case "Day": colVals = "Day, Month, Year,";
				break;
			//case "hour": colVals = "hour, day, month, year";
			//	break;
			default:
		}
		return colVals;    
        }
	
	private String getColumns(String av){
		String colVals = "";
		switch(av){
			case "Year": colVals = ", year as Year";
				break;
			case "Month": colVals = ", month as Month, year as Year";
				break;  
			case "Day": colVals = ", day as Day, month as Month, year as Year";
				break;
			//case "hour": colVals = "hour, day, month, year";
			//	break;
			default:
		}
		return colVals;
	}
	
	private ArrayList<String> getColumnVals(String av){
		ArrayList<String> colList = new ArrayList<String>();
                colList.add("site_name");
                colList.add("temp");
		switch(av){
			case "Year": colList.add("year");
				break;
			case "Month": colList.add("month"); colList.add("year");
				break;
			case "Day": colList.add("day"); colList.add("month"); colList.add("year");
				break;
			default:
		}
		return colList;
	}
	
	public String getQuery(String calculation){
		String query = "";
		switch(calculation){
			case "average": query = "SELECT site_name, AVG(temp) as temp " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.getGroupBy(this.aggregateVal) + " site_name "
					+ "ORDER BY site_name, year, month, day";
				break;
			case "standard": query = "SELECT site_name, STD(temp) as temp " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.getGroupBy(this.aggregateVal) + " site_name "
					+ "ORDER BY site_name, year, month, day";
				break;
			case "high": query = "SELECT site_name, MAX(temp) as temp " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.getGroupBy(this.aggregateVal) + " site_name "
					+ "ORDER BY site_name, year, month, day";
				break;
			case "low": query = "SELECT site_name, MIN(temp) as temp " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.getGroupBy(this.aggregateVal) + " site_name "
					+ "ORDER BY site_name, year, month, day";
				break;
			default: System.out.println("NOT A VALID CALCULATION"); 
		}
		return query;
	}
	
	public ArrayList<HashMap<String, Object>> calculate(String calculation){
		String[] calcArr = this.getTableCols(this.aggregateVal);
		ArrayList<String> colList = this.getColumnVals(this.aggregateVal);
		ArrayList<HashMap<String, Object>> calcList = new ArrayList<HashMap<String,Object>>();
		String query = this.getQuery(calculation);
		//System.out.println(query);
		try {
			calcStatement = conn.prepareStatement(query);
			calcRS = calcStatement.executeQuery();
			while(calcRS.next()){
				HashMap<String, Object> calcMap = new HashMap<String, Object>();
				//calcMap.put("SiteName", calcRS.getString("site_name"));
				//calcMap.put("Temp", calcRS.getFloat("temp"));
				for(int i=0; i < colList.size(); i++){
					if(colList.get(i).contains("year")){
						calcMap.put(calcArr[i], QRD.yearConvert(calcRS.getInt(colList.get(i))));
					}else if(colList.get(i).contains("site_name")){
                                                calcMap.put(calcArr[i],calcRS.getString(colList.get(i)));
                                        }else if(colList.get(i).contains("temp")){
                                                calcMap.put(calcArr[i],calcRS.getFloat(colList.get(i)));
                                        }else{
						calcMap.put(calcArr[i],calcRS.getInt(colList.get(i)));
					}
				}
				//System.out.println(calcMap.toString());
				calcList.add(calcMap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return calcList;
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
