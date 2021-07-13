package com.camel.taxi.repositories;

import com.camel.taxi.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Optional<Car> findByDriver_Id(Long driverId);

    List<Car> findAllByModel(String model);

    @Modifying
    @Transactional
    @Query("update Car c set c.model = ?1 where c.id = ?2")
    void updateModel(String model, Long carId);
}