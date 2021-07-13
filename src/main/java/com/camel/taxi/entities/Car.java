package com.camel.taxi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;

//    @OneToOne
//    @JoinColumn(name = "driver_id")
//    private Driver driver;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Car car = (Car) o;
//        return Objects.equals(id, car.id)
//                && Objects.equals(model, car.model)
//                && Objects.equals(driver.getId(), car.driver.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, model, driver);
//    }
}