package com.verush.brewery.repositories.security;

import com.verush.brewery.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: Bello Adamou - 2024.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
