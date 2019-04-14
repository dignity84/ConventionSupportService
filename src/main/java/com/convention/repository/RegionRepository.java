package com.convention.repository;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Repository
public interface RegionRepository extends JpaRepository<RegionInfo, Long>{

	/** 지자체 코드로 지자체 정보 조회하기 */
	RegionInfo findByRegionCode(@NotEmpty String regionCode);
	/** 지자체명으로 지자체 정보 조회하기 */
	RegionInfo findByRegionName(@NotEmpty String regionName);
}
