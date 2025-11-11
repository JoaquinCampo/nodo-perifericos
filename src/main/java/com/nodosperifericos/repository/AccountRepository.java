package com.nodosperifericos.repository;

import com.nodosperifericos.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByProviderAndProviderAccountId(String provider, String providerAccountId);
}

