UPDATE orders o
SET o.driver_id = :#${exchangeProperty.driverId}
WHERE
    o.order_id = :#${exchangeProperty.orderId}
;