UPDATE driver d SET d.name = :#${exchangeProperty.name}
                    ,
                    d.password = :#${exchangeProperty.password}
                    WHERE
    d.driver_id = :#${exchangeProperty.driverId}
;