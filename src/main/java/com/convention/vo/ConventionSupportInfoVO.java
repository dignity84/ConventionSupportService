package com.convention.vo;

import org.hibernate.validator.constraints.Length;

import com.convention.repository.ConventionSupportInfoDT;
import com.convention.repository.ConventionSupportInfoDT.ConventionSupportInfoDTBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ConventionSupportInfoVO {	

	/** 지자체 코드(기관코드) */
	@Length(max=10)
	@ApiModelProperty(notes="지자체 코드(기관코드)", example="210", position=1)
	private String regionCode;
	
	/** 지자체명(기관명) */
	@Length(max=10)
	@ApiModelProperty(notes="지자체명(기관명)", example="강릉시", position=10)
	private String regionName;

	/** 지원대상 */
	@Length(max=200)
	@ApiModelProperty(notes="지원대상", example="강릉시 소재 중소기업으로서 강릉시장이 추천한 자", position=20)
	private String target;
	
	/** 용도 */
	@Length(max=50)
	@ApiModelProperty(notes="용도", example="운전", position=30)
	private String usage;
	
	/** 지원한도 */
	@Length(max=50)
	@ApiModelProperty(notes="지원한도", example="추천금액 이내", position=40)
	private String limits;
	
	/** 이차보전 */
	@Length(max=10)
	@ApiModelProperty(notes="이차보전", example="3.00%", position=50)
	private String rate;
	
	/** 추천기관 */
	@Length(max=100)
	@ApiModelProperty(notes="추천기관", example="강릉시", position=60)
	private String recommend;
	
	/** 관리점 */
	@Length(max=100)
	@ApiModelProperty(notes="관리점", example="강릉지점", position=70)
	private String mgmt;
	
	/** 취급점 */
	@Length(max=100)
	@ApiModelProperty(notes="취급점", example="강릉시 소재 영업점", position=80)
	private String reception;

}
