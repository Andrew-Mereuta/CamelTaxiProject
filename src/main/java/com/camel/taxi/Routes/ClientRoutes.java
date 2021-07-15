package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.datasonnet.document.MediaTypes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class ClientRoutes extends BaseRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(direct("get-client-by-id"))
                .routeId("direct:get-client-by-id")
                .log(LoggingLevel.INFO, "${body}")
                .setProperty("clientId", simple("${header.clientId}"))
                .to(sql("classpath:/client/select-client-by-id.sql")) // throws here exception !!! datasonnet
                .transform(datasonnetEx("resource:classpath:client.ds", String.class)
                        .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                        .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                ;


    }
}