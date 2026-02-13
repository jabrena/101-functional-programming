package info.jab.ms.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import info.jab.ms.model.MyResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class MyEndpointController {

    @Bean
    public RouterFunction<ServerResponse> myEndpointRouter() {
        return route()
                .nest(path("/api/v1"), builder -> builder
                        .GET("/my-endpoint", request -> 
                                ServerResponse.ok().body(new MyResponse("Hello World"))))
                .build();
    }
}
