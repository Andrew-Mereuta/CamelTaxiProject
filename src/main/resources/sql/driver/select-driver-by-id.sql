SELECT  driver.email as email,
        driver.name as name,
        driver.role as role,
        driver.is_busy as is_busy
FROM driver
WHERE driver_id = :#${exchangeProperty.driverId}
;