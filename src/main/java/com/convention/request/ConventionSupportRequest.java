package com.convention.request;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 협약 지원 서비스 요청 객체
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@ApiModel(value="ConventionSupportRequest", description="협약 지원 서비스 요청 객체")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConventionSupportRequest {
	
	/** 지원대상 */
	@Length(max=200)
	@ApiModelProperty(notes="지원대상", example="강릉시 소재 중소기업으로서 강릉시장이 추천한 자", required=false, position=20)
	private String target;
	
	/** 용도 */
	@Length(max=50)
	@ApiModelProperty(notes="용도", example="운전", required=false, position=30)
	private String usage;
	
	/** 지원한도 */
	@Length(max=50)
	@ApiModelProperty(notes="지원한도", example="추천금액 이내", required=false, position=40)
	private String limit;
	
	/** 이차보전 */
	@Length(max=10)
	@ApiModelProperty(notes="이차보전", example="3.00%", required=false, position=50)
	private String rate;
	
	/** 추천기관 */
	@Length(max=100)
	@ApiModelProperty(notes="추천기관", example="강릉시", required=false, position=60)
	private String institute;
	
	/** 관리점 */
	@Length(max=100)
	@ApiModelProperty(notes="관리점", example="강릉지점", required=false, position=70)
	private String mgmt;
	
	/** 취급점 */
	@Length(max=100)
	@ApiModelProperty(notes="취급점", example="강릉시 소재 영업점", required=false, position=80)
	private String reception;
}