update car c set c.model = :#${exchangeProperty.model}
where c.id = :#${exchangeProperty.carId}
;