package com.convention.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 협약 지원 정보 DT
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConventionSupportInfoDT {

	/** 지자체 코드(기관 코드) */
	private String regionCode;
	
	/** 지자체명(기관명) */
	private String regionName;

	/** 지원대상 */
	private String target;
	
	/** 용도 */
	private String usage;
	
	/** 지원한도 */
	private String limit;
	
	/** 이차보전 */
	private String rate;
	
	/** 추천기관 */
	private String institute;
	
	/** 관리점 */
	private String mgmt;
	
	/** 취급점 */
	private String reception;
	
	public ConventionSupportInfo toEntity(String regionCode) {
		return ConventionSupportInfo.builder()
					.regionCode(regionCode != null ? regionCode : this.regionName)
					.target(this.target)
					.usage(this.usage)
					.limits(this.limit)
					.rate(this.rate)
					.recommend(this.institute)
					.mgmt(this.mgmt)
					.reception(this.reception)
					.build();
	}
}
