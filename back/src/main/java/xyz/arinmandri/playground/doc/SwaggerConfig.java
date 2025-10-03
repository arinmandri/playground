package xyz.arinmandri.playground.doc;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class SwaggerConfig
{

	@Bean
	public OpenAPI customOpenAPI () {
		return new OpenAPI()
		        .info( new Info()
		                .title( "springdoc-openapi" )
		                .version( "1.0.0-SNAPSHOT" )
		                .description( "springdoc-openapi swagger-ui 화면입니다." ) );
	}

	@Bean
	public GroupedOpenApi all () {
		String[] paths = { "/**" };
		String[] packagesToScan = { "xyz.arinmandri.playground.apps" };
		return GroupedOpenApi.builder().group( "all" )
		        .pathsToMatch( paths )
		        .packagesToScan( packagesToScan )
		        .build();
	}

	@Bean
	public GroupedOpenApi forClient () {
		String[] paths = {
		        "/member/**",
		        "/post/**"
		};
		String[] packagesToScan = { "xyz.arinmandri.playground.apps" };
		return GroupedOpenApi.builder().group( "for-client" )
		        .pathsToMatch( paths )
		        .packagesToScan( packagesToScan )
		        .build();
	}
}
