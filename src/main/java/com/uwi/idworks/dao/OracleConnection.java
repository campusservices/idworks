package com.uwi.idworks.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uwi.idworks.config.OracleConfig;

@Service
public class OracleConnection {
	
	Logger logger = LoggerFactory.getLogger(OracleDao.class);
	
	@Autowired
    private final OracleConfig service;
	
	private Connection conn;
	
	public OracleConnection(OracleConfig service) {
		this.service = service;
	}
	
	public void connectDataSource() {
		
		try {
			System.setProperty("oracle.jdbc.javaNetNio", "false");
			logger.info("Starting Oracle Db");
			Class.forName(service.getDriver());
			Properties prop = new Properties();
			prop.put("oracle.jdbc.javaNetNio", false);
			
			conn = DriverManager.getConnection(service.getConnect(), 
															service.getUsername(), 
															service.getPassword());
			
			conn.setAutoCommit(true);
			logger.info("Oracle Db Started");
		} catch (ClassNotFoundException e) {
			logger.info("Driver class not found - {}", e.getMessage());
		} catch (SQLException ex) {
			logger.info("Connection error - {}", ex.getMessage());
		}
        
	}
	public Connection getConnection() {
		return conn;
	}
}
