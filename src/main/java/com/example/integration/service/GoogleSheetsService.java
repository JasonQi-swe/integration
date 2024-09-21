package com.example.integration.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.example.integration.entity.Application;
import com.example.integration.entity.CheckedJob;
import com.example.integration.entity.Job;
import com.example.integration.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GoogleSheetsService {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "src/main/resources/tokens";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "client_secret.json";

    private final Sheets sheetsService;
    private final Drive driveService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private CheckedJobService checkedJobService;

    @Autowired
    private JobService jobService;

    public GoogleSheetsService() throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void createReport(long tenantId) throws IOException {
        Optional<Tenant> tenantOptional = tenantService.findById(tenantId);
        Tenant tenant = null;
        if(tenantOptional.isPresent()){
            tenant = tenantOptional.get();
        }else{
            log.error("Failed to create report for tenant {}", tenantId);
        }

        Spreadsheet spreadsheet = this.createSpreadsheet(tenant.getUserName() + "_" + LocalDate.now());

        List<List<Object>> values = new ArrayList<>();
        values.add(List.of("Tenant Username", "Job ID", "Job Title", "Job URL", "Cover Letter", "Reason to select"));

        List<Application> applications = applicationService.findAllByTenantIdAndDate(tenant.getId(), LocalDate.now());

        for (Application application : applications) {
            Job job = application.getJob();
            String title = jobService.findById(Long.valueOf(job.getId().toString())).get().getTitle();
            String userName = tenant.getUserName();
            String jobId = job.getId() != null ? job.getId().toString() : null;
            String jobUrl = job.getUrl();
            String coverLetter = application.getCoverLetter();
            String reasonToSelect = application.getReasonToSelect();
            try {
                values.add(List.of(
                        tenant.getUserName(),
                        job.getId().toString(),
                        title != null ? title : " ",
                        job.getUrl(),
                        application.getCoverLetter() != null ? application.getCoverLetter() : "--",
                        application.getReasonToSelect()
                ));
            }catch(Exception e){
                log.info("userName: {}", userName);
                log.info("jobId: {}", jobId);
                log.info("title: {}", title);
                log.info("jobUrl: {}", jobUrl);
                log.info("coverLetter: {}", coverLetter);
                log.info("reasonToSelect: {}", reasonToSelect);
                throw e;
            }
        }

        this.updateSheet(spreadsheet.getSpreadsheetId(), "Sheet1!A1", values);

        String newSheetName = "Checked_Jobs";
        this.createNewSheet(spreadsheet.getSpreadsheetId(), newSheetName);
        List<List<Object>> valuesCheckedJobs = new ArrayList<>();
        valuesCheckedJobs.add(List.of("Job ID", "Job Title", "Job URL", "Job Description", "Reason to skip", "Checked Date"));
        List<CheckedJob> checkedJobList = checkedJobService.findByTenantIdAndLocalDate(tenantId, LocalDate.now());
        for(CheckedJob checkedJob: checkedJobList){
            valuesCheckedJobs.add(List.of(
                    checkedJob.getId(),
                    checkedJob.getJob().getTitle()!=null ? checkedJob.getJob().getTitle() : " ",
                    checkedJob.getJob().getUrl(),
                    checkedJob.getJob().getDescription(),
                    checkedJob.getReasonToSkip(),
                    checkedJob.getAddedDateTime().toString()
            ));
        }
        this.updateSheet(spreadsheet.getSpreadsheetId(), newSheetName + "!A1", valuesCheckedJobs);
    }

    private void createNewSheet(String spreadsheetId, String sheetTitle) throws IOException {
        AddSheetRequest addSheetRequest = new AddSheetRequest().setProperties(new SheetProperties().setTitle(sheetTitle));
        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request().setAddSheet(addSheetRequest)));
        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest).execute();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets from the classpath.
        ClassPathResource resource = new ClassPathResource(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(resource.getInputStream()));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public List<List<Object>> readData(String spreadsheetId, String range) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        return response.getValues();
    }

    public void writeData(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(values);
        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public void appendData(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(values);
        AppendValuesResponse result = sheetsService.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();
    }

    public Spreadsheet createSpreadsheet(String title) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle(title));
        return sheetsService.spreadsheets().create(spreadsheet).execute();
    }

    public void makeCellsBold(String spreadsheetId, String range) throws IOException {
        CellFormat cellFormat = new CellFormat().setTextFormat(new TextFormat().setBold(true));
        applyCellFormat(spreadsheetId, range, cellFormat, "userEnteredFormat.textFormat.bold");
    }

    public void makeCellsItalic(String spreadsheetId, String range) throws IOException {
        CellFormat cellFormat = new CellFormat().setTextFormat(new TextFormat().setItalic(true));
        applyCellFormat(spreadsheetId, range, cellFormat, "userEnteredFormat.textFormat.italic");
    }

    public void changeTextColor(String spreadsheetId, String range, int red, int green, int blue) throws IOException {
        Color textColor = new Color().setRed(red / 255.0f).setGreen(green / 255.0f).setBlue(blue / 255.0f);
        CellFormat cellFormat = new CellFormat().setTextFormat(new TextFormat().setForegroundColor(textColor));
        applyCellFormat(spreadsheetId, range, cellFormat, "userEnteredFormat.textFormat.foregroundColor");
    }

    public void setBackgroundColor(String spreadsheetId, String range, int red, int green, int blue) throws IOException {
        Color backgroundColor = new Color().setRed(red / 255.0f).setGreen(green / 255.0f).setBlue(blue / 255.0f);
        CellFormat cellFormat = new CellFormat().setBackgroundColor(backgroundColor);
        applyCellFormat(spreadsheetId, range, cellFormat, "userEnteredFormat.backgroundColor");
    }

    public void mergeCells(String spreadsheetId, String range) throws IOException {
        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();

        GridRange gridRange = new GridRange().setSheetId(getSheetId(spreadsheetId, range)).setStartRowIndex(1).setEndRowIndex(10).setStartColumnIndex(1).setEndColumnIndex(2);
        MergeCellsRequest mergeCellsRequest = new MergeCellsRequest().setRange(gridRange).setMergeType("MERGE_ALL");

        requestBody.setRequests(Collections.singletonList(new Request().setMergeCells(mergeCellsRequest)));

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody).execute();
    }

    public void addBorders(String spreadsheetId, String range) throws IOException {
        Border border = new Border().setStyle("SOLID").setWidth(1).setColor(new Color().setRed(0f).setGreen(0f).setBlue(0f));
        UpdateBordersRequest updateBordersRequest = new UpdateBordersRequest()
                .setRange(new GridRange().setSheetId(getSheetId(spreadsheetId, range)).setStartRowIndex(1).setEndRowIndex(10).setStartColumnIndex(1).setEndColumnIndex(2))
                .setTop(border).setBottom(border).setLeft(border).setRight(border);

        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();
        requestBody.setRequests(Collections.singletonList(new Request().setUpdateBorders(updateBordersRequest)));

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody).execute();
    }

    public void updateSheet(String spreadsheetId, String range, List<List<Object>> values) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(values);
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    public void removeSheet(String spreadsheetId, String sheetName) throws IOException {
        Integer sheetId = getSheetId(spreadsheetId, sheetName);

        DeleteSheetRequest deleteSheetRequest = new DeleteSheetRequest().setSheetId(sheetId);

        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request().setDeleteSheet(deleteSheetRequest)));

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
    }

    public void removeSheetByTitle(String spreadsheetId, String sheetTitle) throws IOException {
        Integer sheetId = getSheetId(spreadsheetId, sheetTitle);

        DeleteSheetRequest deleteSheetRequest = new DeleteSheetRequest().setSheetId(sheetId);

        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request().setDeleteSheet(deleteSheetRequest)));

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
    }

    public void removeContent(String spreadsheetId, String range) throws IOException {
        ClearValuesRequest requestBody = new ClearValuesRequest();
        sheetsService.spreadsheets().values().clear(spreadsheetId, range, requestBody).execute();
    }

    public Spreadsheet createSpreadsheetInFolder(String title, String folderId) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties().setTitle(title));
        Spreadsheet createdSpreadsheet = sheetsService.spreadsheets().create(spreadsheet).execute();

        File fileMetadata = new File();
        fileMetadata.setParents(Collections.singletonList(folderId));
        driveService.files().update(createdSpreadsheet.getSpreadsheetId(), fileMetadata).execute();

        return createdSpreadsheet;
    }

    private void applyCellFormat(String spreadsheetId, String range, CellFormat cellFormat, String fields) throws IOException {
        BatchUpdateSpreadsheetRequest requestBody = new BatchUpdateSpreadsheetRequest();

        RepeatCellRequest repeatCellRequest = new RepeatCellRequest()
                .setRange(new GridRange().setSheetId(getSheetId(spreadsheetId, range)).setStartRowIndex(1).setEndRowIndex(10).setStartColumnIndex(1).setEndColumnIndex(2))
                .setCell(new CellData().setUserEnteredFormat(cellFormat))
                .setFields(fields);

        requestBody.setRequests(Collections.singletonList(new Request().setRepeatCell(repeatCellRequest)));

        sheetsService.spreadsheets().batchUpdate(spreadsheetId, requestBody).execute();
    }

    private Integer getSheetId(String spreadsheetId, String range) throws IOException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        String sheetName = range.split("!")[0];
        for (Sheet sheet : spreadsheet.getSheets()) {
            if (sheet.getProperties().getTitle().equals(sheetName)) {
                return sheet.getProperties().getSheetId();
            }
        }
        throw new IllegalArgumentException("Sheet not found: " + sheetName);
    }
}
