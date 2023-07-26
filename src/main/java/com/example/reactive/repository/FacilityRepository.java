package com.example.reactive.repository;

import com.example.reactive.domain.entities.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByIsActive(boolean isActive);

    List<Facility> findByIdAndIsActive(Long id, boolean isActive);
}
