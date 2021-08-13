package com.camel.taxi.routes;

import com.camel.taxi.Routes.ClientRoutes;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ClientRoutesTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new ClientRoutes();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.setMessageHistory(true);
        context.addComponent("sql", new MockComponent());
        return context;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void retrieveAllClientsTest() throws Exception {
        String routeToTest = "direct:retrieve-all-clients";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-clients.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/clients.json", List.class))
                ;
                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);
        assertEquals("Andrew", map[0].get("name"));
        assertEquals("ROLE_CLIENT", map[0].get("role"));
        assertEquals("andrew@email.com", map[0].get("email"));
    }

    @Test
    public void getClientByIdTest() throws Exception {
        String routeToTest = "direct:get-client-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-client-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(datasonnet("resource:classpath:/json/clients.json", List.class))
                ;
                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
        assertEquals("Andrew", map.get("name"));
        assertEquals("ROLE_CLIENT", map.get("role"));
        assertEquals("andrew@email.com", map.get("email"));
    }

    @Test
    public void putClientByIdTest() throws Exception {
        String routeToTest = "direct:put-client-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

                weaveAddFirst()
                        .setProperty("clientId", constant(1))
                        .setProperty("name", constant("Andy"))
                        .setProperty("password", constant("password"));

                weaveByToString(".*select-client-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                ;

                weaveByToString(".*update-name-password-client.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/clients.json", List.class));

                weaveAddLast().to("mock:result");
            }
        });


        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");

        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);

        assertEquals("ROLE_CLIENT", map[0].get("role"));
        assertEquals("andrew@email.com", map[0].get("email"));
    }

    @Test
    public void deleteClientByIdTest() throws Exception {
        String routeToTest = "direct:delete-client-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

                weaveAddFirst()
                        .setProperty("clientId", constant(1))
                        ;

                weaveByToString(".*select-client-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                ;

                weaveByToString(".*delete-client-by-id.*")
                        .replace()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                        .setBody(constant(StringUtils.EMPTY));

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");

        assertEquals(HttpStatus.OK.value(), mockEndpoint.getExchanges().get(0).getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));

    }
}
