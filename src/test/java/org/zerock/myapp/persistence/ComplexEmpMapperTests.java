package org.zerock.myapp.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.domain.EmpVO;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComplexEmpMapperTests {
	
	
//	@Disabled
	@Order(1)
//	@Test
	@RepeatedTest(1)
	@DisplayName("1. testRangeEmployees")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void testRangeEmployees() throws IOException {
		log.trace("testRangeEmployees() invoked.");
		
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory factory = builder.build(is);
		SqlSession sqlSession = factory.openSession();	// DQL이니까요. TX관리 불필요.
		
		ComplexEmpMapper mapper = 
			sqlSession.<ComplexEmpMapper>getMapper(ComplexEmpMapper.class);
		
		// ------------
		// 혼합방식의 추상메소드 호출 -> 자동실행규칙에 맞게 설정했으면,
		//                               자동으로 Mapper XML 을 찾고, 그 안에
		//								 등록된 SQL문장 수행
		// ------------
		int startEmpno = 7500;
		int endEmpno = 7600;
		
		// 현재는 SQL문장에 RANGE 범위값이 하드코딩 되어 있기 때문에,
		// 사실상 무의미하지만, 그 다음테스트에는 이 매개변수로 테스트 합니다.
		List<EmpVO> list = mapper.rangeEmployees(startEmpno, endEmpno);
		
		Objects.requireNonNull(list);
		list.forEach(log::info);
	} // testRangeEmployees
	
	
//	@Disabled
	@Order(2)
//	@Test
	@RepeatedTest(1)
	@DisplayName("2. testGetCurrentDate")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void testGetCurrentDate() throws IOException {
		log.trace("testGetCurrentDate() invoked.");
		
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
		SqlSessionFactory factory = builder.build(is);
		SqlSession sqlSession = factory.openSession();	// DQL이니까요. TX관리 불필요.
		
		ComplexEmpMapper mapper = 
			sqlSession.<ComplexEmpMapper>getMapper(ComplexEmpMapper.class);
		
		// ------------
		// 어노테이션 방식 메소드 수행
		// ------------
		Date now = mapper.getCurrentDate();
		Objects.requireNonNull(now);
		log.info("\t+ now: {}", now);
	} // testGetCurrentDate

} // end class
