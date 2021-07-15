SELECT client.email as email,
       client.name as name,
       client.role as role
FROM client WHERE client_id = :#${exchangeProperty.clientId}
;
