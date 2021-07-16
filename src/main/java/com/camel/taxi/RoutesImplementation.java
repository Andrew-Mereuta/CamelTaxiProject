package com.camel.taxi;

import com.datasonnet.document.MediaTypes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.Processor;
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

        from(direct("get-orders-orderId"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-orders-orderId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("put-orders-orderId"))
            .setBody(DatasonnetExpression.builder("{opId: 'put-orders-orderId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("delete-orders-orderId"))
            .setBody(DatasonnetExpression.builder("{opId: 'delete-orders-orderId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("patch-orders-orderId"))
            .setBody(DatasonnetExpression.builder("{opId: 'patch-orders-orderId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        // IMPLEMENTED
        from(direct("get-drivers-driverId"))
            .routeId("direct:get-drivers-driverId")
            .to(direct("get-driver-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("put-drivers-driverId"))
            .routeId("direct:put-drivers-driverId")
            .to(direct("update-driver-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("delete-drivers-driverId"))
            .routeId("direct:delete-drivers-driverId")
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
            .to(direct("put-client-by-id").getUri())
        ;
        // IMPLEMENTED
        from(direct("delete-clients-clientId"))
            .routeId("direct:delete-clients-clientId")
            .to(direct("delete-client-by-id").getUri())
        ;
        // TODO: implement
        from(direct("get-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        // TODO: implement
        from(direct("put-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'put-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        // TODO: implement
        from(direct("delete-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'delete-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        // TODO: implement
        from(direct("patch-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'patch-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
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
        from(direct("get-orders"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-orders'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("post-orders"))
            .setBody(DatasonnetExpression.builder("{opId: 'post-orders'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
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
        from(direct("get-orders-drivers-driverId"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-orders-drivers-driverId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("get-orders-clients-clientId"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-orders-clients-clientId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        // IMPLEMENTED
        from(direct("get-clients"))
                .routeId("direct:get-clients")
                .to(direct("retrieve-all-clients").getUri())
        ;
        // TODO: implement
        from(direct("get-cars"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-cars'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;

    }
}
