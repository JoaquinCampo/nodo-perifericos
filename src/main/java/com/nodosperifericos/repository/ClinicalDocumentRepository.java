package com.nodosperifericos.repository;

import com.nodosperifericos.domain.ClinicalDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalDocumentRepository extends JpaRepository<ClinicalDocument, String> {
    
    @Query("SELECT cd FROM ClinicalDocument cd " +
           "LEFT JOIN FETCH cd.clinic " +
           "LEFT JOIN FETCH cd.healthWorker hw " +
           "LEFT JOIN FETCH hw.user " +
           "WHERE cd.healthUserCi = :healthUserCi")
    List<ClinicalDocument> findByHealthUserCi(@Param("healthUserCi") String healthUserCi);
    
    List<ClinicalDocument> findByClinicId(String clinicId);
    
    List<ClinicalDocument> findByHealthWorkerId(String healthWorkerId);
}

