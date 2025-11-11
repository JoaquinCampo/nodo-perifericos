package com.nodosperifericos.repository;

import com.nodosperifericos.domain.ClinicAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicAdminRepository extends JpaRepository<ClinicAdmin, String> {
    
    @Query("SELECT ca FROM ClinicAdmin ca " +
           "LEFT JOIN FETCH ca.user u " +
           "WHERE u.clinic.id = :clinicId")
    List<ClinicAdmin> findByClinicId(@Param("clinicId") String clinicId);
    
    Optional<ClinicAdmin> findByUserId(String userId);
}

