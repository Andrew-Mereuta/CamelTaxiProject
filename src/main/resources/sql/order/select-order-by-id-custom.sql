select o.order_id as id,
       o.price as price,
       o.driver_id as driverId,
       o.client_id as clientId
from orders as o
where o.order_id = :#${exchangeProperty.orderId}
;