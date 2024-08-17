package com.example.integration.client.config;

import com.example.integration.exception.exceptions.ServiceUnavailableException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.REQUEST_TIMEOUT.value()) {
            return new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Request timeout while calling " + methodKey);
        } else if (response.status() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            return new ServiceUnavailableException("Service unavailable while calling " + methodKey);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
