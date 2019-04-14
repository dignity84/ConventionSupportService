package com.convention;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.convention.request.ConventionSupportRequest;
import com.convention.service.ConventionSupportService;
import com.convention.vo.ConventionSupportInfoVO;


@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "development")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConventionSupportServiceApplicationTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ConventionSupportServiceApplicationTests.class);
	
	@Autowired private TestRestTemplate restTemplate;
	@Autowired private ConventionSupportService conventionSupportService;

	@LocalServerPort private int port;
	
	@SuppressWarnings("rawtypes")
	private ResponseEntity<Map> responseEntity;
	
	private String baseUrl;
	
	HttpHeaders headers = new HttpHeaders();
	
	//연결 서버 셋팅
	@Before
	public void setUp() throws FileNotFoundException, IOException{
		logger.info("-------> setUp :: test() port : " + port);
        this.baseUrl = "http://localhost:" + String.valueOf(port);
        logger.info("-------> setUp ::baseUrl : " + baseUrl);
        //헤더 셋팅
        headers.setContentType(MediaType.APPLICATION_JSON);
	}
	
	@DisplayName("지자체 협약 지원 정보 리스트 조회")
	@Test
	public void test2SearchConventionSupportList() throws IOException {
		logger.info("[searchConventionSupportList Test Start]=================================");
		String testSearchConventionSupportList =  "/convention/support";	
		
		ClassPathResource resource = new ClassPathResource("sample/data.csv");
		File file = resource.getFile();
	
		MockMultipartFile csvFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
		List<ConventionSupportInfoVO> info = conventionSupportService.saveConventionSupportCSVFile(csvFile);
		
		//파일 업로드 결과 출력
		info.stream().forEach(System.out::println);
		
		//httpEntity 셋팅(파라미터 객체)
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		
		//지자체 협약 지원 정보 리스트 조회
		responseEntity = this.restTemplate.exchange(this.baseUrl + testSearchConventionSupportList, HttpMethod.GET, httpEntity, Map.class); 
		logger.info("응답 Body : {}", responseEntity.getBody());

		then (responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);	
		
		logger.info("[searchConventionSupportList Test Start]=================================");

	}
	
	@DisplayName("지자체 협약 지원 정보 조회")
	@Test
	public void test1SearchConventionSupport() throws IOException {
		logger.info("[searchConventionSupport Test Start]=================================");
		String testSearchConventionSupportList =  "/convention/support";
		
		ClassPathResource resource = new ClassPathResource("sample/data2.csv");
		File file = resource.getFile();
	
		MockMultipartFile csvFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
		List<ConventionSupportInfoVO> info = conventionSupportService.saveConventionSupportCSVFile(csvFile);
		
		//파일 업로드 결과 출력
		info.stream().forEach(System.out::println);
		
		//httpEntity 셋팅(파라미터 객체)
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		
		//지자체 협약 지원 정보 조회
		responseEntity = this.restTemplate.exchange(this.baseUrl + testSearchConventionSupportList+"/국토교통부", HttpMethod.GET, httpEntity, Map.class); 
		logger.info("응답 Body : {}", responseEntity.getBody());

		then (responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);	
		
		logger.info("[searchConventionSupport Test Start]=================================");

	}
	
	@DisplayName("지자체 협약 지원 정보 수정")
	@Test
	public void test3ModifyConventionSupport() throws IOException {
		logger.info("[modifyConventionSupport Test Start]=================================");
		String testSearchConventionSupportList =  "/convention/support";
		
		ClassPathResource resource = new ClassPathResource("sample/data3.csv");
		File file = resource.getFile();
	
		MockMultipartFile csvFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
		List<ConventionSupportInfoVO> info = conventionSupportService.saveConventionSupportCSVFile(csvFile);
		
		//파일 업로드 결과 출력
		info.stream().forEach(System.out::println);
		
		//httpEntity 셋팅(파라미터 객체)		
		ConventionSupportRequest requestEntity = ConventionSupportRequest.builder()
				.usage("가공")
				.limit("10조 이내")
				.rate("10%~15%")
				.build();
		HttpEntity<ConventionSupportRequest> httpEntity = new HttpEntity<ConventionSupportRequest>(requestEntity, headers);
				
		//지자체 협약 지원 정보 조회
		responseEntity = this.restTemplate.exchange(this.baseUrl + testSearchConventionSupportList+"/강릉시", HttpMethod.PUT, httpEntity, Map.class); 
		logger.info("응답 Body : {}", responseEntity.getBody());

		then (responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);	
		
		logger.info("[modifyConventionSupport Test Start]=================================");

	}
	
	@DisplayName("지자체 협약 지원 정보 특정 지역 출력(요청 갯수만큼 지원 금액에 따라 내림차순)")
	@Test
	public void test4SearchConventionSupportRegionByAmount() throws IOException {
		logger.info("[searchConventionSupportRegionByAmount Test Start]=================================");
		String testSearchConventionSupportList =  "/convention/support/region-by-amount";
		
		ClassPathResource resource = new ClassPathResource("sample/data4.csv");
		File file = resource.getFile();
	
		MockMultipartFile csvFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
		List<ConventionSupportInfoVO> info = conventionSupportService.saveConventionSupportCSVFile(csvFile);
		
		//파일 업로드 결과 출력
		info.stream().forEach(System.out::println);
		
		//httpEntity 셋팅(파라미터 객체)
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
				
		//지자체 협약 지원 정보 조회
		responseEntity = this.restTemplate.exchange(this.baseUrl + testSearchConventionSupportList+"/5", HttpMethod.GET, httpEntity, Map.class); 
		logger.info("응답 Body : {}", responseEntity.getBody());

		then (responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);	
		
		logger.info("[searchConventionSupportRegionByAmount Test Start]=================================");

	}
	
	@DisplayName("자체 협약 지원 정보 특정 지역 출력(보전비율이 가장 작은 추천 기관명)")
	@Test
	public void test5SearchConventionSupportRegionByRate() throws IOException {
		logger.info("[searchConventionSupportRegionByRate Test Start]=================================");
		String testSearchConventionSupportList =  "/convention/support/region-by-rate";
		
		ClassPathResource resource = new ClassPathResource("sample/data5.csv");
		File file = resource.getFile();
	
		MockMultipartFile csvFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
		List<ConventionSupportInfoVO> info = conventionSupportService.saveConventionSupportCSVFile(csvFile);
		
		//파일 업로드 결과 출력
		info.stream().forEach(System.out::println);
		
		//httpEntity 셋팅(파라미터 객체)				
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
				
		//지자체 협약 지원 정보 조회
		responseEntity = this.restTemplate.exchange(this.baseUrl + testSearchConventionSupportList, HttpMethod.GET, httpEntity, Map.class); 
		logger.info("응답 Body : {}", responseEntity.getBody());

		then (responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);	
		
		logger.info("[searchConventionSupportRegionByRate Test Start]=================================");

	}
}