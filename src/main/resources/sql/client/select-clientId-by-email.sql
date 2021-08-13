SELECT client.client_id AS clientId FROM client WHERE client.email = :#${exchangeProperty.clientEmail}
;