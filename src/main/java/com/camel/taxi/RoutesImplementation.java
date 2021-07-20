package com.camel.taxi;

import com.datasonnet.document.MediaTypes;
import org.apache.camel.language.datasonnet.DatasonnetExpression;
import org.springframework.stereotype.Component;

/**
 * The RoutesImplementation class holds implementations for the end points configured in RoutesGenerated.
 * These routes are based on operation Ids, that correspond to an API end point:  method+path.
 *
 * @author Maven Archetype (camel-openapi-archetype)
 */
@Component
public class RoutesImplementation extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        // IMPLEMENTED
        from(direct("get-orders-orderId"))
            .routeId("direct:get-orders-orderId")
            .to(direct("get-orders-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("put-orders-orderId"))
            .routeId("direct:put-orders-orderId")
            .to(direct("update-order-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("delete-orders-orderId"))
            .routeId("direct:delete-orders-orderId")
            .to(direct("delete-order-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("patch-orders-orderId"))
            .routeId("direct:patch-orders-orderId")
            .to(direct("change-driver-of-order").getUri())
        ;
        // IMPLEMENTED
        from(direct("get-drivers-driverId"))
            .routeId("direct:get-drivers-driverId")
            .setProperty("driverId", simple("${header.driverId}"))
            .to(direct("get-driver-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("put-drivers-driverId"))
            .routeId("direct:put-drivers-driverId")
            .transform(datasonnetEx("resource:classpath:put-driver.ds", String.class))
            .setProperty("driverId", simple("${header.driverId}"))
            .setProperty("name", datasonnetEx("payload.name", String.class))
            .setProperty("password", datasonnetEx("payload.password", String.class))
            .to(direct("update-driver-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("delete-drivers-driverId"))
            .routeId("direct:delete-drivers-driverId")
            .setProperty("driverId", simple("${header.driverId}"))
            .to(direct("delete-driver-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("get-clients-clientId"))
            .routeId("direct:get-clients-clientId")
            .to(direct("get-client-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("put-clients-clientId"))
            .routeId("direct:put-clients-clientsId")
            .transform(datasonnetEx("resource:classpath:create-client-payload.ds", String.class))
            .setProperty("clientId", simple("${header.clientId}"))
            .setProperty("name", datasonnetEx("payload.name", String.class))
            .setProperty("password", datasonnetEx("payload.password", String.class))
            .to(direct("put-client-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("delete-clients-clientId"))
            .routeId("direct:delete-clients-clientId")
            .setProperty("clientId", simple("${header.clientId}"))
            .to(direct("delete-client-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("get-cars-carId"))
            .routeId("direct:get-cars-carId")
            .to(direct("get-car-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("put-cars-carId"))
            .routeId("direct:put-cars-carId")
            .to(direct("update-car-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("delete-cars-carId"))
            .routeId("direct:delete-cars-carId")
            .to(direct("delete-car-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("patch-cars-carId"))
            .routeId("direct:patch-cars-carId")
            .to(direct("patch-car-by-id"))
        ;
        // IMPLEMENTED
        from(direct("post-register"))
                .routeId("direct:post-register")
                .choice()
                    .when(simple("${header.who} == 'driver'"))
                        .to(direct("register-driver").getUri())
                    .otherwise()
                        .to(direct("register-client").getUri())
        ;
        from(direct("post-login"))
            .setBody(DatasonnetExpression.builder("{opId: 'post-login'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        // IMPLEMENTED
        from(direct("get-orders"))
            .routeId("direct:get-orders")
            .to(direct("get-all-orders").getUri())
        ;
        // IMPLEMENTED
        from(direct("post-orders"))
            .routeId("direct:post-orders")
            .to(direct("create-order").getUri())
        ;
        // IMPLEMENTED
        from(direct("get-drivers"))
            .routeId("direct:get-drivers")
            .to(direct("get-all-drivers").getUri())
        ;
        from(direct("get-test"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-test'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        // IMPLEMENTED
        from(direct("get-orders-drivers-driverId"))
            .routeId("direct:get-orders-drivers-driverId")
            .to(direct("get-orders-by-driverId"))
        ;
        // IMPLEMENTED
        from(direct("get-orders-clients-clientId"))
            .routeId("direct:get-orders-clients-clientId")
            .to(direct("get-orders-by-clientId"))
        ;
        // IMPLEMENTED
        from(direct("get-clients"))
                .routeId("direct:get-clients")
                .to(direct("retrieve-all-clients").getUri())
        ;
        // IMPLEMENTED
        from(direct("get-cars"))
            .routeId("direct:get-cars")
            .to(direct("get-all-cars").getUri())
        ;

    }
}
