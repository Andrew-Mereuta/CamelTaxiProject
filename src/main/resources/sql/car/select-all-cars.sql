SELECT car.model as model,
       driver.email as 'driver-email',
       driver.name as 'driver-name' FROM car, driver WHERE car.driver_id = driver.driver_id;