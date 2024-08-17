package com.example.integration.controller;

import com.example.integration.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/run")
    public ResponseEntity<String> runTask(@RequestParam("id") long id) {
        try {
            taskService.runTask(id);
            return ResponseEntity.ok("Task executed successfully.");
        } catch (GeneralSecurityException | IOException e) {
            log.error("Error executing task: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error executing task: " + e.getMessage());
        }
    }

    @GetMapping("/run-with-jobids")
    public ResponseEntity<String> runTask(@RequestParam("id") long id,@RequestParam("jobIds") List<String> jobIds) throws IOException {
        taskService.runTask(id, jobIds);
        return ResponseEntity.ok("Task executed successfully.");
    }
}
