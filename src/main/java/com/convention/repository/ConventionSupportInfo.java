package com.convention.repository;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 협약 지원 정보 테이블 엔티티
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConventionSupportInfo {
	
	/** 고유 식별 ID(순번) */
	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;	
	
	/** 지자체코드(기관코드) */
	@Column(name = "region_code", length = 20, nullable = false)
	private String regionCode;

	/** 지원대상 */
	@Column(name = "target", length = 200, nullable = false)
	private String target;
	
	/** 용도 */
	@Column(name = "usage", length = 50, nullable = false)
	private String usage;
	
	/** 지원한도 */
	@Column(name = "limits", length = 50, nullable = false)
	private String limits;
	
	/** 이차보전 */
	@Column(name = "rate", length = 10, nullable = false)
	private String rate;
	
	/** 추천기관 */
	@Column(name = "recommend", length = 100, nullable = false)
	private String recommend;
	
	/** 관리점 */
	@Column(name = "mgmt", length = 100, nullable = false)
	private String mgmt;
	
	/** 취급점 */
	@Column(name = "reception", length = 100, nullable = false)
	private String reception;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_date", insertable = false)
	private Date modifyDate;
	
}
