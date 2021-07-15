INSERT INTO car (model, driver_id)
# VALUES (
#         'MODEL',
#         1
#        );
VALUES (
        :#${exchangeProperty.model},
        :#${exchangeProperty.driverId},
       );
