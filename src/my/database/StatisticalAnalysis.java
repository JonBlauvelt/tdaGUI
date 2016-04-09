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
	
	StatisticalAnalysis(String vn, String av, Connection conn){
		this.viewName = vn;
		this.aggregateVal = av;
		this.conn = conn;
	}
	
	public String[] getTableCols(String av){
		ArrayList<String> tableList = new ArrayList<String>();
		switch(av){
		case "year": tableList.add("Year");
			break;
		case "month": tableList.add("Month"); tableList.add("Year"); 
			break;  
		case "day": tableList.add("Day"); tableList.add("Month"); tableList.add("Year"); 
			break;
		//case "hour": colVals = "hour, day, month, year";
		//	break;
		default:
		}
		String[] tableArr = tableList.toArray(new String[tableList.size()]);
		return tableArr;
	}
	
	
	private String getColumns(String av){
		String colVals = "";
		switch(av){
			case "year": colVals = "year";
				break;
			case "month": colVals = "month, year";
				break;  
			case "day": colVals = "day, month, year";
				break;
			//case "hour": colVals = "hour, day, month, year";
			//	break;
			default:
		}
		return colVals;
	}
	
	private ArrayList<String> getColumnVals(String av){
		ArrayList<String> colList = new ArrayList<String>();
		switch(av){
			case "year": colList.add("year");
				break;
			case "month": colList.add("month"); colList.add("year");
				break;
			case "day": colList.add("day"); colList.add("month"); colList.add("year");
				break;
			default:
		}
		return colList;
	}
	
	public String getQuery(String calculation){
		String query = "";
		switch(calculation){
			case "average": query = "SELECT site_name, AVG(temp) as temp, " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.aggregateVal + ", site_name "
					+ "ORDER BY site_name, year, month, day";
				break;
			case "standard": query = "SELECT site_name, STD(temp) as temp, " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.aggregateVal + ", site_name "
					+ "ORDER BY site_name, year, month, day";
				break;
			case "high": query = "SELECT site_name, MAX(temp) as temp, " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.aggregateVal + ", site_name "
					+ "ORDER BY site_name, year, month, day";
				break;
			case "low": query = "SELECT site_name, MIN(temp) as temp, " + this.getColumns(this.aggregateVal)+ " from " + this.viewName + " GROUP BY " + this.aggregateVal + ", site_name "
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
		System.out.println(query);
		try {
			calcStatement = conn.prepareStatement(query);
			calcRS = calcStatement.executeQuery();
			while(calcRS.next()){
				HashMap<String, Object> calcMap = new HashMap<String, Object>();
				calcMap.put("SiteName", calcRS.getString("site_name"));
				calcMap.put("Temp", calcRS.getFloat("temp"));
				for(int i=0; i < colList.size(); i++){
					if(colList.get(i).contains("year")){
						calcMap.put(calcArr[i], QRD.yearConvert(calcRS.getInt(colList.get(i))));
					}else{
						calcMap.put(calcArr[i],calcRS.getInt(colList.get(i)));
					}
				}
				System.out.println(calcMap.toString());
				calcList.add(calcMap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return calcList;
	}
}
	
	
	
	
	
	
	
	
	
	
	
	