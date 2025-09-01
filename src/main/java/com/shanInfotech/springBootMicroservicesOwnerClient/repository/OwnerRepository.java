package com.shanInfotech.springBootMicroservicesOwnerClient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shanInfotech.springBootMicroservicesOwnerClient.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner,Integer>{

}
