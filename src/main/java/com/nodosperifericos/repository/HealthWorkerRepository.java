package com.nodosperifericos.repository;

import com.nodosperifericos.domain.HealthWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthWorkerRepository extends JpaRepository<HealthWorker, String> {
    
    @Query("SELECT hw FROM HealthWorker hw " +
           "LEFT JOIN FETCH hw.user u " +
           "LEFT JOIN FETCH hw.healthWorkerSpecialities hws " +
           "LEFT JOIN FETCH hws.speciality " +
           "WHERE u.clinic.id = :clinicId")
    List<HealthWorker> findByUserClinicId(@Param("clinicId") String clinicId);
    
    @Query("SELECT hw FROM HealthWorker hw " +
           "LEFT JOIN FETCH hw.user u " +
           "LEFT JOIN FETCH u.clinic c " +
           "WHERE u.ci = :ci AND c.name = :clinicName")
    Optional<HealthWorker> findByCiAndClinicName(@Param("ci") String ci, @Param("clinicName") String clinicName);
    
    Optional<HealthWorker> findByUserId(String userId);
}

