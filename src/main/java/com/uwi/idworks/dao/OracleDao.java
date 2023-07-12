package com.uwi.idworks.dao;


import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.uwi.idworks.config.OracleConfig;
import com.uwi.idworks.entity.BannerStudentInfo;
import com.uwi.idworks.query.Queries;
import com.uwi.idworks.util.NewDateFormatter;

@Service
public class OracleDao extends Queries {

	Logger logger = LoggerFactory.getLogger(OracleDao.class);
	
    @SuppressWarnings("unused")
    
    @Autowired
    private final OracleConfig service;
    
    private Connection conn;
	
    private String strDate;
    
    private ComboPooledDataSource dataSource;
 
	public OracleDao(OracleConfig service) {
		this.service = service;
		dataSource = getPooledDataSource();
	}
	
	private ComboPooledDataSource getPooledDataSource() {
		
		    ComboPooledDataSource cpds = new ComboPooledDataSource();
		    cpds.setJdbcUrl(service.getConnect());
			cpds.setUser(service.getUsername());
			cpds.setPassword(service.getPassword());
			
	        // Optional Settings
	        cpds.setInitialPoolSize(5);
	        cpds.setMinPoolSize(5);
	        cpds.setAcquireIncrement(5);
	        cpds.setMaxPoolSize(20);
	        cpds.setMaxStatements(100);
	        
	        return cpds;
	}
    public Long findPidm(String studentId) {
		
		Long pidm = 0L;

		String sqlstmt = "select spriden_pidm, spriden_id from spriden where spriden_id = ?";

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			prepStmt.setString(1, studentId);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				pidm = rs.getLong(1);
			}

