package com.uwi.idworks.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.uwi.idworks.config.MySQLConfig;

import lombok.Getter;
import lombok.Setter;

@Configuration
@Service
@Getter @Setter
@ConfigurationProperties(prefix="override")
public class OverrideDao {
   
	
   
	Logger logger = LoggerFactory.getLogger(OverrideDao.class);
	
	protected Connection conn;
	
	@Autowired
    private final MySQLConfig config;
	
	public OverrideDao(MySQLConfig config) {
		
		this.config = config;
		
		try {
			Class.forName(config.getDriver());

			conn = DriverManager.getConnection(
					config.getDatabase(), config.getUser(),
					config.getPassword());
			conn.setAutoCommit(true);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	public String overrideSemester() {

		String term = null;
		try {
			String selectStatement = "select override,semester from overridesemester where override = 1";
			PreparedStatement prepStmt = conn.prepareStatement(selectStatement);

			prepStmt.execute("use lookups");
			ResultSet rs = prepStmt.executeQuery();

			if (rs.next()) {
					term = rs.getString(2);
			}

		} catch (SQLException ex) {

			logger.info("Error on term override (method = overrideSemester) -> {}"
							+ ex.getMessage());
		}
		return term;
	}
}
