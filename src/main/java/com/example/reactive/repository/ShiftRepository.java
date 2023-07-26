package com.example.reactive.repository;

import com.example.reactive.domain.entities.Shift;
import com.example.reactive.domain.projection.AvailableShiftsProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    @Query(value = """
        WITH cte_claimed_shift as (
        SELECT s.id, s."start", s."end"
        FROM "Shift" s
        WHERE s.worker_id = :workerId
        AND NOT s.is_deleted
        AND (s."start" BETWEEN :range_start AND :range_end OR s."end" BETWEEN :range_start AND :range_end)),
        
        cte_available_shift as (
        SELECT s.facility_id, s."start", s."end", s.id
        FROM "Shift" s
        WHERE s.worker_id is null
        AND s.facility_id IN (:facilityId)
        AND NOT s.is_deleted and s."profession" = :profession
        AND s."start" >= :range_start 
        AND s."end" <= :range_end),
        
        cte_shift_overlapping as (
        SELECT av.id FROM cte_available_shift av, cte_claimed_shift cs
        WHERE (av."start" > cs."start" and av."start" < cs."end") OR (av."end" > cs."start" AND  av."end" < cs."end")
        )
        
        SELECT av.facility_id as facilityId, av."start", av."end",  STRING_AGG(av.id||'', ',') as shifts 
        FROM cte_available_shift av 
        WHERE NOT EXISTS (SELECT 1 FROM cte_shift_overlapping cov where cov.id = av.id)
        GROUP BY av.facility_id, av."start", av."end"
        """, nativeQuery = true)
    List<AvailableShiftsProjection> findAvailableShiftsBy(@Param("workerId") Long workerId,
                                                          @Param("facilityId") List<Long> facilityId,
                                                          @Param("profession") String profession,
                                                          @Param("range_start") ZonedDateTime range_start,
                                                          @Param("range_end") ZonedDateTime range_end,
                                                          Pageable pageConfig);
}
