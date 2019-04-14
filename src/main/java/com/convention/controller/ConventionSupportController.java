package com.convention.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.convention.repository.ConventionSupportInfo;
import com.convention.repository.ConventionSupportInfoDT;
import com.convention.request.ConventionSupportRequest;
import com.convention.response.ConventionSupportResponse;
import com.convention.service.ConventionSupportService;
import com.convention.vo.ConventionSupportInfoVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 지자체 협약지원 서비스 컨드롤러
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@RestController
public class ConventionSupportController {

	@Autowired private ConventionSupportService conventionSupportService;
	
	/**
	 * 지자체 협약 지원 정보 데이터 저장(파일업로드)
	 * @param conventionSupportCSVFile 지자체 협약 지원 정보 관련 CSV 파일
	 * @return {@link ConventionSupportResponse} 지자체 협약 지원 정보 응답 객체 
	 */
    @ApiOperation(value = "지자체 협약 지원 정보 데이터 저장(파일업로드)", nickname = "지자체 협약 지원 정보 데이터 저장(파일업로드)")
	@PostMapping("/convention/support")
	public ConventionSupportResponse saveConventionSupportCSVFile(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "conventionSupportCSVFile", value = "지자체 협약 지원 데이터 파일(확장명 : CSV)", required = true) 
			@RequestParam(value = "conventionSupportCSVFile", required = true) MultipartFile csvFile) {

		//1. 지자체 협약 지원 정보 저장
		List<ConventionSupportInfoVO> conventionSupportInfoList = conventionSupportService.saveConventionSupportCSVFile(csvFile);
	
		//2. 결과 값 응답
		return ConventionSupportResponse.builder()
					.total(conventionSupportInfoList.size())
					.conventionSupportInfoList(conventionSupportInfoList)
					.build();
	}
    
	/**
	 * 지자체 협약 지원 정보 리스트 조회
	 * @return {@link ConventionSupportResponse} 지자체 협약 지원 정보 응답 객체 
	 */
    @ApiOperation(value = "지자체 협약 지원 정보 리스트 조회", nickname = "지자체 협약 지원 정보 리스트 조회")
	@GetMapping("/convention/support")
	public ConventionSupportResponse searchConventionSupportList(HttpServletRequest request, HttpServletResponse response) {

		//1. 지자체 협약 지원 정보 리스트 조회
		List<ConventionSupportInfoVO> conventionSupportInfoList = conventionSupportService.searchConventionSupportList();
	
		//2. 결과 값 응답
		return ConventionSupportResponse.builder()
					.total(conventionSupportInfoList.size())
					.conventionSupportInfoList(conventionSupportInfoList)
					.build();
	}
    
	/**
	 * 지자체 협약 지원 정보 조회
	 * @param region 지자체명
	 * @return {@link ConventionSupportResponse} 지자체 협약 지원 정보 응답 객체 
	 */
    @ApiOperation(value = "지자체 협약 지원 정보 조회", nickname = "지자체 협약 지원 정보 조회")
	@GetMapping("/convention/support/{region}")
	public ConventionSupportResponse searchConventionSupport(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "region", value = "지자체명", example = "강릉시", required = true) 
			@PathVariable(value = "region", required = true) @NotEmpty String region) {

		//1. 지자체 협약 지원 정보 조회
		ConventionSupportInfoVO conventionSupportInfo = conventionSupportService.searchConventionSupport(region);
	
		//2. 결과 값 응답
		return ConventionSupportResponse.builder()
					.regionName(conventionSupportInfo.getRegionName())
					.target(conventionSupportInfo.getTarget())
					.usage(conventionSupportInfo.getUsage())
					.limit(conventionSupportInfo.getLimits())
					.rate(conventionSupportInfo.getRate())
					.institute(conventionSupportInfo.getRecommend())
					.mgmt(conventionSupportInfo.getMgmt())
					.reception(conventionSupportInfo.getReception())
					.build();
	}
    
    /**
     * 지자체 협약 지원 정보 수정
     * @param request
     * @param response
     * @param region 지자체명
     * @param conventionSupportRequest 요청 파라미터 Body 객체
     * @return {@link ConventionSupportResponse} 지자체 협약 지원 정보 응답 객체
     */
    @ApiOperation(value = "지자체 협약 지원 정보 수정", nickname = "지자체 협약 지원 정보 수정")
	@PutMapping("/convention/support/{region}")
	public ConventionSupportResponse modifyConventionSupport(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "region", value = "지자체명", example = "강릉시", required = true) 
			@PathVariable(value = "region", required = true) @NotEmpty String region,
    		@ApiParam(name = "conventionSupportRequest", value = "지자체 협약 정보", required = true) 
    		@Validated @RequestBody @NotNull ConventionSupportRequest conventionSupportRequest) {
    	
    	// 1. 요청 객체 DT객체 생성.
    	ConventionSupportInfoDT conventionSupportDt = ConventionSupportInfoDT.builder()
    															.regionName(region)
    															.build();
    	BeanUtils.copyProperties(conventionSupportRequest, conventionSupportDt);
    	

		//2. 지자체 협약 지원 정보 수정
		ConventionSupportInfoVO conventionSupportInfo = conventionSupportService.modifyConventionSupport(conventionSupportDt);
	
		//3. 결과 값 응답
		return ConventionSupportResponse.builder()
					.regionName(conventionSupportInfo.getRegionName())
					.target(conventionSupportInfo.getTarget())
					.usage(conventionSupportInfo.getUsage())
					.limit(conventionSupportInfo.getLimits())
					.rate(conventionSupportInfo.getRate())
					.institute(conventionSupportInfo.getRecommend())
					.mgmt(conventionSupportInfo.getMgmt())
					.reception(conventionSupportInfo.getReception())
					.build();
	}
    
    /**
     * 지자체 협약 지원 정보 특정 지역 출력(요청 갯수만큼 지원 금액에 따라 내림차순)
     * @param request
     * @param response
     * @param regionCount 요청 지자체 수
     * @return {@link ConventionSupportResponse} 지자체 협약 지원 정보 응답 객체
     */
    @ApiOperation(value = "지자체 협약 지원 정보 특정 지역 출력(요청 갯수만큼 지원 금액에 따라 내림차순)", nickname = "지자체 협약 지원 정보 특정 지역 출력(요청 갯수만큼 지원 금액에 따라 내림차순)")
	@GetMapping("/convention/support/region-by-amount/{regionCount}")
	public ConventionSupportResponse searchConventionSupportRegionByAmount(HttpServletRequest request, HttpServletResponse response,
			@ApiParam(name = "regionCount", value = "요청 지자체 수", example = "5", required = true) 
			@PathVariable(value = "regionCount", required = true) @NotEmpty Integer regionCount){

		//1. 지자체 협약 지원 정보 특정 지역 출력(요청 갯수만큼 지원 금액에 따라 내림차순)
    	List<ConventionSupportInfoVO> conventionSupportInfoList = conventionSupportService.searchConventionSupportRegionByAmount(regionCount);
	
		//2. 결과 값 응답
		return ConventionSupportResponse.builder()
					.total(conventionSupportInfoList.size())
					.conventionSupportInfoList(conventionSupportInfoList)
					.build();
	}
    
    /**
     * 지자체 협약 지원 정보 특정 지역 출력(보전비율이 가장 작은 추천 기관명)
     * @param request
     * @param response
     * @param regionCount 요청 지자체 수
     * @return {@link ConventionSupportResponse} 지자체 협약 지원 정보 응답 객체
     */
    @ApiOperation(value = "지자체 협약 지원 정보 특정 지역 출력(보전비율이 가장 작은 추천 기관명)", nickname = "지자체 협약 지원 정보 특정 지역 출력(보전비율이 가장 작은 추천 기관명)")
	@GetMapping("/convention/support/region-by-rate")
	public ConventionSupportResponse searchConventionSupportRegionByRate(HttpServletRequest request, HttpServletResponse response){

		//1. 지자체 협약 지원 정보 특정 지역 출력(보전비율이 가장 작은 추천 기관명)
    	List<ConventionSupportInfoVO> conventionSupportInfoList = conventionSupportService.searchConventionSupportRegionByRate();
	
		//2. 결과 값 응답
		return ConventionSupportResponse.builder()
					.regionName(conventionSupportInfoList.get(0).getRegionName())
					.target(conventionSupportInfoList.get(0).getTarget())
					.usage(conventionSupportInfoList.get(0).getUsage())
					.limit(conventionSupportInfoList.get(0).getLimits())
					.rate(conventionSupportInfoList.get(0).getRate())
					.institute(conventionSupportInfoList.get(0).getRecommend())
					.mgmt(conventionSupportInfoList.get(0).getMgmt())
					.reception(conventionSupportInfoList.get(0).getReception())
					.build();
	}
    
}
