package org.zerock.myapp.domain;

import java.util.Date;

import lombok.Value;


@Value
// EMP 테이블의 한개의 행을 저장할 수 있는 클래스
public class EmpVO {	// POJO
	private Integer empno;
	private String ename;
	private String job;
	private Integer mgr;
	
	// JAVA8에서 새로개발된 날짜관련 타입은 아래 3개가 핵심입니다:
	//	(1) LocalDate (날짜만 저장)
	//	(2) LocalTime (시간만 저장)
	//	(3) LocalDateTime (날짜 + 시간 저장)
//	private LocalDate hireDate;
	private Date hireDate;
//	private Timestamp hireDate;
	
	private Double sal;
	private Double comm;
	private Integer deptno;
	

} // end class
