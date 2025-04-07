package com.skcc.ra.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }

//    @Getter
//    @Setter
//    @Schema
//    static class Page {
//        @Schema(name = "페이지 번호(0..N)")
//        private Integer page;
//
//        @Schema(name = "페이지 크기", allowableValues = "range[0, 100]")
//        private Integer size;
//
//        @Schema(name = "정렬(사용법: 컬럼명,ASC|DESC)")
//        private List<String> sort;
//    }


//    private CollectionHelper Lists;

//    @Bean
//    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
//                                                                         EndpointsSupplier endpointsSupplier,
//                                                                         ControllerEndpointsSupplier controllerEndpointsSupplier,
//                                                                         EndpointMediaTypes endpointMediaTypes,
//                                                                         CorsEndpointProperties corsProperties,
//                                                                         WebEndpointProperties webEndpointProperties,
//                                                                         Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
//        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(endpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath);
//        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
//        return new WebMvcEndpointHandlerMapping(endpointMapping,
//                webEndpoints,
//                endpointMediaTypes,
//                corsProperties.toCorsConfiguration(),
//                new EndpointLinksResolver(allEndpoints, basePath),
//                shouldRegisterLinksMapping);
//    }
//    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties,
//                                               Environment environment,
//                                               String basePath) {
//        return webEndpointProperties.getDiscovery().isEnabled()
//                && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
//    }


//    @Bean
//    public Docket docket() {
//        return new Docket(DocumentationType.OAS_30)
//                .alternateTypeRules(AlternateTypeRules.newRule(Pageable.class, Page.class))
//                .useDefaultResponseMessages(false)
//                .securityContexts(Arrays.asList(securityContext()))
//                .securitySchemes(Arrays.asList(apiKey()))
//                .select()
//                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("API Documentation")
//                .description("")
//                .version("v1")
//                .license("ADT CAPS")
//                .build();
//    }
//
//    @Getter
//    @Setter
//    @ApiModel
//    static class Page {
//        @Schema(name = "페이지 번호(0..N)")
//        private Integer page;
//
//        @Schema(name = "페이지 크기", allowableValues = "range[0, 100]")
//        private Integer size;
//
//        @Schema(name = "정렬(사용법: 컬럼명,ASC|DESC)")
//        private List<String> sort;
//    }
//
//
//    private ApiKey apiKey(){
//        return new ApiKey("Authorization", "Authorization", "header");
//    }
//
//    private SecurityContext securityContext() {
//        return SecurityContext.builder().securityReferences(defaultAuth()).build();
//    }
//
//    private List<SecurityReference> defaultAuth(){
//        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
//    }
}