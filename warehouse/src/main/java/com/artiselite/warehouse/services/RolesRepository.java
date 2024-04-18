package com.artiselite.warehouse.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artiselite.warehouse.models.Role;

public interface RolesRepository extends JpaRepository<Role, Integer>{

}
