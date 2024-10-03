package org.zerock.myapp.mybatis;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import lombok.Cleanup;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//@Log4j2
@Slf4j

@NoArgsConstructor

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlSessionFactoryBuilderTests {
	
	// SqlSessionFactory 객체를 MyBatis 설정파일대로 생성해주는
	// 건설사(SqlSessionFactoryBuilder) 객체를 얻는 방법을 배우고
	// 얻어낸 건설사를 통해서, 현재 싯점의 우리의 목표인 공장을
	// 세우자!!!
	
	private SqlSessionFactoryBuilder builder;
	private InputStream configIS;
	private SqlSessionFactory sqlSessionFactory;
	
	
	@BeforeAll
	void beforeAll() throws IOException {	// 1회성 전처리: 건설사를 만들어서, 필드에 저장하자!!!
		log.trace("beforeAll() invoked.");
		
		// -------------------
		// Step1. 재사용할 Builder 객체를 생성하여, 필드에 저장(재사용위해)
		// -------------------
		this.builder = new SqlSessionFactoryBuilder();
		log.info("\t+ this.builder: {}", this.builder);
		
		assertNotNull(this.builder);

		// -------------------
		// Step2. 공장의 설계도에 해당되는, MyBatis의 설정파일에 대한 입력스트림 생성
		// -------------------
		
		// 2-1. 설정파일의 경로 지정
		String configPath = "mybatis-config.xml";
		InputStream is = Resources.getResourceAsStream(configPath);
		log.info("\tStep2. is: {}", is);
		
		Objects.requireNonNull(is);
		this.configIS = is;		
	} // beforeAll
	
	
	@AfterAll
	void afterAll() {	// 1회성 후처리: 설정파일에 대한 입력스트림 닫기
		log.trace("afterAll() invoked.");
		
		try { this.configIS.close(); }
		catch (IOException _ignored) {;;}
	} // afterAll

	
//	@Disabled
	@Order(1)
	@Test
//	@RepeatedTest(1)
	@DisplayName("1. testCreateSqlSessionFactory")
	@Timeout(value=2L, unit=TimeUnit.SECONDS)
	void testCreateSqlSessionFactory() {
		log.trace("testCreateSqlSessionFactory() invoked.");
		
		// Step1. 필드에 저장된 Builder 객체와 설정파일에 대한 입력스트림을 기반으로
		//        MyBatis 사용시 항상 요구되는 SqlSessionFactory 생성
		SqlSessionFactory sqlSessionFactory = this.builder.build(this.configIS);
		log.info("\tStep1. sqlSessionFactory: {}", sqlSessionFactory);
		
		assert sqlSessionFactory != null;
		
		// Step2. 이후 여러 단위테스트에서 재사용할 수 있도록, 필드에 저장
		this.sqlSessionFactory = sqlSessionFactory;
	} // testCreateSqlSessionFactory

	
//	@Disabled
	@Order(2)
	@Test
//	@RepeatedTest(1)
	@DisplayName("2. testSqlSessionFactory")
	@Timeout(value=1L, unit=TimeUnit.MINUTES)
	void testSqlSessionFactory() {
		log.trace("testSqlSessionFactory() invoked.");
		
		for(int i = 0 ; i < 300; i++) {
			// Step1. 공장(Factory)으로부터, MyBatis의 가장 중요한 핵심객체인
			//        SqlSession 객체를 획득
			@Cleanup
			SqlSession sqlSession = this.sqlSessionFactory.openSession();
			log.info("\tStep1. sqlSession[{}]: {}", i, sqlSession);
			
			// Step2. 추상적 연결(데이터베이스에 대한)을 의미하는 SqlSession 객체의
			//        Low-level 에는 실제, 데이터소스에서 얻어낸 물리적인 JDBC Connection
			//        있어야 합니다. 그래서 실제 물리적인 연결을 얻어내어 검증수행
			Objects.requireNonNull(sqlSession);
			
			Connection conn = sqlSession.getConnection();
//			log.info("\tStep2. conn: {}", conn);
			
			assertNotNull(conn);
		} // for
	} // testSqlSessionFactory
	
	
	
} // end class
