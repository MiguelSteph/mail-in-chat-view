package com.mailchatview.backend.repo;

import com.mailchatview.backend.entities.Jwtsecret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtsecretRepo extends JpaRepository<Jwtsecret, Integer> {
    Optional<Jwtsecret> findBySecretName(String name);
}
