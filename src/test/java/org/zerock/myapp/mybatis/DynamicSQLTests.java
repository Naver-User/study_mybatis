package org.zerock.myapp.mybatis;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.zerock.myapp.domain.EmpVO;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
//@Slf4j

@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DynamicSQLTests {
	private SqlSessionFactory sqlSessionFactory;
	
	
	@BeforeAll
	void beforeAll() throws IOException {
		log.trace("beforeAll() invoked.");
		
		SqlSessionFactoryBuilder builder =
			new SqlSessionFactoryBuilder();
		
		InputStream configIS = 
			Resources.getResourceAsStream("mybatis-config.xml");
		
		this.sqlSessionFactory = builder.build(configIS);
		
		assertNotNull(this.sqlSessionFactory);
	} // beforeAll
	
	
//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. testWhereAndIfTag")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testWhereAndIfTag() {
		log.trace("testWhereAndIfTag() invoked.");
		
		// Step1. Mapper XML 방식에 필요한, 매핑된 SQL문장을 찾아가기
		//        위한 값 생성
		String namespace = "EmpMapper";
		String sqlId = "3";
		String mappedStatement = namespace+'.'+sqlId;
		
		// Step2. SqlSession 획득
		@Cleanup SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		// Step3. SqlSession을 통해, 매핑된 문장(MappedStatement)을
		// 수행.
		List<EmpVO> list = sqlSession.<EmpVO>selectList(mappedStatement, "A");
		
		// Step4. 검색된 사원목록출력
		Objects.requireNonNull(list);
		list.forEach(log::info);
	} // testWhereAndIfTag	
	
	
//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. testWhereAndIfTagWithMultipleConditions")
	@Timeout(value=3L, unit=TimeUnit.SECONDS)
	void testWhereAndIfTagWithMultipleConditions() {
		log.trace("testWhereAndIfTagWithMultipleConditions() invoked.");
		
		// Step1. Mapper XML 방식에 필요한, 매핑된 SQL문장을 찾아가기
		//        위한 값 생성
		String namespace = "EmpMapper";
		String sqlId = "4";
		String mappedStatement = namespace+'.'+sqlId;
		
		// Step2. SqlSession 획득
		@Cleanup SqlSession sqlSession = this.sqlSessionFactory.openSession();
		
		// Step3. SqlSession을 통해, 매핑된 문장(MappedStatement) 수행.
		// 작동원리: 마이바티스는, 우리가 전달한 Map 객체안에 있는
		// 요소를 어떻게 SQL문의 각 바인드 변수에 전달하는가?
		// SQL문의 바인드변수명을 Map의 Key로 사용해서, Map 을 검색하여
		// 값을 얻어내어, 바인딩시킵니다.
		// 자바빈 객체도 동일합니다. 자바빈이 가지고 있는 프로퍼티명으로
		// 바인드변수명과 일치하는 프로퍼티의 Getter 메소드를 호출해서,
		// 필드값을 얻어서 바인딩시킵니다.
		
		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put(bind변수명, 바인딩값);
		
//		paramMap.put("ename", "A");		
//		paramMap.put("startEmpno", 7500);
//		paramMap.put("endEmpno", 7600);
		
		List<EmpVO> list = 
			sqlSession.<EmpVO>selectList(mappedStatement, paramMap);
		
		// Step4. 검색된 사원목록출력
		Objects.requireNonNull(list);
		list.forEach(log::info);
	} // testWhereAndIfTagWithMultipleConditions	

} // end class
