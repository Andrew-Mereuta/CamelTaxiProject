UPDATE client c SET c.name = :#${exchangeProperty.name}
                    ,c.password = :#${exchangeProperty.password}
                    WHERE
                    c.client_id = :#${exchangeProperty.clientId}
                    ;