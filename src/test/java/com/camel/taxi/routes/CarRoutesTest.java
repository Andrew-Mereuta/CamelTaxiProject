package com.camel.taxi.routes;

import com.camel.taxi.Routes.CarRoutes;
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

public class CarRoutesTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new CarRoutes();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.setMessageHistory(true);
        context.addComponent("sql", new MockComponent());
        return context;
    }

    @Test
    public void getAllCarsTest() throws Exception {
        String routeToTest = "direct:get-all-cars";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-all-cars.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/cars.json", List.class))
                ;
                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);
        assertEquals("Mers", map[0].get("model"));
        assertEquals("rock@email.com", map[0].get("driver-email"));
        assertEquals("Mr.Rock", map[0].get("driver-name"));
    }

    @Test
    public void getCarByIdTest() throws Exception {
        String routeToTest = "direct:get-car-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-car-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(datasonnet("resource:classpath:/json/cars.json", List.class))
                ;
                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
        assertEquals("Mers", map.get("model"));
        assertEquals("rock@email.com", map.get("driver-email"));
        assertEquals("Mr.Rock", map.get("driver-name"));
    }

    @Test
    public void updateCarByIdTest() throws Exception {
        String routeToTest = "direct:update-car-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddFirst()
                        .setProperty("carId", constant(1))
                        .setProperty("model", constant("BMW"))
                        .setProperty("driverEmail", constant("rock@email.com"));


                weaveByToString(".*select-car-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                ;

                weaveByToString(".*select-driverId-by-email.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                ;

                weaveByToString("To[sql://classpath:/sql/car/update-car-by-id.sql]")
                        .replace()
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                        .setBody(datasonnet("resource:classpath:/json/cars.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
        assertEquals("Mers", map.get("model"));
        assertEquals("rock@email.com", map.get("driver-email"));
        assertEquals("Mr.Rock", map.get("driver-name"));
    }

    @Test
    public void deleteCarByIdTest() throws Exception {
        String routeToTest = "direct:delete-car-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddFirst()
                        .setProperty("carId", constant(1));

                weaveByToString(".*select-car-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                ;

                weaveByToString(".*delete-car-by-id.*")
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
    public void patchCarByIdTest() throws Exception {
        String routeToTest = "direct:patch-car-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddFirst()
                        .setProperty("carId", constant(1))
                        .setProperty("model", constant("Mers"))
                ;

                weaveByToString(".*select-car-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                ;

                weaveByToString(".*update-model-of-car-by-id.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/cars.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
        assertEquals("Mers", map.get("model"));
        assertEquals("rock@email.com", map.get("driver-email"));
        assertEquals("Mr.Rock", map.get("driver-name"));
    }
}
