package com.cts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.cts.filter.AuthenticationFilter;

@EnableDiscoveryClient
@SpringBootApplication
public class PharmacyGatewayApplication {

	@Autowired
	AuthenticationFilter authFilter;

	private static final Logger logger = LoggerFactory.getLogger(PharmacyGatewayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PharmacyGatewayApplication.class, args);
	}

	@LoadBalanced
	@Bean
	WebClient.Builder client() {
		return WebClient.builder();
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		logger.info("Routing Configuration complete");
		return builder.routes()
				.route(ps -> ps.path("/stock/**")
						.filters(f -> f.filter(authFilter)
								.stripPrefix(1))
						.uri("lb://medicine-stock"))
				.route(ps -> ps.path("/ui/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://pharmacy-ui"))
				.build();
	}
}
