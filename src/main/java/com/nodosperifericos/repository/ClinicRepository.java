package com.nodosperifericos.repository;

import com.nodosperifericos.domain.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, String> {
    Optional<Clinic> findByName(String name);
}

