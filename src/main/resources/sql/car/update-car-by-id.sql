update car c set c.model = :#${exchangeProperty.model}
               , c.driver_id = (select driver_id from driver where driver.email = :#${exchangeProperty.driverEmail}
                                                                   )
where c.id = :#${exchangeProperty.carId}
      ;