package com.uwi.idworks.query;

public class Queries {
   public String getStudentQuery() {
	   String selectStatement = "SELECT SPRIDEN.SPRIDEN_ID,SGBSTDN.SGBSTDN_TERM_CODE_EFF,   rtrim(utl_raw.cast_to_varchar2((nlssort(SPRIDEN.SPRIDEN_LAST_NAME, 'nls_sort=binary_ai'))),chr(0))  SPRIDEN_LAST_NAME, "
				+ "rtrim(utl_raw.cast_to_varchar2((nlssort(SPRIDEN.SPRIDEN_FIRST_NAME, 'nls_sort=binary_ai'))),chr(0)) SPRIDEN_FIRST_NAME, rtrim(utl_raw.cast_to_varchar2((nlssort(SPRIDEN.SPRIDEN_MI, 'nls_sort=binary_ai'))),chr(0))  AS MI, "
				+ "SGBSTDN.SGBSTDN_LEVL_CODE, SGBSTDN.SGBSTDN_COLL_CODE_1 AS FACULTY, "
				+ "SGBSTDN.SGBSTDN_CAMP_CODE, SGBSTDN.SGBSTDN_STST_CODE, SGBSTDN.SGBSTDN_STYP_CODE, "
				+ "SPRIDEN.SPRIDEN_ACTIVITY_DATE,SPBPERS.SPBPERS_BIRTH_DATE, STVMAJR.STVMAJR_DESC, SGBSTDN.SGBSTDN_PROGRAM_1, "
				+ " DECODE(STVNATN.STVNATN_NATION,NULL, 'Azerbaijan',STVNATN.STVNATN_NATION) AS STVNATN_NATION, SPRIDEN.SPRIDEN_PIDM, SGBSTDN.SGBSTDN_FULL_PART_IND "
				+ "FROM ((SPBPERS INNER JOIN (SGBSTDN INNER JOIN SPRIDEN ON "
				+ "SGBSTDN.SGBSTDN_PIDM = SPRIDEN.SPRIDEN_PIDM) ON "
				+ "SPBPERS.SPBPERS_PIDM = SGBSTDN.SGBSTDN_PIDM) INNER JOIN STVMAJR ON "
				+ "SGBSTDN.SGBSTDN_MAJR_CODE_1 = STVMAJR.STVMAJR_CODE) LEFT JOIN (GOBINTL LEFT JOIN STVNATN ON "
				+ "GOBINTL.GOBINTL_NATN_CODE_LEGAL = STVNATN.STVNATN_CODE) ON "
				+ "SGBSTDN.SGBSTDN_PIDM = GOBINTL.GOBINTL_PIDM " 
				+"WHERE (((SGBSTDN.SGBSTDN_STST_CODE) = ? OR (SGBSTDN.SGBSTDN_STST_CODE) = ? OR (SGBSTDN.SGBSTDN_STST_CODE) = ? OR (SGBSTDN.SGBSTDN_STST_CODE) = ?) AND "
				+ "((SGBSTDN.SGBSTDN_STYP_CODE) In (?,?,?,?,?,?,?,?,?) AND (SPRIDEN.SPRIDEN_ID NOT LIKE ?) AND "
				+ "        ((SGBSTDN.SGBSTDN_TERM_CODE_EFF) in (?,?,?,'201905')))  "
				+ "AND ((SPRIDEN.SPRIDEN_CHANGE_IND) Is Null) AND "
				+ "((SPBPERS.SPBPERS_DEAD_IND) Is Null) AND ((SPBPERS.SPBPERS_BIRTH_DATE) Is Not null)  "
				+ "and ( SGBSTDN.SGBSTDN_PIDM, SGBSTDN.SGBSTDN_TERM_CODE_EFF )  = "
				+ "( select SGBSTDN1.SGBSTDN_PIDM, "
				+ "Max( SGBSTDN1.SGBSTDN_TERM_CODE_EFF ) AS Max_SGBSTDN_TERM_CODE_EFF "
				+ "from SATURN.SGBSTDN SGBSTDN1 "
				+ "where SGBSTDN1.SGBSTDN_PIDM = SGBSTDN.SGBSTDN_PIDM "
				+ "group by SGBSTDN1.SGBSTDN_PIDM ) ) "
				+ "order by SPRIDEN.SPRIDEN_LAST_NAME, SPRIDEN.SPRIDEN_FIRST_NAME,SGBSTDN.SGBSTDN_TERM_CODE_EFF ";

		return selectStatement;

   }
}