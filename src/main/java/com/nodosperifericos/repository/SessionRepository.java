package com.nodosperifericos.repository;

import com.nodosperifericos.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findBySessionToken(String sessionToken);
    
    void deleteBySessionToken(String sessionToken);
}

