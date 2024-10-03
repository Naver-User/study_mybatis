package org.zerock.myapp.persistence;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.zerock.myapp.domain.EmpVO;


// 혼합방식(어노테이션 + Mapper XML)으로 구동시킬 Mapper Interface
public interface ComplexEmpMapper {

	// (1) 어노테이션 방식
	@Select("SELECT current_date FROM dual")
	public abstract Date getCurrentDate();
	
	// (2) Mapper XML 방식	<---- "자동실행규칙"에 따라, SQL문장 수행
	public abstract List<EmpVO> rangeEmployees(Integer startEmpno, Integer endEmpno);
	
	
} // end interface
