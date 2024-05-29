package com.uwi.idworks.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.uwi.idworks.IdworksApplication;
import com.uwi.idworks.entity.BannerStudentInfo;
import com.uwi.idworks.entity.IDWorksInfo;
import com.uwi.idworks.util.NewDateFormatter;

@Service
public class IDWorksDao {
  
	private Connection idconn;
	private ArrayList<IDWorksInfo> idworkslist = new ArrayList<IDWorksInfo>();
	Logger logger = LoggerFactory.getLogger(IDWorksDao.class);
	
	 public IDWorksDao() {
		  
	  }
	 
	 private void openConnection() {
		 
		 //jdbc/sql/idworks
		 try
	      {
//	          Class.forName("net.sourceforge.jtds.jdbc.Driver");
//	          conn = DriverManager.openConnection(
//	          "jdbc:jtds:sqlserver://ACCELUS:1433/UWI_IDWorks","sa","admin");
	        Context initContext = new InitialContext();
	  		Context envContext  = (Context)initContext.lookup("java:/comp/env");
	  		DataSource ds = (DataSource)envContext.lookup("jjdbc/sql/idworks");
	  		idconn = ds.getConnection();
	      }
	      catch (Exception e)
	      {
	          e.printStackTrace();
	      }
	
	 }
	 public void closeConnection() {
		 try {
			 idconn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 public ArrayList<IDWorksInfo> gatherIDWorksDataTst(){
		 return new ArrayList<IDWorksInfo>();
	 }
	 public ArrayList<IDWorksInfo> gatherIDWorksData(){
			
			int i = 0;
			String selectStatement =
		    	 "select distinct HolderID,lastName,FirstName,MI,UserType,Department from IDWorks_PrintData";
		    try {
		    	openConnection();
	          PreparedStatement prepStmt = idconn.prepareStatement(selectStatement,ResultSet.TYPE_SCROLL_SENSITIVE, 
	                  ResultSet.CONCUR_UPDATABLE);
	          
	          ResultSet rs = prepStmt.executeQuery();
	          
	          while (rs.next()){
	        	  i++;
	        	  IDWorksInfo s = new IDWorksInfo();
	        	  
	        	  s.setHolderid(rs.getString(1));
	        	  s.setLastname(rs.getString(2));
	        	  s.setInitial(rs.getString(4));
	        	  s.setFirstname(rs.getString(3));
	        	  s.setUserType(rs.getString(5));
	        	  s.setFaculty(rs.getString(6));
	        	  
	        	  idworkslist.add(s);
	          }
	          closeConnection();
	         
		   } catch (Exception e){
			   logger.info("Error accessing data from ID Works DB - {}",e.getMessage());
			   IdworksApplication.restart();
		   }
           return idworkslist;
	   }
	 public void insertToIDWorks(BannerStudentInfo t){
		 try {
		    	String insertStatement = "insert into IDWorks_PrintData values ( ? , ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		    	
		    	openConnection();
		    	
		        PreparedStatement prepStmt = idconn.prepareStatement(insertStatement);
		        NewDateFormatter f = new NewDateFormatter();
		        
		        prepStmt.setString(1, t.getId().trim());
		        prepStmt.setString(2, t.getLastname().toUpperCase().trim());
		        prepStmt.setString(3, t.getFirstname().trim());
		        prepStmt.setString(4, t.getInitial() != null ? getIntitals(t.getInitial()): null);
		        prepStmt.setString(5, "");
		        prepStmt.setString(6, "");
		        prepStmt.setString(7, t.getFaculty().trim());
		        prepStmt.setString(8, "");
		        prepStmt.setTimestamp(9, null);
		        if (t.getLevel().trim().equals("Graduate")){
		        	t.setLevel("POSTGRAD");
		        } else if (t.getLevel().equals("Undergraduate")){
		        	t.setLevel("UNDERGRAD");
		        }
		        prepStmt.setString(10, t.getLevel().trim());
		        
		        prepStmt.setInt(11, 0);
		        prepStmt.setString(12, t.getStudent().trim());
		        prepStmt.setString(13, null);
		        prepStmt.setTimestamp(14, null);
		        prepStmt.setTimestamp(15, Timestamp.valueOf(f.printDate()));
		        prepStmt.setString(16, "");
		         
		        prepStmt.executeUpdate();
		        prepStmt.close();
		        
		        logger.info("inserted - {} {} {}", t.getId(), t.getFirstname(), t.getLastname());
		    	closeConnection();
		    	
		    } catch (SQLException e){
		    	logger.info(e.getMessage());
		    	logger.info("Insert Failed - {} {} {}", t.getId(), t.getFirstname(), t.getLastname());
		    }
		   
	 }
	 private String getIntitals(String initial) {
		 String finalInitial = "";
		 if (initial.indexOf(" ") >= 0) {
			 finalInitial = initial.substring(0,1).toUpperCase()+initial.substring(initial.indexOf(" ")+ 2,initial.indexOf(" ")+ 3).toUpperCase();
		 } else {
			 finalInitial = initial.substring(0,1);
		 }
		 return finalInitial;
	 }
	 public void updateIDWorks(IDWorksInfo t) {
		 
		   String updateStatement =
	           "update IDWorks_PrintData set holderid = ?, lastname = ?, " +
	           " firstname = ?,  " + 
				"department = ?, usertype = ?,c_type = ?, mi = ? " +
				"where holderid = ?";
	       try {
	    	       openConnection();
	    	       
	    	       PreparedStatement prepStmt = idconn.prepareStatement(updateStatement);
			       if (t.getUserType() != null) {
				       prepStmt.setString(1, t.getHolderid());
				       prepStmt.setString(2, t.getLastname().toUpperCase().trim());
				       prepStmt.setString(3, t.getFirstname().trim());
				       prepStmt.setString(4, t.getFaculty().trim());
				       prepStmt.setString(5, t.getUserType());
				       prepStmt.setString(6, t.getStudent());
				       prepStmt.setString(7, t.getInitial() != null && t.getInitial().length() > 2 ? t.getInitial().substring(0,2): t.getInitial());
				       prepStmt.setString(8, t.getHolderid());        
				       prepStmt.executeUpdate();
			           prepStmt.close();
				       
			           logger.info("updated - {} {} {}", t.getHolderid(), t.getFirstname(), t.getLastname());
			           closeConnection();
			           
			        }
	       } catch (SQLException e){
	    	   logger.info("tried to update - {} {} {} {} {}", t.getHolderid(), t.getFirstname(), t.getLastname(),t.getFaculty(), t.getLevel());
	    	   logger.info("Error updating ID Works DB - {}",e.getMessage());
	    	  
	       }
	       

	   }
}
