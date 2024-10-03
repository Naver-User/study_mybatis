package org.zerock.myapp.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.zerock.myapp.domain.EmpVO;

// MyBatis 프레임워크가 지원하는 Mapper Interface로
// 이 인터페이스에 선언된 추상메소드 + SQL문장 결합시켜서,
// 개발자는 인터페이스의 메소드를 호출하면, 자연스럽게 결합된 SQL문장이 수행되고
// 그 수행결과값은, 추상메소드의 리턴타입대로 반환시켜줍니다.
// 결국, 개발자가 MVC패턴대로 하자면, 반드시 DAO패턴대로 DB조작을 하는 클래스를
// 만들어야 하지만, Mybatis 프레임워크의 이 Mapper Interface를 이용하면,
// 따로 DAO클래스를 만들필요도 없고, 아래의 인터페이스 만으로, dAO역할을 수행


//FQCN = 패키지명 + 간단한참조타입명 
//     = org.zerock.myapp.persistence.EmpMapper
public interface EmpMapper {
	
	@Select("SELECT * FROM emp")
	public abstract List<EmpVO> selectAllEmployees();


	
	// 1. 바인드변수가 1개 일 때
//	@Delete("DELETE FROM emp WHERE empno = #{empno}")	
	public abstract int deleteEmployee(int empno);
	
	// 2. 바인드변수가 2개 일 때
	@Delete("DELETE FROM emp WHERE empno = #{empno2} AND ename = #{ename2}")	
//	public abstract int deleteEmployee(int empno, String ename);
	
	// XX : 매개변수이름으로, 바인드변수를 찾아간다 확인
//	public abstract int deleteEmployee(String ename, int empno);
	
	// 바인드변수명과 매개변수명이 어쩔수없이 다를때의 처리방법: @Param 어노테이션 사용
	public abstract Integer deleteEmployee(
			@Param("ename2") String ename, 
			@Param("empno2") Integer empno
		);

} // end interface
