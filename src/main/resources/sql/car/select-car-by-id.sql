SELECT car.model as model,
       driver.name as 'driver-name',
       driver.email as 'driver-email'
       FROM car, driver
       WHERE car.driver_id = driver.driver_id AND car.id = :#${exchangeProperty.carId}
       ;