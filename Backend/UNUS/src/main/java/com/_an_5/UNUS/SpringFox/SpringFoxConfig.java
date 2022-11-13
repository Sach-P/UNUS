package com._an_5.UNUS.SpringFox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Predicates;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collections;

@EnableSwagger2
@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                //.apis(Predicates.(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                //.apis(RequestHandlerSelectors.any())
                //.paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "UNUS REST API",
                "All requests and models from the backend.",
                "API TOS",
                "Terms of service",
                new Contact("Sachin Patel", " ", "sachpat@iastate.edu"),
                "License of API", "API license URL", Collections.emptyList());
    }
}