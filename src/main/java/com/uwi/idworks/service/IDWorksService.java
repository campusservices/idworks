package com.uwi.idworks.service;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.uwi.idworks.dao.IDWorksDao;
import com.uwi.idworks.dao.OracleDao;
import com.uwi.idworks.dao.OverrideDao;
import com.uwi.idworks.entity.BannerStudentInfo;
import com.uwi.idworks.entity.IDWorksInfo;

@Service
public class IDWorksService {

	Logger logger = LoggerFactory.getLogger(IDWorksService.class);
	
	@Autowired
	private OverrideDao overrideDao;
	
	@Autowired
	private OracleDao oracleDao;
	
	@Autowired
	private IDWorksDao worksDao;
	
	@Scheduled(cron="0 15 5 ? * MON-SUN")
//	@Scheduled(initialDelay = 1000, fixedRate = 40000)
	public void performUpdates( ) {
		String term = overrideDao.overrideSemester();
		try {
			ArrayList<BannerStudentInfo>  studentList = oracleDao.collectInfo(term);
			ArrayList<IDWorksInfo> worksList = worksDao.gatherIDWorksData();
			studentList.stream().forEach(student->{
				List<IDWorksInfo> worksUpdateList = worksList.stream().filter(f->f.getHolderid().equals(student.getId())).map(work->{
					return work;
				}).collect(Collectors.toList());
				IDWorksInfo info = new IDWorksInfo();
				if (worksUpdateList.isEmpty()) {
					info.setHolderid(student.getId());
					info.setFaculty(student.getFaculty());
					info.setFirstname(student.getFirstname());
					info.setLastname(student.getLastname().toUpperCase());
					info.setInitial(student.getInitial());
					info.setLevel(student.getLevel().trim());
					info.setStudent("STUDENT");
					worksDao.insertToIDWorks(info);
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
						if (!m.getLastname().trim().equals(student.getLastname().trim().toUpperCase())) {
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
	@Scheduled(cron="0 15 10 ? * MON-SUN")
	public void performLaterUpdates( ) {
		 performUpdates();
	}
}
