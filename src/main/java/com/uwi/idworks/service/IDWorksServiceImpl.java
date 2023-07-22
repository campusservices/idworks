package com.uwi.idworks.service;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.uwi.idworks.config.TermConfig;
import com.uwi.idworks.dao.IDWorksDao;
import com.uwi.idworks.dao.OracleDao;
import com.uwi.idworks.entity.BannerStudentInfo;
import com.uwi.idworks.entity.IDWorksInfo;
import com.uwi.idworks.service.contract.IDWorksService;

@Configuration
@Component
public class IDWorksServiceImpl implements IDWorksService  {

	Logger logger = LoggerFactory.getLogger(IDWorksServiceImpl.class);
	
	@Autowired
	private final OracleDao oracleDao;
	
	@Autowired
	private final IDWorksDao worksDao;
	
	@Autowired
    private final TermConfig termConfig;
	
	public IDWorksServiceImpl(TermConfig termConfig, OracleDao oracleDao, IDWorksDao worksDao) {
		this.termConfig = termConfig;
		this.oracleDao = oracleDao;
		this.worksDao = worksDao;
	}
	
	@Scheduled(cron="0 15 5 ? * MON-SUN")
//	@Scheduled(initialDelay = 1000, fixedRate = 40000)
	public void performUpdates( ) {
		
		String overrideSemester = System.getenv("OVERRIDE_SEMESTER") != null ? System.getenv("OVERRIDE_SEMESTER"):termConfig.getOverride();
		String term = overrideSemester == "true" && System.getenv("SEMESTER") != null ? System.getenv("SEMESTER") :termConfig.getSemester();

		try {
			ArrayList<BannerStudentInfo>  studentList = oracleDao.collectInfo(term);
			ArrayList<IDWorksInfo> worksList = worksDao.gatherIDWorksData();
			studentList.stream().forEach(student->{
				List<IDWorksInfo> worksUpdateList = worksList.stream().filter(f->f.getHolderid().equals(student.getId())).map(work->{
					return work;
				}).collect(Collectors.toList());
				IDWorksInfo info = new IDWorksInfo();
				if (worksUpdateList.isEmpty()) {
					student.setLastname(student.getLastname().trim().toUpperCase());
					student.setLevel(student.getLevel().trim());
					student.setStudent("STUDENT");
					worksDao.insertToIDWorks(student);
				} else {
					for (IDWorksInfo m:worksUpdateList) {
						boolean change = false;
						if (m.getFaculty() == null) {
							m.setFaculty(student.getFaculty());
							change = true;
						}
						if (m.getFaculty().trim().indexOf(student.getFaculty().trim())<0) {
							m.setFaculty(student.getFaculty());
							change = true;
						}
						student.setLastname(student.getLastname().trim().toUpperCase());
						if (!m.getLastname().toUpperCase().trim().equals(student.getLastname())) {
							m.setLastname(student.getLastname().toUpperCase());
							change = true;
						}
						if (!m.getFirstname().trim().equals(StringUtils.capitalize(student.getFirstname().trim()))) {
							m.setFirstname(student.getFirstname());
							change = true;
						}
						if (student.getLevel().trim().equals("Graduate")){
				        	student.setLevel("POSTGRAD");
				        } else if (student.getLevel().equals("Undergraduate")){
				        	student.setLevel("UNDERGRAD");
				        }
						if (m.getLevel() != null) {
							int equality = student.getLevel().trim().indexOf(m.getLevel().trim());
							if (equality < 0) {
								m.setLevel(student.getLevel());
								change = true;
							}
						}
						
						if (change)
							worksDao.updateIDWorks(m);
					}
                    
				}
			});
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Scheduled(cron="0 30 11 ? * MON-SUN")
	public void performLaterUpdates( ) {
		 performUpdates();
	}
}
