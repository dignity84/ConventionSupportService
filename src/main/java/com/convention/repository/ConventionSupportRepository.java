package com.convention.repository;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 협약 지원 repository
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Repository
public interface ConventionSupportRepository extends JpaRepository<ConventionSupportInfo, Long>{
	
	/** 지자체 코드로 지자체 협약 지원 정보 조회하기 */
	ConventionSupportInfo findByRegionCode(@NotEmpty String regionCode);
	
	@Query(
			value = "SELECT ROWNUM, A.* FROM (\n" + 
					"SELECT * FROM CONVENTION_SUPPORT_INFO order by limits, rate asc\n" + 
					")A WHERE ROWNUM <= :regionCount ",
			nativeQuery = true
			)
	List<ConventionSupportInfo> findByCount(@NotEmpty @Param("regionCount") Integer regionCount);
	
}
