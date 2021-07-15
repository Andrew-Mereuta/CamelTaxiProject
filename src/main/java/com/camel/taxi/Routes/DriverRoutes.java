package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.datasonnet.document.MediaTypes;
import fansi.Str;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class DriverRoutes extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        from(direct("get-driver-by-id"))
            .routeId("direct:get-driver-by-id")
            .setProperty("driverId", simple("${header.driverId}"))
            .to(sql("classpath:/sql/driver/select-driver-by-id.sql"))
            .transform(datasonnetEx("resource:classpath:driver.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        from(direct("update-driver-by-id"))
            .routeId("direct:update-driver-by-id")
            .setProperty("driverId", simple("${header.driverId}"))
            .setProperty("name", datasonnetEx("payload.name", String.class))
            .setProperty("password", datasonnetEx("payload.password", String.class))
            .to(sql("classpath:/sql/driver/update-driver-by-id.sql"))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        from(direct("delete-driver-by-id"))
            .routeId("direct:delete-driver-by-id")
            .setProperty("driverId", simple("${header.driverId}"))
            .to(sql("classpath:/sql/car/delete-car-by-driverId.sql"))
            .to(sql("classpath:/sql/driver/delete-driver-by-id.sql"))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        from(direct("get-all-drivers"))
            .routeId("direct:get-all-drivers")
            .to(sql("classpath:/sql/driver/select-all-drivers.sql"))
            .transform(datasonnetEx("resource:classpath:drivers.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;
    }
}