			rs.close();
			prepStmt.close();

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return pidm;
	}
   
	public Connection getConnection() {
    	return conn;
    }
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}	

	public String[] termRange(String term) {

		String[] range = new String[2];
		range[0] = new String();
		range[1] = new String();

		String year = term.substring(0, 4);

		if (term.substring(4, 6).equals("10")) {
			range[0] = year + "20";
			range[1] = year + "30";
		} else if (term.substring(4, 6).equals("20")) {
			range[0] = year + "10";
			range[1] = year + "30";
		} else if (term.substring(4, 6).equals("30")) {
			range[0] = year + "10";
			range[1] = year + "20";
		}

		if (term.substring(4, 6).equals("30")) {
			int yr = Integer.parseInt(year) + 1;
			range[0] = term;
			range[1] = Integer.toString(yr) + "10";
			
		}
		return range;
	}
	public int getPidm(String id) {

		int pidm = 0;

		String sqlstmt = "select spriden_pidm, spriden_id from spriden where spriden_id = ?";

		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
			prepStmt.setString(1, id);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				pidm = rs.getInt(1);
			}

			rs.close();
			prepStmt.close();

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return pidm;

	}
	private String getFaculty(String fcode){
	    
    	String faculty = null;
    	String sqlstmt = "SELECT stvcoll_desc FROM stvcoll WHERE stvcoll_code = ?"; 
    	
    	try {
    		Connection conn = dataSource.getConnection();
    		PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
    		prepStmt.setString(1, fcode);
    		ResultSet rs = prepStmt.executeQuery();

			int i = 0;

			if (rs.next()) {
			   faculty = rs.getString(1);
			}
    		rs.close();
    		prepStmt.close();
			
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return faculty;
    }

    public ArrayList<BannerStudentInfo>  collectInfo(String term)
			throws SQLException, RemoteException {
		// TODO Auto-generated method stub

		ArrayList<BannerStudentInfo> studentStatusMap = new ArrayList<BannerStudentInfo>();
		String selectStatement =this.getStudentQuery();
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);
			prepStmt.setString(1, "AS");
			prepStmt.setString(2, "EX");
			prepStmt.setString(3, "CE");
			prepStmt.setString(4, "CG");
			prepStmt.setString(5, "N");
			prepStmt.setString(6, "T");
			prepStmt.setString(7, "R");
			prepStmt.setString(8, "F");
			prepStmt.setString(9, "S");
			prepStmt.setString(10, "C");
			prepStmt.setString(11, "E");
			prepStmt.setString(12, "V");
			prepStmt.setString(13, "O");
			prepStmt.setString(14, "2000%");
            if (term == null)
			  term = this.getCurrentTerm();
			prepStmt.setString(15, term);
			String[] range = termRange(term);
			prepStmt.setString(16, range[0]);
			prepStmt.setString(17, range[1]);
			
			ResultSet rs = prepStmt.executeQuery();

			int i = 0;
		  
			while (rs.next()) {
				i++;
				BannerStudentInfo stu = new BannerStudentInfo();

				stu.setId(rs.getString(1));
				stu.setTerm(rs.getString(2));
				stu.setPidm(getPidm(rs.getString(1)));
				stu.setLastname(rs.getString(3));
				stu.setFirstname(rs.getString(4));
				stu.setInitial(rs.getString(5));
				if (rs.getString(6) != null && rs.getString(6).equals("UG")) {
					stu.setLevel("Undergraduate");
				} else if (rs.getString(6) != null
						&& rs.getString(6).equals("GR")) {
					stu.setLevel("Graduate");
				} else {
					stu.setLevel(rs.getString(6));
				}

				String faculty = "";
				if (rs.getString(7).equals("GS")){
					faculty = "Social Sciences";
				}
				else if (rs.getString(7).equals("HE")){
					faculty = "Humanities and Education";
				} else if (rs.getString(7).equals("PA")){
					faculty = "Pure and Applied Sciences";
				} else if (rs.getString(7).equals("SP")){
					faculty = "Social Sciences";
				} else {
					faculty = getFaculty(rs.getString(7));
					faculty = faculty.replace(",", " ");
				}
				
				stu.setCampusCode(rs.getString(8));
				stu.setStatus(rs.getString(9));
				stu.setStyp(rs.getString(10));
				stu.setActivityDate(rs.getDate(11).toString());
				stu.setBirthDate(rs.getDate(12));
				String major = rs.getString(13);
			
				if (major.equals("Banking & Finance"))
					major = "Banking and Finance";
				if (major.equals("Sport Sciences (Sport)") || major.equals("Sport Sciences")){
					major = "Management";
					
				} 
				
				stu.setFaculty(faculty);
				stu.setMajor(major);
				
				stu.setCountry(rs.getString(15));
				
				studentStatusMap.add(stu);
				
			}
			
			prepStmt.close();
			rs.close();

		} catch (Exception e) {
			
			e.printStackTrace();
		}
        return studentStatusMap;
	}
    public String getCurrentTerm() {
		String term = null;
		NewDateFormatter df = new NewDateFormatter();
	    
		String sqlstmt = "select min(stvterm_code) as maxtermcode from stvterm where stvterm_start_date <= ? and stvterm_end_date >= ? and stvterm_code not in ('201905') and stvterm_desc not like  '%Year%Long%'";
		
		try {
			Connection conn = dataSource.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(sqlstmt);
            prepStmt.setDate(1, java.sql.Date.valueOf((df.getSimpleDate())));
			prepStmt.setDate(2, java.sql.Date.valueOf(df.getSimpleDate()));

			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				term = rs.getString(1);
			}

			if ((term != null) && (term.indexOf("40") >= 0)) {

				term = null;

			}
			if (term == null) {

				sqlstmt = "select min(stvterm_code) as maxtermcode from stvterm where stvterm_start_date >= ? and stvterm_code <> '201905'";
				
				PreparedStatement prepStmt1 = conn.prepareStatement(sqlstmt);
				prepStmt1.setDate(1,
						java.sql.Date.valueOf((df.getSimpleDate())));

				ResultSet rs1 = prepStmt1.executeQuery();
				while (rs1.next()) {
					term = rs1.getString(1);
				}

				rs1.close();
				prepStmt1.close();
			}

			/* Only use for XRUN */
			rs.close();

			prepStmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return term;
	}
	
}
