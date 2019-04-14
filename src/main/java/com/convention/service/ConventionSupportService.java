package com.convention.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.convention.repository.ConventionSupportInfo;
import com.convention.repository.ConventionSupportInfoDT;
import com.convention.repository.ConventionSupportRepository;
import com.convention.repository.RegionRepository;
import com.convention.vo.ConventionSupportInfoVO;

/**
 * 지자체 협약지원 서비스 서비스
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Service
public class ConventionSupportService {
	
	@Autowired private FileStorageService fileStorageService;
	@Autowired ConventionSupportRepository conventionSupportRepository;
	@Autowired RegionRepository regionRepository;

	/**
	 * 지자체 협약 지원 정보 데이터 저장(파일업로드)
	 * 
	 * @param svcFile
	 * @return 저장된 총 갯수
	 */
	public List<ConventionSupportInfoVO> saveConventionSupportCSVFile(@NotEmpty MultipartFile csvFile) {
		
		List<List<String>> maybeConventionSupportInfoList = new ArrayList<List<String>>();
		
		String fileName = fileStorageService.storeFile(csvFile);
		
		try {			
			String line = null;
			@SuppressWarnings("resource")
			BufferedReader in = new BufferedReader(new FileReader(fileName, Charset.forName("EUC-KR")));
			while( (line = in.readLine()) != null) {
				maybeConventionSupportInfoList.add(Arrays.asList(line.split(",")));
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}				
		maybeConventionSupportInfoList.remove(0);
		return maybeConventionSupportInfoList.stream()
							.map(maybeConventionSupportInfo -> {
								return this.saveConventionSupportInfo(ConventionSupportInfoDT.builder()
										.regionCode(null)
										.regionName(maybeConventionSupportInfo.get(1).replaceAll("[\"]", "").replace("/", ", "))
										.target(maybeConventionSupportInfo.get(2).replaceAll("[\"]", "").replace("/", ", "))
										.usage(maybeConventionSupportInfo.get(3).replaceAll("[\"]", "").replace("/", ", "))
										.limit(maybeConventionSupportInfo.get(4).replaceAll("[\"]", "").replace("/", ", "))
										.rate(maybeConventionSupportInfo.get(5).replaceAll("[\"]", "").replace("/", ", "))
										.institute(maybeConventionSupportInfo.get(6).replaceAll("[\"]", "").replace("/", ", "))
										.mgmt(maybeConventionSupportInfo.get(7).replaceAll("[\"]", "").replace("/", ", "))
										.reception(maybeConventionSupportInfo.get(8).replaceAll("[\"]", "").replace("/", ", "))
										.build());				
							})
							.collect(Collectors.toList());
	}
	
	/**
	 * 지자체 협약 지원 정보 데이터 생성
	 * @param conventionSupportDt 저장될 지자체 협약 지원 정보
	 * @return 생성된 지자체 협약 지원 정보 
	 */
	public ConventionSupportInfoVO saveConventionSupportInfo(ConventionSupportInfoDT conventionSupportDt) {
		// 지자체 코드로 지자체 정보 조회하기
		String regionCode = Optional.ofNullable(regionRepository.findByRegionName(conventionSupportDt.getRegionName()).getRegionCode())
								.orElseThrow();
		ConventionSupportInfo conventionSupportInfo =  Optional.ofNullable(conventionSupportRepository.save(conventionSupportDt.toEntity(regionCode)))
															.orElseThrow();
		
		ConventionSupportInfoVO conventionSupportInfoVO = ConventionSupportInfoVO.builder()
																.regionName(conventionSupportDt.getRegionName())
																.build();
		BeanUtils.copyProperties(conventionSupportInfo, conventionSupportInfoVO);
		conventionSupportInfoVO.setRegionCode(null);
		
		return conventionSupportInfoVO;
		
	}

	/**
	 * 지자체 협약 지원 정보 리스트 조회
	 * @return 지자체 협약 지원 정보 리스트
	 */
	public List<ConventionSupportInfoVO> searchConventionSupportList() {
		List<ConventionSupportInfo> conventionSupportInfoList = Optional.ofNullable(conventionSupportRepository.findAll())
																.orElseThrow();
		
		return conventionSupportInfoList.stream()
					.map(conventionSupportInfo -> {						
						ConventionSupportInfoVO conventionSupportInfoVO = ConventionSupportInfoVO.builder()
								.regionName(regionRepository.findByRegionCode(conventionSupportInfo.getRegionCode()).getRegionName())
								.build();

						BeanUtils.copyProperties(conventionSupportInfo, conventionSupportInfoVO);
						conventionSupportInfoVO.setRegionCode(null);
						
						return conventionSupportInfoVO;
					})
					.collect(Collectors.toList());
	}

	/**
	 * 지자체 협약 지원 정보 조회
	 * @param region 지자체명
	 * @return 지자체명에 따른 지자체 협약 지원 정보
	 */
	public ConventionSupportInfoVO searchConventionSupport(@NotEmpty String regionName) {
		String regionCode = Optional.ofNullable(regionRepository.findByRegionName(regionName).getRegionCode())
								.orElseThrow();

		ConventionSupportInfoVO conventionSupportInfoVO = ConventionSupportInfoVO.builder()
				.regionName(regionName)
				.build();
		
		BeanUtils.copyProperties(conventionSupportRepository.findByRegionCode(regionCode), conventionSupportInfoVO);
		conventionSupportInfoVO.setRegionCode(null);
		
		return conventionSupportInfoVO;
	}

	/**
	 * 지자체 협약 지원 정보 데이터 수정
	 * @param conventionSupportDt 수정될 지자체 협약 지원 정보
	 * @return 생성된 지자체 협약 지원 정보 
	 */
	public ConventionSupportInfoVO modifyConventionSupport(ConventionSupportInfoDT conventionSupportDt) {
		String regionCode = Optional.ofNullable(regionRepository.findByRegionName(conventionSupportDt.getRegionName()).getRegionCode())
				.orElseThrow();
		ConventionSupportInfo conventionSupportInfo = conventionSupportRepository.findByRegionCode(regionCode);
		
		//지자체명 셋팅
		conventionSupportInfo.setRegionCode(regionCode);
		//업데이트 요청 제한한도가 null 인 경우 기존 데이터로 셋팅
		conventionSupportInfo.setLimits(conventionSupportDt.getLimit() != null ? conventionSupportDt.getLimit() : conventionSupportInfo.getLimits());
		//업데이트 요청 관리점이 null 인 경우 기존 데이터로 셋팅
		conventionSupportInfo.setMgmt(conventionSupportDt.getMgmt() != null ? conventionSupportDt.getMgmt() : conventionSupportInfo.getMgmt());
		//업데이트 요청 이차보전이 null 인 경우 기존 데이터로 셋팅
		conventionSupportInfo.setRate(conventionSupportDt.getRate() != null ? conventionSupportDt.getRate() : conventionSupportInfo.getRate());
		//업데이트 요청 취급점이 null 인 경우 기존 데이터로 셋팅
		conventionSupportInfo.setReception(conventionSupportDt.getReception() != null ? conventionSupportDt.getReception() : conventionSupportInfo.getReception());
		//업데이트 요청 추천기관이 null 인 경우 기존 데이터로 셋팅
		conventionSupportInfo.setRecommend(conventionSupportDt.getInstitute() != null ? conventionSupportDt.getInstitute() : conventionSupportInfo.getRecommend());
		//업데이트 요청 지원대상이 null 인 경우 기존 데이터로 셋팅
		conventionSupportInfo.setTarget(conventionSupportDt.getTarget() != null ? conventionSupportDt.getTarget() : conventionSupportInfo.getTarget());
		//업데이트 요청 용도가 null 인 경우 기존 데이터로 셋팅
		conventionSupportInfo.setUsage(conventionSupportDt.getUsage() != null ? conventionSupportDt.getUsage() : conventionSupportInfo.getUsage());
		
		
		ConventionSupportInfoVO conventionSupportInfoVO = ConventionSupportInfoVO.builder()
				.regionName(conventionSupportDt.getRegionName())
				.build();
		
		BeanUtils.copyProperties(conventionSupportRepository.saveAndFlush(conventionSupportInfo), conventionSupportInfoVO);
		conventionSupportInfoVO.setRegionCode(null);
		
		return conventionSupportInfoVO;					
	}

	/**
	 * 지자체 협약 지원 정보 특정 지역 출력(요청 갯수만큼 지원 금액에 따라 내림차순)
	 * @param regionCount 요청 지자체 수
	 * @return 요청 지자체 수 만큼의 특정 지역 리스트
	 */
	public List<ConventionSupportInfoVO> searchConventionSupportRegionByAmount(@NotEmpty Integer regionCount) {
		List<ConventionSupportInfo> conventionSupportInfoList = Optional.ofNullable(conventionSupportRepository.findAll())
				.orElseThrow();
		
		Map<String,Integer> unitMap = new HashMap<>();
		unitMap.put("십", 10);
		unitMap.put("백", 100);
		unitMap.put("천", 1000);
		unitMap.put("만", 10000);
		unitMap.put("억", 100000000);
		
		conventionSupportInfoList.stream()
//			.filter(x -> x.getLimits().matches(".*[0-9].*"))
			.forEach(conventionSupportInfo -> {
				//지원금액(DB(String 데이터) -> Integer)
				if(conventionSupportInfo.getLimits().matches(".*[0-9].*")) {					
					List<String> maybeLimitAmount = Arrays.asList(conventionSupportInfo.getLimits().replaceAll("[^0-9,십,백,천,만,억]", "").split(""));				
					Integer limitAmount = maybeLimitAmount.stream()
							.map(x -> {
								if(unitMap.containsKey(x))
									return unitMap.get(x);
								
								return Integer.parseInt(String.valueOf(x));
							})
							.reduce((x,y) -> x * y)
							.get();									
					conventionSupportInfo.setLimits(String.valueOf(limitAmount));
				}
				//이차보전 정보 평균(DB(String 데이터) -> Double 평균)
				if(conventionSupportInfo.getRate().matches(".*[0-9].*")) {
					Double rate;
					if(conventionSupportInfo.getRate().contains("~")) {
						List<String> maybeRate = Arrays.asList(conventionSupportInfo.getRate().replace("~", "").split("%"));
						rate = maybeRate.stream()
									.mapToDouble(x -> Double.parseDouble(x))
									.average()
									.getAsDouble();
					} else {
						rate = Double.parseDouble(conventionSupportInfo.getRate().replace("%", ""));
					}					
					conventionSupportInfo.setRate(String.valueOf(rate));
				}				
			});
		
		//지원금액 순으로 내림차순, 이차보전 평균 비율 오름차순(적은 순서->높은 순서로...)
		Comparator.reverseOrder();
		conventionSupportInfoList.sort(Comparator.comparing(ConventionSupportInfo::getLimits)
				.reversed().thenComparing(ConventionSupportInfo::getRate));

		//요청 지자체 수만큼 리턴
		return conventionSupportInfoList.stream()
					.map(conventionSupportInfo -> {
						ConventionSupportInfoVO conventionSupportInfoVO = new ConventionSupportInfoVO();
						BeanUtils.copyProperties(conventionSupportInfo, conventionSupportInfoVO);
						conventionSupportInfoVO.setRegionName(regionRepository.findByRegionCode(conventionSupportInfo.getRegionCode()).getRegionName());
						conventionSupportInfoVO.setRegionCode(null);
						
						return conventionSupportInfoVO;
					})
					.limit(regionCount)
					.collect(Collectors.toList());
	}

	/**
	 * 지자체 협약 지원 정보 특정 지역 출력(보전비율이 가장 작은 추천 기관명)
	 * @return 보전비율이 가장 작은 추천 기관명
	 */
	public List<ConventionSupportInfoVO> searchConventionSupportRegionByRate() {
		List<ConventionSupportInfo> conventionSupportInfoList = Optional.ofNullable(conventionSupportRepository.findAll())
				.orElseThrow();
		
		conventionSupportInfoList.stream()
			.forEach(conventionSupportInfo -> {
				//이차보전 정보(DB(String 데이터) -> Double)
				if(conventionSupportInfo.getRate().matches(".*[0-9].*")) {
					Double rate;
					if(conventionSupportInfo.getRate().contains("~")) {
						List<String> maybeRate = Arrays.asList(conventionSupportInfo.getRate().replace("~", "").split("%"));
						//구간으로 설정되어 있다면 제일 낮은 금액으로 설정
						rate = Double.parseDouble(maybeRate.get(0));
					} else {
						rate = Double.parseDouble(conventionSupportInfo.getRate().replace("%", ""));
					}					
					conventionSupportInfo.setRate(String.valueOf(rate));
				}
			});
		
		//이차보전 평균 비율 오름차순(적은 순서->높은 순서로...) 
		conventionSupportInfoList.sort(Comparator.comparing(ConventionSupportInfo::getLimits));
		
		//요청 지자체 수만큼 리턴
		return conventionSupportInfoList.stream()
					.map(conventionSupportInfo -> {
						ConventionSupportInfoVO conventionSupportInfoVO = new ConventionSupportInfoVO();
						BeanUtils.copyProperties(conventionSupportInfo, conventionSupportInfoVO);
						conventionSupportInfoVO.setRegionName(regionRepository.findByRegionCode(conventionSupportInfo.getRegionCode()).getRegionName());
						conventionSupportInfoVO.setRegionCode(null);
						
						return conventionSupportInfoVO;
					})
					.limit(1)
					.collect(Collectors.toList());
	}

}
