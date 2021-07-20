package com.camel.taxi.Routes;

import com.camel.taxi.BaseRestRouteBuilder;
import com.datasonnet.document.MediaTypes;
import com.ms3_inc.tavros.extensions.rest.exception.BadRequestException;
import com.ms3_inc.tavros.extensions.rest.exception.NotFoundException;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CarRoutes extends BaseRestRouteBuilder {

    @Override
    public void configure() throws Exception {
        super.configure();

        from(direct("get-all-cars"))
            .routeId("direct:get-all-cars")
            .to(sql("classpath:/sql/car/select-all-cars.sql"))
            .transform(datasonnetEx("resource:classpath:cars.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        from(direct("get-car-by-id"))
            .routeId("direct:get-car-by-id")
            .to(sql("classpath:/sql/car/select-car-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .transform(datasonnetEx("resource:classpath:car.ds", String.class)
                    .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                    .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        from(direct("update-car-by-id"))
            .routeId("direct:update-car-by-id")
            .to(sql("classpath:/sql/car/select-car-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .to(sql("classpath:/sql/driver/select-driverId-by-email.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
            .to(sql("classpath:/sql/car/update-car-by-id.sql"))
            .to(sql("classpath:/sql/car/select-car-by-id.sql"))
            .transform(datasonnetEx("resource:classpath:car.ds", String.class)
                .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                .outputMediaType(MediaTypes.APPLICATION_JSON))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;

        // have doubts about implementation, have to doublecheck with basecamp project
        from(direct("delete-car-by-id"))
            .routeId("direct:delete-car-by-id")
            .to(sql("classpath:/sql/car/select-car-by-id.sql"))
            .choice()
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .to(sql("classpath:/sql/car/delete-car-by-id.sql"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .setBody(constant(StringUtils.EMPTY))
        ;

        from(direct("patch-car-by-id"))
            .routeId("direct:patch-car-by-id")
            .to(sql("classpath:/sql/car/select-car-by-id.sql"))
            .choice()
                .when(exchangeProperty("model").isNull())
                    .throwException(new BadRequestException(msg.BAD_REQUEST_ERROR))
                .when(simple("${header.CamelSqlRowCount} == 0"))
                    .throwException(new NotFoundException(msg.NOT_FOUND_ERROR))
            .otherwise()
                .to(sql("classpath:/sql/car/update-model-of-car-by-id.sql"))
                .to(sql("classpath:/sql/car/select-car-by-id.sql"))
                .transform(datasonnetEx("resource:classpath:car.ds", String.class)
                        .bodyMediaType(MediaTypes.APPLICATION_JAVA)
                        .outputMediaType(MediaTypes.APPLICATION_JSON))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
        ;
    }
}
