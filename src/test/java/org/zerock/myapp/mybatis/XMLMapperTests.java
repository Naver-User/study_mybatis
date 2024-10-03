package org.zerock.myapp.mybatis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
public class XMLMapperTests {

	
	// ----------------------
	// MyBatis Framework의 두번째 중요한 개념인, XML Mapper 라는 것을
	// 실제 사용예를 통해서, 이해하고 사용법 익히자!!!
	// ----------------------
	private SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;
	
	
	@BeforeAll
	void beforeAll() throws IOException {	// 1회성 전처리
		log.trace("beforeAll() invoked.");
		
		this.sqlSessionFactory = 
			new SqlSessionFactoryBuilder()
				.build(Resources.getResourceAsStream("mybatis-config.xml"));
		
		Objects.requireNonNull(this.sqlSessionFactory);
		log.info("\t+ this.sqlSessionFactory: {}", this.sqlSessionFactory);
	} // beforeAll
	
	@BeforeEach
	void beforeEach() {	// 각 단위테스트 수행시마다 반복수행되는 전처리
		log.trace("beforeAll() invoked.");
		
		this.sqlSession = this.sqlSessionFactory.openSession();
		log.info("\t+ this.sqlSession: {}", this.sqlSession);
		
		assertNotNull(this.sqlSession);		
	} // beforeEach
	
//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. BoardMapper.1 으로 특정된 SQL문장 수행 및 결과수신")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testBoardMapper() {
		log.trace("testBoardMapper() invoked.");
		
		// 우리가 특정한 SQL문장이 결정됩니다.
		// 문법: namespace.sqlId
		
		String namespace = "BoardMapper";
		String sqlId = "1";
		
		String sql = namespace + '.' + sqlId;
			
//									이 구체타입은
//									XML Mapper파일에 등록한
//									resultType 속성의 값과
//									동일하게 지정
//									----------
		String now = this.sqlSession.<String>selectOne(sql);
		log.info("\t+ now: {}", now);
		
		assert now != null;
	} // testBoardMapper
	
//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. scott::emp 테이블 조회하여, 모든 사원정보를 출력")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testEmp() {
		log.trace("testEmp() invoked.");
		
		String namespace = "EmpMapper";
		String sqlId = "1";
		
		String sql = "%s.%s".formatted(namespace, sqlId);
		log.info("\t+ sql: {}", sql);
		
		//- 수행 및 결과 수신 -----
		List<EmpVO> employees = this.sqlSession.<EmpVO>selectList(sql);
		log.info("\t+ Impl. Type: {}", employees.getClass().getName());
		
		//-- 전체사원정보 출력
		Objects.requireNonNull(employees);
		employees.forEach(log::info);
	} // testEmp
	
//	@Disabled
	@Order(3)
	@Test
//	@RepeatedTest(1)
	@DisplayName("3. scott::emp 테이블의 9000번 사원삭제")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testEmpDelete() {
		log.trace("testEmpDelete() invoked.");
		
		String namespace = "EmpMapper";
		String sqlId = "2";				// 삭제 쿼리 아이디
		
		String sql = "%s.%s".formatted(namespace, sqlId);
		log.info("\t+ sql: {}", sql);
		
		//- 수행 및 결과 수신 -----
		Integer affectedRows = this.sqlSession.delete(sql);
		log.info("\t+ affectedRows: {}", affectedRows);
		
		// - 검증수행
		assertEquals(1, affectedRows);
	} // testEmpDelete
	
//	@Disabled
	@Order(4)
	@Test
//	@RepeatedTest(1)
	@DisplayName("4. scott::emp 테이블의 사원삭제를 바인드변수를 통해서 수행")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testEmpDeleteWithBindVariable() {
		log.trace("testEmpDeleteWithBindVariable() invoked.");
		
		// Mapper XML 방식으로 SQL 문장 수행 및 트랜잭션 관리
		String namespace = "EmpMapper";
		String sqlId = "2";
		
		// 문법: namespace + '.' + sqlId
		String mappedStatement = namespace + '.' + sqlId;
		
		boolean autoCommit = true;
		
		SqlSession sqlSession = 
//			this.sqlSessionFactory.openSession();
			// Auto-Commit 매개변수를 가진 메소드 호출				
			this.sqlSessionFactory.openSession(autoCommit);
		
		// namespace 와 sqlId로 매핑되어있는 SQL문장에
		// 바인드변수(#{바인드변수명})가 0..N개가 있을 때에,
		// 값을 전달하기 위해서는, Key=Value 쌍으로 여러값을 가질 수 있는
		// 객체를 만들어서, 아래의 2번째 매개변수에 넣어주셔야 합니다.
		// (1) Map Collection
		// (2) Property 란 개념을 가지는 Java Beans 클래스로부터 생성한
		//     자바빈(줄여서, 빈) 객체
		
		// 우리가 수행시킬 삭제SQL문장에는 바인드 변수가 2개(empno, ename)입니다.
		// 그럼 위 (1) 또는 (2)로 객체를 만들어서 아래 메소드의 2번째 매개변수에
		// 인자값으로 넣어주시면 됩니다.
		// 자!!! 만드세요!
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("ename", "Yoseph");
		paramMap.put("empno", 9000);
		
		log.info("\t+ paramMap: {}", paramMap);
		
		int deletedRows = sqlSession.delete(mappedStatement, paramMap);
		
		assertEquals(1, deletedRows);
	} // testEmpDeleteWithBindVariable

	
	
	
	
	
	
	
	
} // end class
