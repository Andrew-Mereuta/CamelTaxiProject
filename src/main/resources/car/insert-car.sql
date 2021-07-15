INSERT INTO car (model, driver_id)
VALUES (
        :#${exchangeProperty.model}
        ,
        :#${exchangeProperty.driverId}
       );
