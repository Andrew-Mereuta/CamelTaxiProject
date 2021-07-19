package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.camel.taxi.Messages.Messages;
import com.datasonnet.document.MediaTypes;
import com.ms3_inc.tavros.extensions.rest.exception.BadRequestException;
import com.ms3_inc.tavros.extensions.rest.exception.NotFoundException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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
                .to(sql("classpath:/sql/client/select-client-by-id.sql"))
                .choice()
                    .when(simple("${header.CamelSqlRowCount} == 0"))
                        .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
                .end()
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
                .transform(datasonnetEx("resource:classpath:create-client-payload.ds", String.class))
                .setProperty("clientId", simple("${header.clientId}"))
                .setProperty("name", datasonnetEx("payload.name", String.class))
                .setProperty("password", datasonnetEx("payload.password", String.class))
                .to(sql("classpath:/sql/client/select-client-by-id.sql"))
                .choice()
                    .when(simple("${header.CamelSqlRowCount} == 0"))
                        .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
                    .when(PredicateBuilder.or(exchangeProperty("name").isNull(),  exchangeProperty("password").isNull()))
                        .throwException(new BadRequestException(msg.BAD_REQUEST_ERROR))
                    .otherwise()
                        .to(sql("classpath:/sql/client/update-name-password-client.sql"))
                        .transform(datasonnetEx("resource:classpath:clients.ds", String.class)
                            .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                            .outputMediaType(MediaTypes.APPLICATION_JSON))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                ;

        from(direct("delete-client-by-id"))
                .routeId("direct:delete-client-by-id")
                .setProperty("clientId", simple("${header.clientId}"))
                .to(sql("classpath:/sql/client/select-client-by-id.sql"))
                .choice()
                    .when(simple("${header.CamelSqlRowCount} == 0"))
                        .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
                    .otherwise()
                        .to(sql("classpath:/sql/client/delete-client-by-id.sql"))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                        .setBody(constant(StringUtils.EMPTY))
                ;
    }
}