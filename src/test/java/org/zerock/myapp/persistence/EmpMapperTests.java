package org.zerock.myapp.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.domain.EmpVO;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
//@Slf4j

@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmpMapperTests {
	private SqlSessionFactory sqlSessionFactory;
	
	
	@BeforeAll
	void beforeAll() throws IOException {	// 1회성 전처리
		log.trace("beforeAll() invoked.");
		
		// Step1. SqlSessionFactoryBuilder 객체 생성
		SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
		
		// Step2. 마이바티스 설정파일에 대한 InputStream 생성
		InputStream is = Resources.getResourceAsStream("mybatis-config.xml");		
		
		// Step3. Step1 + Step2 결과를 이용해서, SqlSessionFactory 생성 및 필드에 저장
		SqlSessionFactory factory = builder.build(is);
		
		Objects.requireNonNull(factory);
		
		this.sqlSessionFactory = factory;
		log.info("\t+ this.sqlSessionFactory: {}", this.sqlSessionFactory);
	} // beforeAll

	
//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. testSelectAllEmployees")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testSelectAllEmployees() {
		log.trace("testSelectAllEmployees() invoked.");
		
		// Step1. 필드에 저장된 SqlSessionFactory로부터, SqlSession 얻어냄
		SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		// Step2. Mapper Interface의 구현객체를 획득.
		EmpMapper mapper = sqlSession.<EmpMapper>getMapper(EmpMapper.class);
		log.info("\t+ mapper: {}, type: {}", 
				mapper, mapper.getClass().getName());
				
		// Step3. Mapper Interface의 메소드를 실행
		Objects.requireNonNull(mapper);
		
		List<EmpVO> list = mapper.selectAllEmployees();
		assertNotNull(list);
		
		// Step4. Step3 에서 받은 리턴값 출력
		list.forEach(log::info);
	} // testSelectAllEmployees

	
//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. testDeleteEmployee")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testDeleteEmployee() {
		log.trace("testDeleteEmployee() invoked.");
		
		// Step1. 필드에 저장된 SqlSessionFactory로부터, SqlSession 얻어냄
		SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		// Step2. Mapper Interface의 구현객체(MapperProxy)를 획득.
		EmpMapper mapper = sqlSession.<EmpMapper>getMapper(EmpMapper.class);
		log.info("\t+ mapper: {}, type: {}", mapper, mapper.getClass().getName());
				
		// Step3. Mapper Interface의 메소드를 실행	<--- ***
		// 3-1. 추상메소드에 붙힌 SQL문장에 바인드변수가 하나도 없을때.
//		int deletedRows = mapper.deleteEmployee();
		
		// 3-2. 추상메소드에 붙힌 SQL문장에 바인드변수가 하나만 있을때.
//		int empnoToDelete = 9000;
//		int deletedRows = mapper.deleteEmployee(empnoToDelete);	// 삭제할 사원번호 전달
		
		// 3-3. 추상메소드에 붙힌 SQL문장에 바인드변수가 두개가 있을때.
//		int deletedRows = mapper.deleteEmployee(9000, "Yoseph");
		int deletedRows = mapper.deleteEmployee("Yoseph", 9000);
		
		log.info("\t+ deletedRows: {}", deletedRows);
		
		// Step4. Step3 에서 받은 리턴값 출력
		assertEquals(1, deletedRows);
				
		/*
		// 중요사항: 마이바티스에서 트랜잭션관리는 어떻게 합니까?
		//           (1) 스프링에서 관리: @Transational  (권장)
		//			 (2) SqlSession.commit() 메소드 사용 (권장하지 않음)
		sqlSession.commit();
//		sqlSession.rollback();
		 * 
		 */
	} // testDeleteEmployee

} // end class


