package com.example.integration.controller;

import com.example.integration.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/google-sheets")
public class GoogleSheetsController {

    private final GoogleSheetsService googleSheetsService;

    @Autowired
    public GoogleSheetsController(GoogleSheetsService googleSheetsService) {
        this.googleSheetsService = googleSheetsService;
    }

    @GetMapping("/createReport")
    public ResponseEntity<String> createReport(@RequestParam long tenantId) {
        try {
            googleSheetsService.createReport(tenantId);
            return ResponseEntity.ok("Report created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating report: " + e.getMessage());
        }
    }

    @GetMapping("/read")
    public List<List<Object>> readData(@RequestParam String spreadsheetId, @RequestParam String range) throws IOException {
        return googleSheetsService.readData(spreadsheetId, range);
    }

    @PostMapping("/write")
    public void writeData(@RequestParam String spreadsheetId, @RequestParam String range, @RequestBody List<List<Object>> values) throws IOException {
        googleSheetsService.writeData(spreadsheetId, range, values);
    }

    @PostMapping("/append")
    public void appendData(@RequestParam String spreadsheetId, @RequestParam String range, @RequestBody List<List<Object>> values) throws IOException {
        googleSheetsService.appendData(spreadsheetId, range, values);
    }

    @PostMapping("/create")
    public void createSpreadsheet(@RequestParam String title) throws IOException {
        googleSheetsService.createSpreadsheet(title);
    }

    @PostMapping("/make-cells-bold")
    public String makeCellsBold(@RequestParam String spreadsheetId, @RequestParam String range) {
        try {
            googleSheetsService.makeCellsBold(spreadsheetId, range);
            return "Cells made bold successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to make cells bold.";
        }
    }

    @PostMapping("/make-cells-italic")
    public String makeCellsItalic(@RequestParam String spreadsheetId, @RequestParam String range) {
        try {
            googleSheetsService.makeCellsItalic(spreadsheetId, range);
            return "Cells made italic successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to make cells italic.";
        }
    }

    @PostMapping("/change-text-color")
    public String changeTextColor(@RequestParam String spreadsheetId, @RequestParam String range, @RequestParam int red, @RequestParam int green, @RequestParam int blue) {
        try {
            googleSheetsService.changeTextColor(spreadsheetId, range, red, green, blue);
            return "Text color changed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to change text color.";
        }
    }

    @PostMapping("/set-background-color")
    public String setBackgroundColor(@RequestParam String spreadsheetId, @RequestParam String range, @RequestParam int red, @RequestParam int green, @RequestParam int blue) {
        try {
            googleSheetsService.setBackgroundColor(spreadsheetId, range, red, green, blue);
            return "Background color set successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to set background color.";
        }
    }

    @PostMapping("/merge-cells")
    public String mergeCells(@RequestParam String spreadsheetId, @RequestParam String range) {
        try {
            googleSheetsService.mergeCells(spreadsheetId, range);
            return "Cells merged successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to merge cells.";
        }
    }

    @PostMapping("/add-borders")
    public String addBorders(@RequestParam String spreadsheetId, @RequestParam String range) {
        try {
            googleSheetsService.addBorders(spreadsheetId, range);
            return "Borders added successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to add borders.";
        }
    }

    @PostMapping("/remove-sheet")
    public String removeSheet(@RequestParam String spreadsheetId, @RequestParam String sheetName) {
        try {
            googleSheetsService.removeSheet(spreadsheetId, sheetName);
            return "Sheet removed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to remove sheet.";
        }
    }

    @PostMapping("/remove-sheet-by-title")
    public String removeSheetByTitle(@RequestParam String spreadsheetId, @RequestParam String sheetTitle) {
        try {
            googleSheetsService.removeSheetByTitle(spreadsheetId, sheetTitle);
            return "Sheet removed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to remove sheet.";
        }
    }


    @PostMapping("/remove-content")
    public String removeContent(@RequestParam String spreadsheetId, @RequestParam String range) {
        try {
            googleSheetsService.removeContent(spreadsheetId, range);
            return "Content removed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to remove content.";
        }
    }

    @PostMapping("/create-spreadsheet-in-folder")
    public String createSpreadsheetInFolder(@RequestParam String title, @RequestParam String folderId) {
        try {
            googleSheetsService.createSpreadsheetInFolder(title, folderId);
            return "Spreadsheet created successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to create spreadsheet.";
        }
    }
}
