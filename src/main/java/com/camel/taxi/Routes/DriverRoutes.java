package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.datasonnet.document.MediaTypes;
import com.ms3_inc.tavros.extensions.rest.exception.BadRequestException;
import com.ms3_inc.tavros.extensions.rest.exception.NotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DriverRoutes extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        from(direct("get-driver-by-id"))
            .routeId("direct:get-driver-by-id")
            .setProperty("driverId", simple("${header.driverId}"))
            .to(sql("classpath:/sql/driver/select-driver-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .transform(datasonnetEx("resource:classpath:driver.ds", String.class)
                    .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        from(direct("update-driver-by-id"))
            .routeId("direct:update-driver-by-id")
            .transform(datasonnetEx("resource:classpath:put-driver.ds", String.class))
            .setProperty("driverId", simple("${header.driverId}"))
            .setProperty("name", datasonnetEx("payload.name", String.class))
            .setProperty("password", datasonnetEx("payload.password", String.class))
            .to(sql("classpath:/sql/driver/select-driver-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
                .when(PredicateBuilder.or(exchangeProperty("name").isNull(),  exchangeProperty("password").isNull()))
                    .throwException(new BadRequestException(msg.BAD_REQUEST_ERROR))
            .otherwise()
                .to(sql("classpath:/sql/driver/update-driver-by-id.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setBody(constant(StringUtils.EMPTY))
        ;

        from(direct("delete-driver-by-id"))
            .routeId("direct:delete-driver-by-id")
            .setProperty("driverId", simple("${header.driverId}"))
            .to(sql("classpath:/sql/driver/select-driver-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .to(sql("classpath:/sql/car/delete-car-by-driverId.sql"))
                .to(sql("classpath:/sql/driver/delete-driver-by-id.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setBody(constant(StringUtils.EMPTY))
        ;

        from(direct("get-all-drivers"))
            .routeId("direct:get-all-drivers")
            .to(sql("classpath:/sql/driver/select-all-drivers.sql"))
            .transform(datasonnetEx("resource:classpath:drivers.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;
    }
}
