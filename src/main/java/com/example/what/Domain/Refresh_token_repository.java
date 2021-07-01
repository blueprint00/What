package com.example.what.Domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Refresh_token_repository extends JpaRepository<Refresh_token, Long> {
    Optional<Refresh_token> findByKey(String refresh_key);
}
