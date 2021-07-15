INSERT INTO driver (email, is_busy, name, password, role)
VALUES (
           :#${exchangeProperty.email}
           ,
           FALSE,
           :#${exchangeProperty.name}
           ,
           :#${exchangeProperty.password}
           ,
           'ROLE_DRIVER'
       );
