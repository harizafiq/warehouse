package com.artiselite.warehouse.services;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artiselite.warehouse.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	Optional<UserEntity> findByUsername(String username);
}
