package com.cts.exception;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	private HttpStatus status = HttpStatus.BAD_REQUEST;
	private String message = "Invalid/Bad Token";

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
		Map<String, Object> map = super.getErrorAttributes(request, options);
		map.put("status", getStatus());
		map.put("message", getMessage());
		return map;
	}
}
