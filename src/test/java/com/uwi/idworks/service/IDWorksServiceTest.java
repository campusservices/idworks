package com.uwi.idworks.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junitpioneer.jupiter.ClearEnvironmentVariable;
import org.junitpioneer.jupiter.SetEnvironmentVariable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mysql.jdbc.Connection;
import com.uwi.idworks.config.OracleConfig;
import com.uwi.idworks.config.TermConfig;
import com.uwi.idworks.dao.IDWorksDao;
import com.uwi.idworks.dao.OracleConnection;
import com.uwi.idworks.dao.OracleDao;
import com.uwi.idworks.entity.BannerStudentInfo;
import com.uwi.idworks.entity.IDWorksInfo;
import com.uwi.idworks.query.Queries;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
//@ContextConfiguration(classes = IDWorksServiceImpl.class)
@EnableConfigurationProperties(value = OracleConfig.class)
@TestPropertySource(properties = {
	    "SEMESTER=202230",
	})
class IDWorksServiceTest {
	
	@InjectMocks
	IDWorksServiceImpl worksService;
	
	@Mock
	OracleDao oracleDao;
	
	@Mock
    private OracleConfig service;
	
	@Mock
	TermConfig termConfig;
	
	@Mock
	IDWorksDao worksDao;
	
	@Mock
	Connection conn;
	
	@Autowired
	OracleConfig oracleConfig;
	
	@Mock
	OracleConnection oracleConnection;
	
	@Mock
	private Queries query;
	
	@Mock
	private PreparedStatement prepStmt;

	@Mock
	private ResultSet rs;
	


	@Test
	@SetEnvironmentVariable(key = "OVERRIDE_SEMESTER", value = "true")
	@SetEnvironmentVariable(key = "SEMESTER", value = "202320")
	public void performanceUpdateVerifyZeroChangesTest() throws Exception {
		
//		
		
		String selectStatement = "select * from spriden";
	    ArrayList<BannerStudentInfo> list = new ArrayList<BannerStudentInfo>();
	    ArrayList<IDWorksInfo> worksList = new ArrayList<IDWorksInfo>();
	    oracleConnection = mock(OracleConnection.class);
	    conn = mock(Connection.class);
	    query = mock(Queries.class);
	    service = mock(OracleConfig.class);
	    oracleDao = mock(OracleDao.class);
	    
	    when(query.getStudentQuery()).thenReturn(selectStatement);
	    when(oracleConnection.getConnection()).thenReturn(conn);
	    when(conn.prepareStatement(query.getStudentQuery())).thenReturn(prepStmt);
	    when(oracleDao.collectInfo(Mockito.any())).thenReturn(list); 
	    when(worksDao.gatherIDWorksData()).thenReturn(worksList);
		when(service.getDriver()).thenReturn(oracleConfig.getDriver());
		when(service.getConnect()).thenReturn(oracleConfig.getConnect());
		when(service.getUsername()).thenReturn(oracleConfig.getUsername());
		when(service.getPassword()).thenReturn(oracleConfig.getPassword());
		
		
	    BannerStudentInfo stu = new BannerStudentInfo();
		stu.setId("20221121");
		stu.setFirstname("Robin");
		stu.setLastname("Johnson");
		stu.setFaculty("Applied Sciences");
		stu.setLevel("UNDERGRAD");
		list.add(stu);
		BannerStudentInfo stu1 = new BannerStudentInfo();
		stu1.setId("432321112");
		stu1.setFirstname("Anthony");
		stu1.setLastname("Mcdowell");
		stu1.setFaculty("Economics");
		stu1.setLevel("UNDERGRAD");
		list.add(stu1);
	    
	    
	    IDWorksInfo info = new IDWorksInfo();
	    info.setHolderid("20221121");
	    info.setFirstname("Robin");
		info.setLastname("Johnson");
		info.setFaculty("Applied Sciences");
		info.setLevel("UNDERGRAD");
		worksList.add(info);
		
		IDWorksInfo info1 = new IDWorksInfo();
		info1.setHolderid("432321112");
	    info1.setFirstname("Anthony");
		info1.setLastname("Mcdowell");
		info1.setFaculty("Economics");
		info1.setLevel("UNDERGRAD");
		worksList.add(info1);
		
		worksService.performUpdates();
		
		verify(worksDao, never()).updateIDWorks(info);
		verify(worksDao, never()).insertToIDWorks(stu);
		verify(worksDao,  never()).updateIDWorks(info1);
		verify(worksDao, never()).insertToIDWorks(stu1);
		
		
	}
	
