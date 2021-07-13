package com.camel.taxi;

import javax.annotation.Generated;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ms3_inc.tavros.extensions.rest.OpenApi4jValidator;

/**
 * Generated routes are based on the OpenAPI document in src/generated/api folder.
 *
 * @author Maven Archetype (camel-oas-archetype)
 */
@Generated("com.ms3_inc.camel.archetype.oas")
@Component
public class RoutesGenerated extends BaseRestRouteBuilder {
    private final String contextPath;

    public RoutesGenerated(@Value("${camel.rest.context-path}") String contextPath) {
        super();
        this.contextPath = contextPath;
    }

    /**
     * Defines Apache Camel routes using the OpenAPI REST DSL.
     * Routes are built using a get(PATH) rest message processor.
     *
     * Make changes to this file with caution.
     * If the API specification changes and this file is regenerated,
     * previous changes may be overwritten.
     */
    @Override
    public void configure() throws Exception {
        super.configure();

        restConfiguration().component("undertow");

        interceptFrom()
            .process(new OpenApi4jValidator("taxiOAS.yaml", contextPath));

        rest()
            .get("/orders/{orderId}")
                .id("get-orders-orderId")
                .produces("application/json")
                .to(direct("get-orders-orderId").getUri())
            .put("/orders/{orderId}")
                .id("put-orders-orderId")
                .consumes("application/json")
                .produces("application/json")
                .to(direct("put-orders-orderId").getUri())
            .delete("/orders/{orderId}")
                .id("delete-orders-orderId")
                .produces("application/json")
                .to(direct("delete-orders-orderId").getUri())
            .patch("/orders/{orderId}")
                .id("patch-orders-orderId")
                .produces("application/json")
                .to(direct("patch-orders-orderId").getUri())
            .get("/drivers/{driverId}")
                .id("get-drivers-driverId")
                .produces("application/json")
                .to(direct("get-drivers-driverId").getUri())
            .put("/drivers/{driverId}")
                .id("put-drivers-driverId")
                .consumes("application/json")
                .produces("application/json")
                .to(direct("put-drivers-driverId").getUri())
            .delete("/drivers/{driverId}")
                .id("delete-drivers-driverId")
                .to(direct("delete-drivers-driverId").getUri())
            .get("/clients/{clientId}")
                .id("get-clients-clientId")
                .produces("application/json")
                .to(direct("get-clients-clientId").getUri())
            .put("/clients/{clientId}")
                .id("put-clients-clientId")
                .consumes("application/json")
                .produces("*/*")
                .to(direct("put-clients-clientId").getUri())
            .delete("/clients/{clientId}")
                .id("delete-clients-clientId")
                .to(direct("delete-clients-clientId").getUri())
            .get("/cars/{carId}")
                .id("get-cars-carId")
                .produces("application/json")
                .to(direct("get-cars-carId").getUri())
            .put("/cars/{carId}")
                .id("put-cars-carId")
                .consumes("application/json")
                .produces("application/json")
                .to(direct("put-cars-carId").getUri())
            .delete("/cars/{carId}")
                .id("delete-cars-carId")
                .to(direct("delete-cars-carId").getUri())
            .patch("/cars/{carId}")
                .id("patch-cars-carId")
                .produces("application/json")
                .to(direct("patch-cars-carId").getUri())
            .post("/register")
                .id("post-register")
                .consumes("application/json")
                .to(direct("post-register").getUri())
            .post("/login")
                .id("post-login")
                .consumes("application:/json")
                .to(direct("post-login").getUri())
            .get("/orders")
                .id("get-orders")
                .produces("application/json")
                .to(direct("get-orders").getUri())
            .post("/orders")
                .id("post-orders")
                .produces("application/json")
                .to(direct("post-orders").getUri())
            .get("/drivers")
                .id("get-drivers")
                .produces("application/json")
                .to(direct("get-drivers").getUri())
            .get("/test")
                .id("get-test")
                .produces("*/*")
                .to(direct("get-test").getUri())
            .get("/orders/drivers/{driverId}")
                .id("get-orders-drivers-driverId")
                .produces("application/json")
                .to(direct("get-orders-drivers-driverId").getUri())
            .get("/orders/clients/{clientId}")
                .id("get-orders-clients-clientId")
                .produces("application/json")
                .to(direct("get-orders-clients-clientId").getUri())
            .get("/clients")
                .id("get-clients")
                .produces("*/*")
                .to(direct("get-clients").getUri())
            .get("/cars")
                .id("get-cars")
                .produces("application/json")
                .to(direct("get-cars").getUri())
        ;
    }
}
