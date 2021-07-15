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

        // TODO: Replace stubs for each endpoint with real implementation. Implementation defaults to a simple response with operation Id.
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
        from(direct("get-drivers-driverId"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-drivers-driverId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("put-drivers-driverId"))
            .setBody(DatasonnetExpression.builder("{opId: 'put-drivers-driverId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("delete-drivers-driverId"))
            .setBody(DatasonnetExpression.builder("{opId: 'delete-drivers-driverId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("get-clients-clientId"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-clients-clientId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("put-clients-clientId"))
            .setBody(DatasonnetExpression.builder("{opId: 'put-clients-clientId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("delete-clients-clientId"))
            .setBody(DatasonnetExpression.builder("{opId: 'delete-clients-clientId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("get-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("put-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'put-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("delete-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'delete-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("patch-cars-carId"))
            .setBody(DatasonnetExpression.builder("{opId: 'patch-cars-carId'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;
        from(direct("post-register"))
                .routeId("direct:post-register")
                .choice()
                    .when(simple("${header.who} == 'driver'"))
                        .to(direct("register-driver").getUri())
                    .otherwise()
                        .to(direct("register-client").getUri());


//            .log(LoggingLevel.INFO, "Start of ${routeId}")
//            .

//            .setBody(DatasonnetExpression.builder("{opId: 'post-register'}", String.class)
//                    .outputMediaType(MediaTypes.APPLICATION_JSON))
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
        from(direct("get-drivers"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-drivers'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
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
        // testing endpoint
        from(direct("get-clients"))
                .routeId("direct:get-clients")
                .to(sql("classpath:select-clients.sql"))
                .transform(datasonnet("payload", String.class))
        ;
        from(direct("get-cars"))
            .setBody(DatasonnetExpression.builder("{opId: 'get-cars'}", String.class)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
        ;

    }
}
