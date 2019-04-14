package com.convention.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 지자체코드 테이블 엔티티
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionInfo {
	
//	/** 고유 식별 ID(순번) */
//	@Id
//	@GeneratedValue
//	@JsonIgnore
//	private Long id;
	
	/** 지자체코드(기관코드) */
	/** 고유 식별 ID(순번) */
	@Id
	@GeneratedValue
	private String regionCode;
	
	/** 지자체코드(기관코드) */
	@Column(name = "region_name", length = 20, nullable = false)
	private String regionName;
}
