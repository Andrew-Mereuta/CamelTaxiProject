package com.camel.taxi.routes;

import com.camel.taxi.Routes.DriverRoutes;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DriverRoutesTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new DriverRoutes();
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
    public void getDriverByIdTest() throws Exception {
        String routeToTest = "direct:get-driver-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-driver-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(datasonnet("resource:classpath:/json/drivers.json", List.class))
                ;
                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
        Assertions.assertEquals("Mr.Rock", map.get("name"));
        Assertions.assertEquals("rock@email.com", map.get("email"));
        Assertions.assertEquals("ROLE_DRIVER", map.get("role"));
        Assertions.assertEquals(false, map.get("is_busy"));
    }

    @Test
    public void updateDriverByIdTest() throws Exception {
        String routeToTest = "direct:update-driver-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-driver-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(constant(StringUtils.EMPTY))
                ;

                weaveByToString(".*update-driver-by-id.*")
                        .replace()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                        .setBody(constant(StringUtils.EMPTY))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        assertEquals(HttpStatus.OK.value(), mockEndpoint.getExchanges().get(0).getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));
    }

    @Test
    public void deleteDriverByIdTest() throws Exception {
        String routeToTest = "direct:delete-driver-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-driver-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(constant(StringUtils.EMPTY))
                ;

                weaveByToString("To[sql://classpath:/sql/driver/delete-driver-by-id.sql]")
                        .replace()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                        .setBody(constant(StringUtils.EMPTY))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        assertEquals(HttpStatus.OK.value(), mockEndpoint.getExchanges().get(0).getIn().getHeader(Exchange.HTTP_RESPONSE_CODE));
    }

    @Test
    public void getAllDriversTest() throws Exception {
        String routeToTest = "direct:get-all-drivers";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-all-drivers.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/drivers.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);
        assertEquals("Mr.Rock", map[0].get("name"));
        assertEquals("ROLE_DRIVER", map[0].get("role"));
        assertEquals("rock@email.com", map[0].get("email"));
        assertEquals(false, map[0].get("is_busy"));
    }

}
