package com.camel.taxi.routes;

import com.camel.taxi.Routes.RegisterRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterRouteTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RegisterRoute();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.setMessageHistory(true);
        context.addComponent("sql", new MockComponent());
        return context;
    }

    @Test
    public void registerClientTest() throws Exception {
        String routeToTest = "direct:register-client";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddFirst()
                        .setProperty("name", constant("Andrew"))
                        .setProperty("email", constant("andrew@email.com"))
                        .setProperty("clientEmail", constant("andrew@email.com"))
                        .setProperty("password", constant("password"))
                ;

                weaveByToString(".*select-clientId-by-email.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("0"))
                        .setBody(constant(null))
                ;

                weaveByToString(".*insert-client.*")
                        .replace()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                        .setBody(constant(StringUtils.EMPTY))
                ;
                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        assertEquals(HttpStatus.CREATED.value(), mockEndpoint.getExchanges().get(0).getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));
    }

    @Test
    public void registerDriverTest() throws Exception {
        String routeToTest = "direct:register-car";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddFirst()
                        .setProperty("name", constant("Andrew"))
                        .setProperty("email", constant("andrew@email.com"))
                        .setProperty("clientEmail", constant("andrew@email.com"))
                        .setProperty("password", constant("password"))
                        .setProperty("driverId", constant("1"))
                ;

                weaveByToString(".*insert-car.*")
                        .replace()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                        .setBody(constant(StringUtils.EMPTY))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        assertEquals(HttpStatus.CREATED.value(), mockEndpoint.getExchanges().get(0).getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));
    }
}
