insert into taxidb.order (price, client_id, driver_id)
values (
        :#${exchangeProperty.price}
           ,
        :#${exchangeProperty.clientId}
        ,
        :#${exchangeProperty.driverId}
       );