package com.cts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.cts.vo.AuthenticationResponse;
import com.cts.vo.PharmacyUser;

import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {

	@Autowired
	WebClient.Builder webClient;

	private String baseUrl = "http://pharmacy-authentication";

	@PostMapping("/authenticate")
	public Mono<ResponseEntity<AuthenticationResponse>> authenticate(@RequestBody PharmacyUser user) {
		return webClient.baseUrl(baseUrl)
				.build()
				.post()
				.uri("/security/authenticate")
				.body(Mono.just(user), PharmacyUser.class)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToMono(AuthenticationResponse.class)
				.flatMap(data -> Mono.just(ResponseEntity.ok()
						.body(data)))
				.onErrorResume(error -> Mono.just(ResponseEntity.badRequest()
						.build()));
	}

}
