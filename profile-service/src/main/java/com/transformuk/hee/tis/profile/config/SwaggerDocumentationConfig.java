package com.transformuk.hee.tis.profile.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Configuration for swagger to auto generate our REST API documentation. For more info please
 * {@see http://swagger.io/getting-started/}
 */
@Configuration
@EnableSwagger2WebMvc
public class SwaggerDocumentationConfig {

  ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("TIS Profile API")
        .description("Profile Service REST API")
        .license("")
        .licenseUrl("")
        .termsOfServiceUrl("")
        .version("1.0.0")
        .contact(new Contact("Trainee Information Systems Team", "https://tis-support.hee.nhs.uk/",
            "england.tis.team@nhs.net"))
        .build();
  }

  @Bean
  public Docket customImplementation() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("Profile")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.transformuk.hee.tis.profile.web.rest"))
        .build()
        .apiInfo(apiInfo());
  }

  @Bean
  public LinkDiscoverers discoverers() {
    return new LinkDiscoverers(SimplePluginRegistry.of(new CollectionJsonLinkDiscoverer()));
  }
}
