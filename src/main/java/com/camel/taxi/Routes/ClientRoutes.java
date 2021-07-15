package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.datasonnet.document.MediaTypes;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.springframework.stereotype.Component;

@Component
public class ClientRoutes extends BaseRestRouteBuilder {


    @Override
    public void configure() throws Exception {
        super.configure();

        from(direct("get-client-by-id"))
                .routeId("direct:get-client-by-id")
                .log(LoggingLevel.INFO, "${body}")
                .setProperty("clientId", simple("${header.clientId}"))
                .to(sql("classpath:/sql/client/select-client-by-id.sql"))
                .transform(datasonnetEx("resource:classpath:client.ds", String.class)
                        .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                        .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                ;

        from(direct("retrieve-all-clients"))
                .routeId("direct:retrieve-all-clients")
                .log(LoggingLevel.INFO, "${body}")
                .to(sql("classpath:/sql/client/select-clients.sql"))
                .transform(datasonnetEx("resource:classpath:clients.ds", String.class)
                    .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                ;

        from(direct("put-client-by-id"))
                .routeId("direct:put-client-by-id")
                .log(LoggingLevel.INFO, "${body}")
                .setProperty("clientId", simple("${header.clientId}"))
                .setProperty("name", datasonnetEx("payload.name", String.class))
                .setProperty("password", datasonnetEx("payload.password", String.class))
                .to(sql("classpath:/sql/client/update-name-password-client.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                ;

        from(direct("delete-client-by-id"))
                .routeId("direct:delete-client-by-id")
                .setProperty("clientId", simple("${header.clientId}"))
                .to(sql("classpath:/sql/client/delete-client-by-id.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                ;
    }
}