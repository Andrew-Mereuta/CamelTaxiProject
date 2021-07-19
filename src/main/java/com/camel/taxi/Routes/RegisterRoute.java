package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.camel.taxi.Messages.Messages;
import com.datasonnet.document.MediaType;
import com.datasonnet.document.MediaTypes;
import com.ms3_inc.tavros.extensions.rest.exception.BadRequestException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class RegisterRoute extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        from(direct("register-client"))
                .routeId("direct:register-client")
                .transform(datasonnetEx("resource:classpath:create-client-payload.ds", String.class))
                .setProperty("name", datasonnetEx("payload.name", String.class))
                .setProperty("email", datasonnetEx("payload.email", String.class))
                .setProperty("clientEmail", datasonnetEx("payload.email", String.class)) // stupid solution, but I am too lazy to change it
                .setProperty("password", datasonnetEx("payload.password", String.class))
                .choice()
                    .when(PredicateBuilder.or(exchangeProperty("name").isNull(),
                                                exchangeProperty("password").isNull(),
                                                exchangeProperty("email").isNull()))
                        .throwException(new BadRequestException(msg.BAD_REQUEST_ERROR))
                .otherwise()
                .log(LoggingLevel.INFO, "${body}")
                .to(sql("classpath:/sql/client/select-clientId-by-email.sql"))
                .choice()
                    .when(simple("${header.CamelSqlRowCount} != 0"))
                        .throwException(new BadRequestException(msg.BAD_REQUEST_ERROR))
                .otherwise()
                .to(sql("classpath:/sql/client/insert-client.sql"))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                    .setBody(constant(StringUtils.EMPTY))
                ;

        from(direct("register-driver"))
                .routeId("direct:register-driver")
                .transform(datasonnetEx("resource:classpath:create-driver-payload.ds", String.class))
                .setProperty("name", datasonnetEx("payload.driver.name", String.class))
                .setProperty("email", datasonnetEx("payload.driver.email", String.class))
                .setProperty("driverEmail", datasonnetEx("payload.driver.email", String.class))
                .setProperty("password", datasonnetEx("payload.driver.password", String.class))
                .setProperty("model", datasonnetEx("payload.car.model", String.class))
                .choice()
                    .when(PredicateBuilder.or(exchangeProperty("name").isNull(),
                                            exchangeProperty("password").isNull(),
                                            exchangeProperty("email").isNull(),
                                            exchangeProperty("model").isNull()))
                        .throwException(new BadRequestException(msg.BAD_REQUEST_ERROR))
                .otherwise()
                .log(LoggingLevel.INFO, "${body}")
                .to(sql("classpath:/sql/driver/select-driverId-by-email.sql"))
                .choice()
                    .when(simple("${header.CamelSqlRowCount} != 0"))
                        .throwException(new BadRequestException(msg.BAD_REQUEST_ERROR))
                .otherwise()
                .setHeader("CamelSqlRetrieveGeneratedKeys", constant(true))
                .to(sql("classpath:/sql/driver/insert-driver.sql"))
                .setProperty("driverId", simple("${headers.CamelSqlGeneratedKeyRows[0]['GENERATED_KEY']}"))
                .to(direct("register-car").getUri());

        from(direct("register-car"))
                .routeId("direct:register-car")
                .to(sql("classpath:/sql/car/insert-car.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                .setBody(constant(StringUtils.EMPTY));


    }
}
