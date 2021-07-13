package com.camel.taxi.repositories;

import com.camel.taxi.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Driver findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Driver d set d.name = ?1, d.password = ?2 where d.id = ?3")
    void updateDriver(String name, String password, Long driverId);
}