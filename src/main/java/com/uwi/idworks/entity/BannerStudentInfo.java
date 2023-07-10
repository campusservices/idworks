package com.uwi.idworks.entity;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BannerStudentInfo {

	private String id;
	private String lastname;
	private String firstname; 
    private String initial;
    private String middleName;
    private String faculty;
    private String level;
    private String status;
    private String styp;
    private String term;
    private String activityDate;
    private Date birthDate;
    private String campusCode;
    private String action;
    private String student;
    private String major;
    private String country;
    private String pin;
    private String email;
    private int emailCnt;
    private int pidm;
    private boolean toTable;
    private String campus;
    private String operation;
    private boolean toPrint;
    private String UID;
    private String facultyCode;

}
