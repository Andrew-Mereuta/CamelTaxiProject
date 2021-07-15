INSERT INTO client ( email, name, password, role)
VALUES (
        :#${exchangeProperty.email}
        ,
        :#${exchangeProperty.name}
        ,
        :#${exchangeProperty.password}
        ,
        'ROLE_CLIENT'
       );