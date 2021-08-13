select o.price as price,
       d.email as driverEmail,
       c.email as clientEmail,
       car.model as carModel
from orders as o,
     client as c,
     driver as d,
     car
where o.order_id = :#${exchangeProperty.orderId}
  and o.client_id = c.client_id
  and o.driver_id = d.driver_id
  and car.driver_id = d.driver_id
;