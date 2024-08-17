package com.example.integration.service;


import com.example.integration.exception.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new BadRequestException("Input cannot be null or empty");
        }
    }
}
