package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.datasonnet.document.MediaTypes;
import com.ms3_inc.tavros.extensions.rest.exception.NotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OrderRoutes extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        from(direct("create-order"))
            .routeId("direct:create-order")
            .setProperty("clientId", simple("${header.clientId}"))
            .setProperty("driverId", simple("${header.driverId}"))
            .setProperty("price", simple("${random(50, 101)}"))
            .to(sql("classpath:/sql/client/select-client-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .to(sql("classpath:/sql/driver/select-driver-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .setHeader("CamelSqlRetrieveGeneratedKeys", constant(true))
            .to(sql("classpath:/sql/order/insert-order.sql"))
            .setProperty("orderId", simple("${headers.CamelSqlGeneratedKeyRows[0]['GENERATED_KEY']}"))
            .to(sql("classpath:/sql/order/select-order-by-id.sql"))
            .transform(datasonnetEx("resource:classpath:order.ds", String.class)
                    .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
        ;

        from(direct("get-all-orders"))
            .routeId("direct:get-all-orders")
            .to(sql("classpath:/sql/order/select-all-orders.sql"))
            .transform(datasonnetEx("resource:classpath:orders.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
        ;

        from(direct("get-orders-by-id"))
            .routeId("direct:get-orders-by-id")
            .setProperty("orderId", simple("${header.orderId}"))
            .to(sql("classpath:/sql/order/select-order-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .transform(datasonnetEx("resource:classpath:orders.ds", String.class)
                    .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
        ;

        from(direct("get-orders-by-driverId"))
            .routeId("direct:get-orders-by-driverId")
            .setProperty("driverId", simple("${header.driverId}"))
            .to(sql("classpath:/sql/driver/select-driver-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .to(sql("classpath:/sql/order/select-order-by-driverId.sql"))
                .transform(datasonnetEx("resource:classpath:orders.ds", String.class)
                    .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
        ;

        from(direct("get-orders-by-clientId"))
            .routeId("direct:get-orders-by-clientId")
            .setProperty("clientId", simple("${header.clientId}"))
            .to(sql("classpath:/sql/client/select-client-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .to(sql("classpath:/sql/order/select-order-by-clientId.sql"))
                .transform(datasonnetEx("resource:classpath:orders.ds", String.class)
                    .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
        ;

        from(direct("update-order-by-id"))
            .routeId("direct:update-order-by-id")
            .transform(datasonnetEx("resource:classpath:put-order.ds", String.class))
            .setProperty("orderId", simple("${header.orderId}"))
            .setProperty("clientEmail", datasonnetEx("payload.clientEmail", String.class))
            .setProperty("driverEmail", datasonnetEx("payload.driverEmail", String.class))
            .setProperty("price", datasonnetEx("payload.price", String.class))
            .to(sql("classpath:/sql/order/select-order-by-id-custom.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .setHeader("CamelSqlRetrieveGeneratedKeys", constant(true))
            .to(sql("classpath:/sql/client/select-clientId-by-email.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    Map<String, Object> properties = exchange.getProperties();
                    List<Object> objs = (ArrayList<Object>) exchange.getIn().getBody();
                    Map<String, Long> o = (Map<String, Long>) objs.get(0);
                    Long clientId = o.get("clientId");
                    properties.put("clientId", clientId);
                }
            })
            .to(sql("classpath:/sql/driver/select-driverId-by-email.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    Map<String, Object> properties = exchange.getProperties();
                    List<Object> objs = (ArrayList<Object>) exchange.getIn().getBody();
                    Map<String, Long> o = (Map<String, Long>) objs.get(0);
                    Long clientId = o.get("driverId");
                    properties.put("driverId", clientId);
                }
            })
            .to(sql("classpath:/sql/order/update-order-by-id.sql"))
            .to(sql("classpath:/sql/order/select-order-by-id.sql"))
            .transform(datasonnetEx("resource:classpath:orders.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        from(direct("delete-order-by-id"))
            .routeId("direct:delete-order-by-id")
            .setProperty("orderId", simple("${header.orderId}"))
            .to(sql("classpath:/sql/order/select-order-by-id-custom.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .to(sql("classpath:/sql/order/delete-order-by-id.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setBody(constant(StringUtils.EMPTY))
        ;

        from(direct("change-driver-of-order"))
            .routeId("direct:change-driver-of-order")
            .setProperty("orderId", simple("${header.orderId}"))
            .setProperty("driverId", simple("${header.newDriverId}"))
            .to(sql("classpath:/sql/order/select-order-by-id-custom.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .to(sql("classpath:/sql/driver/select-driver-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .to(sql("classpath:/sql/order/update-driver-of-order-by-id.sql"))
            .to(sql("classpath:/sql/order/select-order-by-id.sql"))
            .transform(datasonnetEx("resource:classpath:order.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;
    }
}
