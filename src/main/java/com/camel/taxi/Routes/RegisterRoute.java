package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.datasonnet.document.MediaType;
import com.datasonnet.document.MediaTypes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
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
                .setProperty("password", datasonnetEx("payload.password", String.class))
                .log(LoggingLevel.INFO, "${body}")
                .to(sql("classpath:/client/insert-client.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                .setBody(constant(StringUtils.EMPTY))
//                .transform(datasonnetEx("resource:classpath:client.ds", String.class)
//                        .bodyMediaType(MediaTypes.APPLICATION_JAVA)
//                        .outputMediaType(MediaTypes.APPLICATION_JSON))
                ;

        // TODO
        from(direct("register-driver"))
                .routeId("direct:register-driver")
                .transform(datasonnetEx("resource:classpath:create-driver-payload.ds", String.class))
                .setProperty("name", datasonnetEx("payload.driver.name", String.class))
                .setProperty("email", datasonnetEx("payload.driver.email", String.class))
                .setProperty("password", datasonnetEx("payload.driver.password", String.class))
                .setProperty("model", datasonnetEx("payload.car.model", String.class))
                .log(LoggingLevel.INFO, "${body}")
                .setHeader("CamelSqlRetrieveGeneratedKeys", constant(true))
                .to(sql("classpath:/driver/insert-driver.sql")) // here I insert driver into db, now I need to get his id, and put it to the property, so that I can insert car into db
                .setProperty("driverId", simple("${headers.CamelSqlGeneratedKeyRows[0]['GENERATED_KEY']}"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Map<String, Object> properties = exchange.getProperties();
                    }
                })
                .to(sql("classpath:/car/insert-car.sql"))
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Map<String, Object> properties = exchange.getProperties();
                    }
                })
                ;


    }
}
