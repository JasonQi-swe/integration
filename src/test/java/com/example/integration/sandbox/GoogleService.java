package com.example.integration.sandbox;

import com.example.integration.service.GoogleSheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleService {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        GoogleSheetsService s = new GoogleSheetsService();
        System.out.println(s.toString());
        s.createSpreadsheetInFolder("test-title-integration", "1971TSoVeb8q5DxRpg_-2ZA8gR17lJmuR");
    }
}
