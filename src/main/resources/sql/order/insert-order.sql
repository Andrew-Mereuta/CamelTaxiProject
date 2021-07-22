insert into orders (price, client_id, driver_id)
values (
        :#${exchangeProperty.price}
           ,
        :#${exchangeProperty.clientId}
        ,
        :#${exchangeProperty.driverId}
       );