	@Test
	public void performanceUpdateVerifyOneChangeTest() throws Exception {
		String selectStatement = "select * from spriden";
	    ArrayList<BannerStudentInfo> list = new ArrayList<BannerStudentInfo>();
	    ArrayList<IDWorksInfo> worksList = new ArrayList<IDWorksInfo>();
	    oracleConnection = mock(OracleConnection.class);
	    conn = mock(Connection.class);
	    query = mock(Queries.class);
	    service = mock(OracleConfig.class);
	    
	    
	    when(query.getStudentQuery()).thenReturn(selectStatement);
	    when(oracleConnection.getConnection()).thenReturn(conn);
	    when(conn.prepareStatement(query.getStudentQuery())).thenReturn(prepStmt);
	    when(oracleDao.collectInfo("202230")).thenReturn(list); 
	    when(worksDao.gatherIDWorksData()).thenReturn(worksList);
		when(service.getDriver()).thenReturn(oracleConfig.getDriver());
		when(service.getConnect()).thenReturn(oracleConfig.getConnect());
		when(service.getUsername()).thenReturn(oracleConfig.getUsername());
		when(service.getPassword()).thenReturn(oracleConfig.getPassword());
		when(termConfig.getOverride()).thenReturn("true");
		when(termConfig.getSemester()).thenReturn("202230");
		
	    BannerStudentInfo stu = new BannerStudentInfo();
		stu.setId("20221121");
		stu.setFirstname("Robin");
		stu.setLastname("Harris");
		stu.setFaculty("Applied Sciences");
		stu.setLevel("UNDERGRAD");
		list.add(stu);
		BannerStudentInfo stu1 = new BannerStudentInfo();
		stu1.setId("432321112");
		stu1.setFirstname("Anthony");
		stu1.setLastname("Mcdowell");
		stu1.setFaculty("Economics");
		stu1.setLevel("UNDERGRAD");
		list.add(stu1);
	    
	    
	    IDWorksInfo info = new IDWorksInfo();
	    info.setHolderid("20221121");
	    info.setFirstname("Robin");
		info.setLastname("Johnson");
		info.setFaculty("Applied Sciences");
		info.setLevel("UNDERGRAD");
		worksList.add(info);
		
		IDWorksInfo info1 = new IDWorksInfo();
		info1.setHolderid("432321112");
	    info1.setFirstname("Anthony");
		info1.setLastname("Mcdowell");
		info1.setFaculty("Economics");
		info1.setLevel("UNDERGRAD");
		worksList.add(info1);
		
		worksService.performUpdates();;
		
		verify(worksDao, times(1)).updateIDWorks(info);
		verify(worksDao, never()).insertToIDWorks(stu);
		verify(worksDao,  never()).updateIDWorks(info1);
		verify(worksDao, never()).insertToIDWorks(stu1);
		
		
	}
	
	@Test
	public void performanceUpdateVerifyOneInsertTest() throws Exception {
		String selectStatement = "select * from spriden";
	    ArrayList<BannerStudentInfo> studentList = new ArrayList<BannerStudentInfo>();
	    ArrayList<IDWorksInfo> worksList = new ArrayList<IDWorksInfo>();
	    oracleConnection = mock(OracleConnection.class);
	    conn = mock(Connection.class);
	    query = mock(Queries.class);
	    service = mock(OracleConfig.class);
	    
	    BannerStudentInfo stu = new BannerStudentInfo();
		stu.setId("20221121");
		stu.setFirstname("Robin");
		stu.setLastname("Johnson");
		stu.setFaculty("Applied Sciences");
		stu.setLevel("Undergraduate");
		studentList.add(stu);
		
		BannerStudentInfo stu1 = new BannerStudentInfo();
		stu1.setId("40003421");
		stu1.setFirstname("Pamela");
		stu1.setLastname("Jones");
		stu1.setFaculty("Economics");
		stu1.setLevel("Undergraduate");
		studentList.add(stu1);
	    
		IDWorksInfo info = new IDWorksInfo();
	    info.setHolderid("20221121");
	    info.setFirstname("Robin");
		info.setLastname("Johnson");
		info.setFaculty("Applied Sciences");
		info.setLevel("UNDERGRAD");
		worksList.add(info);
		
		IDWorksInfo info1 = new IDWorksInfo();
		info1.setHolderid("432321112");
	    info1.setFirstname("Anthony");
		info1.setLastname("Mcdowell");
		info1.setFaculty("Economics");
		info1.setLevel("UNDERGRAD");
		worksList.add(info1);
		
		
	    when(query.getStudentQuery()).thenReturn(selectStatement);
	    when(oracleConnection.getConnection()).thenReturn(conn);
	    when(conn.prepareStatement(query.getStudentQuery())).thenReturn(prepStmt);
	    when(oracleDao.collectInfo(null)).thenReturn(studentList); 
	    when(worksDao.gatherIDWorksData()).thenReturn(worksList);
		when(service.getDriver()).thenReturn(oracleConfig.getDriver());
		when(service.getConnect()).thenReturn(oracleConfig.getConnect());
		when(service.getUsername()).thenReturn(oracleConfig.getUsername());
		when(service.getPassword()).thenReturn(oracleConfig.getPassword());
		
		worksService.performUpdates();
		
		//verify(worksDao, times(1)).updateIDWorks(info);
		verify(worksDao, never()).insertToIDWorks(stu);
		//verify(worksDao,  times(1)).updateIDWorks(info1);
		verify(worksDao, times(1)).insertToIDWorks(stu1);
		
		
	}

}
