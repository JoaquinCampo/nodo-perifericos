package com.nodosperifericos.repository;

import com.nodosperifericos.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.clinic.id = :clinicId")
    Optional<User> findByEmailAndClinicId(@Param("email") String email, @Param("clinicId") String clinicId);
    
    List<User> findByClinicId(String clinicId);
    
    @Query("SELECT u FROM User u WHERE u.clinic.id = :clinicId AND (u.email = :email OR u.ci = :ci OR (:phone IS NOT NULL AND u.phone = :phone))")
    Optional<User> findByClinicIdAndEmailOrCiOrPhone(
        @Param("clinicId") String clinicId,
        @Param("email") String email,
        @Param("ci") String ci,
        @Param("phone") String phone
    );
}

