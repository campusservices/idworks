package com.surepay.provider.payments.query;

import org.springframework.stereotype.Service;

@Service
public class OracleQueries {

	public OracleQueries() {
		super();
	}

	public String getStudentUserQuery() {
		
		String sqlstmt = "select distinct SPRIDEN.SPRIDEN_ID,rtrim(utl_raw.cast_to_varchar2((nlssort(SPRIDEN.SPRIDEN_LAST_NAME, 'nls_sort=binary_ai'))),chr(0)) AS LAST_NAME, "
				+ " rtrim(utl_raw.cast_to_varchar2((nlssort(SPRIDEN.SPRIDEN_FIRST_NAME, 'nls_sort=binary_ai'))),chr(0)) AS FIRST_NAME, SPRIDEN.SPRIDEN_MI " +
			       "from GENERAL.GOREMAL GOREMAL, " +
			       "SATURN.SPRIDEN SPRIDEN, " +
			       "SATURN.SGBSTDN SGBSTDN, " +
			       "SATURN.SPBPERS SPBPERS, " +
			       "SATURN.STVNATN STVNATN, " +
			       "SATURN.STVCOLL STVCOLL, " +
			       "SATURN.STVMAJR STVMAJR, "+
			       "SATURN.STVCOLL STVCOLL1, "+
			       "GENERAL.GOBINTL GOBINTL, "+
			       "SATURN.SMRPRLE SMRPRLE "+
			 "where ( GOREMAL.GOREMAL_PIDM = SPRIDEN.SPRIDEN_PIDM " +
			         "and SGBSTDN.SGBSTDN_PIDM = SPRIDEN.SPRIDEN_PIDM " +
			        "and SPBPERS.SPBPERS_PIDM = SGBSTDN.SGBSTDN_PIDM "+
			         "and SGBSTDN.SGBSTDN_COLL_CODE_1 = STVCOLL.STVCOLL_CODE "+
			         "and SGBSTDN.SGBSTDN_MAJR_CODE_1 = STVMAJR.STVMAJR_CODE "+
			         "and SGBSTDN.SGBSTDN_COLL_CODE_1 = STVCOLL1.STVCOLL_CODE " +
			         "and GOBINTL.GOBINTL_PIDM = SPRIDEN.SPRIDEN_PIDM "+
			         "and SGBSTDN.SGBSTDN_PROGRAM_1 = SMRPRLE.SMRPRLE_PROGRAM ) "+
			   "and ( SPRIDEN.SPRIDEN_CHANGE_IND is null " +
			         " and SGBSTDN.SGBSTDN_STST_CODE in  ('EX','AS','IG','CT') "+
			         "and SGBSTDN.SGBSTDN_STYP_CODE in  ('N','F','R','S','C','E','V','T','O') "+
			         "and SGBSTDN.SGBSTDN_LEVL_CODE in ('GR','UG') "+
			         "and  SGBSTDN.SGBSTDN_TERM_CODE_EFF >= '201510'  " +
			         "and SGBSTDN.SGBSTDN_CAMP_CODE = 'C' "+
			         "and GOREMAL.GOREMAL_EMAL_CODE = 'CA') ";
		
		return sqlstmt;
	}
}
