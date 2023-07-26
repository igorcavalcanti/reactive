package com.example.reactive.repository;

import com.example.reactive.domain.entities.Profession;
import com.example.reactive.domain.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    @Query(value = """
    select profession from "Worker" where id = :workerId
    """, nativeQuery = true)
    Profession findProfessionById(Long workerId);
}
