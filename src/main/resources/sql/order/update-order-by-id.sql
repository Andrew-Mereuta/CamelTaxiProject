UPDATE taxidb.order o
SET o.driver_id = :#${exchangeProperty.driverId}
    ,
    o.client_id = :#${exchangeProperty.clientId}
    ,
    o.price = :#${exchangeProperty.price}
WHERE
    o.order_id = :#${exchangeProperty.orderId}
;