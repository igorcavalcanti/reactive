package com.example.reactive.repository;

import com.example.reactive.domain.entities.FacilityRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FacilityRequirementRepository extends JpaRepository<FacilityRequirement, Long> {


    @Query(value = """
            SELECT DISTINCT fr.document_id FROM "FacilityRequirement" fr INNER JOIN "Document" d ON d.id = fr.document_id WHERE fr.facility_id = :facilityId AND d.is_active = true
            """,
            nativeQuery = true)
    List<Long> findDocumentsIdByFacilityId(Long facilityId);
}
