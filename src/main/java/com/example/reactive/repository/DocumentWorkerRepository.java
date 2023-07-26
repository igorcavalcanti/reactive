package com.example.reactive.repository;

import com.example.reactive.domain.entities.DocumentWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DocumentWorkerRepository extends JpaRepository<DocumentWorker, Long> {

    @Query(value = """
            SELECT DISTINCT dw.document_id FROM "DocumentWorker" dw INNER JOIN "Document" d ON d.id = dw.document_id WHERE dw .worker_id = :workerId AND d.is_active = true
            """,
            nativeQuery = true)
    List<Long> findDocumentIdByWorkerId(Long workerId);
}
