package com.nodosperifericos.repository;

import com.nodosperifericos.domain.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, String> {
    Optional<Speciality> findByNameIgnoreCase(String name);
}

