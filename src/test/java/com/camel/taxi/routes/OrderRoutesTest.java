package com.camel.taxi.routes;

import com.camel.taxi.Routes.OrderRoutes;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderRoutesTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new OrderRoutes();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();
        context.setMessageHistory(true);
        context.addComponent("sql", new MockComponent());
        return context;
    }

    @Test
    public void createOrderTest() throws Exception {
        String routeToTest = "direct:create-order";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveAddFirst()
                        .setProperty("clientId", constant(1))
                        .setProperty("driverId", constant(1))
                        .setProperty("price", constant(94))
                        .setProperty("orderId", constant(1))
                ;

                weaveByToString(".*select-client-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(constant(null))
                ;

                weaveByToString(".*select-driver-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(constant(null))
                ;

                List<Object> generatedKeys = new ArrayList<Object>(0);
                Map<String, Object> generatedKeyMap = new HashMap<String, Object>(0);
                generatedKeyMap.put("GENERATED_KEY", "1");
                generatedKeys.add(generatedKeyMap);

                weaveByToString(".*insert-order.*")
                        .replace()
                        .setHeader("CamelSqlGeneratedKeyRows", constant(generatedKeys))
                        .setBody(simple("{}"))
                ;

                weaveByToString(".*select-order-by-id.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/orders.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
        Assertions.assertEquals("andrew@email.com", map.get("clientEmail"));
        Assertions.assertEquals("rock@email.com", map.get("driverEmail"));
        Assertions.assertEquals(94, map.get("price"));
        Assertions.assertEquals("Mers", map.get("car"));
    }

    @Test
    public void getOrderByIdTest() throws Exception {
        String routeToTest = "direct:get-orders-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

                weaveByToString(".*select-order-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(datasonnet("resource:classpath:/json/orders.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);
        Assertions.assertEquals("andrew@email.com", map[0].get("clientEmail"));
        Assertions.assertEquals("rock@email.com", map[0].get("driverEmail"));
        Assertions.assertEquals(94, map[0].get("price"));
        Assertions.assertEquals("Mers", map[0].get("car"));
    }

    @Test
    public void getAllOrdersTest() throws Exception {
        String routeToTest = "direct:get-all-orders";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

                weaveByToString(".*select-all-orders.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/orders.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);
        Assertions.assertEquals("andrew@email.com", map[0].get("clientEmail"));
        Assertions.assertEquals("rock@email.com", map[0].get("driverEmail"));
        Assertions.assertEquals(94, map[0].get("price"));
        Assertions.assertEquals("Mers", map[0].get("car"));
    }

    @Test
    public void getOrdersByDriverIdTest() throws Exception {
        String routeToTest = "direct:get-orders-by-driverId";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

                weaveByToString(".*select-driver-by-id.*")
                        .replace()
                        .setHeader("CameSqlRowCount", constant("1"))
                        .setBody(constant(null))
                ;

                weaveByToString(".*select-order-by-driverId.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/orders.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);
        Assertions.assertEquals("andrew@email.com", map[0].get("clientEmail"));
        Assertions.assertEquals("rock@email.com", map[0].get("driverEmail"));
        Assertions.assertEquals(94, map[0].get("price"));
        Assertions.assertEquals("Mers", map[0].get("car"));
    }

    @Test
    public void getOrdersByClientIdTest() throws Exception {
        String routeToTest = "direct:get-orders-by-clientId";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {

                weaveByToString(".*select-client-by-id.*")
                        .replace()
                        .setHeader("CameSqlRowCount", constant("1"))
                        .setBody(constant(null))
                ;

                weaveByToString(".*select-order-by-clientId.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/orders.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String>[] map = mapper.readValue(retrievedBody, Map[].class);
        Assertions.assertEquals("andrew@email.com", map[0].get("clientEmail"));
        Assertions.assertEquals("rock@email.com", map[0].get("driverEmail"));
        Assertions.assertEquals(94, map[0].get("price"));
        Assertions.assertEquals("Mers", map[0].get("car"));
    }

    //not working!!!!!!
//    @Test
//    public void updateOrderByIdTest() throws Exception {
//        String routeToTest = "direct:update-order-by-id";
//
//        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                weaveAddFirst()
//                        .setProperty("orderId", constant(1))
//                        .setProperty("clientEmail", constant("andrew@email.com"))
//                        .setProperty("driverEmail", constant("rock@email.com"))
//                        .setProperty("price", constant(94))
//                ;
//
//                weaveByToString(".*select-order-by-id-custom.*")
//                        .replace()
//                        .setHeader("CamelSqlRowCount", constant("1"))
//                        .setBody(constant(null))
//                ;
//
////                generated keys
//                List<Object> generatedKeys = new ArrayList<Object>(0);
//                Map<String, Object> generatedKeyMap = new HashMap<String, Object>(0);
//                generatedKeyMap.put("GENERATED_KEY", "1");
//                generatedKeys.add(generatedKeyMap);
//
//                weaveByToString(".*select-clientId-by-email.*")
//                        .replace()
//                        .setHeader("CamelSqlRowCount", constant("1"))
//                        .setHeader("CamelSqlGeneratedKeyRows", constant(generatedKeys))
//                        .setProperty("clientId", constant("1"))
//                        .setBody(simple("{}"))
//                ;
//
//
//                //generated keys
//                List<Object> generatedKeys1 = new ArrayList<Object>(0);
//                Map<String, Object> generatedKeyMap1 = new HashMap<String, Object>(0);
//                generatedKeyMap1.put("GENERATED_KEY", "1");
//                generatedKeys.add(generatedKeyMap1);
//
//                weaveByToString(".*select-driverId-by-email.*")
//                        .replace()
//                        .setHeader("CamelSqlRowCount", constant("1"))
//                        .setHeader("CamelSqlGeneratedKeyRows", constant(generatedKeys1))
//                        .setProperty("driverId", constant("1"))
//                        .setBody(simple("1"))
//                ;
//
//                weaveByToString(".*update-order-by-id.*")
//                        .replace()
//                        .setBody(constant(null))
//                ;
//
//                weaveByToString(".*select-order-by-id.*")
//                        .replace()
//                        .setBody(datasonnet("resource:classpath:/json/orders.json", List.class))
//                ;
//
//                weaveAddLast().to("mock:result");
//            }
//        });
//
//        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
//        template.sendBody(routeToTest, "");
//        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
//        Assertions.assertEquals("andrew@email.com", map.get("clientEmail"));
//        Assertions.assertEquals("rock@email.com", map.get("driverEmail"));
//        Assertions.assertEquals(94, map.get("price"));
//        Assertions.assertEquals("Mers", map.get("car"));
//    }

    @Test
    public void deleteOrderByIdTest() throws Exception {
        String routeToTest = "direct:delete-order-by-id";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-order-by-id-custom.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(constant(null))
                ;

                weaveByToString("To[sql://classpath:/sql/order/delete-order-by-id.sql]")
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
    public void changeDriverOfOrderTest() throws Exception {
        String routeToTest = "direct:change-driver-of-order";

        AdviceWith.adviceWith(context.getRouteDefinition(routeToTest), context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString(".*select-order-by-id-custom.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(constant(null))
                ;

                weaveByToString(".*select-driver-by-id.*")
                        .replace()
                        .setHeader("CamelSqlRowCount", constant("1"))
                        .setBody(constant(null))
                ;

                weaveByToString(".*update-driver-of-order-by-id.*")
                        .replace()
                        .setBody(datasonnet("resource:classpath:/json/orders.json", List.class))
                ;

                weaveAddLast().to("mock:result");
            }
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:result");
        template.sendBody(routeToTest, "");
        String retrievedBody = mockEndpoint.getExchanges().get(0).getIn().getBody().toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(retrievedBody, Map.class);
        Assertions.assertEquals("andrew@email.com", map.get("clientEmail"));
        Assertions.assertEquals("rock@email.com", map.get("driverEmail"));
        Assertions.assertEquals(94, map.get("price"));
        Assertions.assertEquals("Mers", map.get("car"));
    }

}
