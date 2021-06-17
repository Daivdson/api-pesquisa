package br.leg.camara.apipesquisa.configuracao;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {
	
	public static final String URL_DOCUMENTATION = "/docs";

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.leg.camara.apipesquisa.adaptadores.controlador"))
				.build()
				.apiInfo(apiInfo());

	}

	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API de pesquisa")
                .build();
    }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler(URL_DOCUMENTATION + "/**").addResourceLocations("classpath:/META-INF/resources/");
	}

}
