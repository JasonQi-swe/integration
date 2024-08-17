package com.example.integration.controller;

import com.example.integration.entity.Tenant;
import com.example.integration.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public List<Tenant> getAllTenants() {
        return tenantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable Long id) {
        Optional<Tenant> tenant = tenantService.findById(id);
        return tenant.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@RequestBody Tenant tenant) {
        Tenant savedTenant = tenantService.save(tenant);
        return ResponseEntity.ok(savedTenant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tenant> updateTenant(@PathVariable Long id, @RequestBody Tenant tenantDetails) {
        Optional<Tenant> tenantOptional = tenantService.findById(id);
        if (tenantOptional.isPresent()) {
            Tenant tenantToUpdate = tenantOptional.get();
            tenantToUpdate.setEmail(tenantDetails.getEmail());
            tenantToUpdate.setUserName(tenantDetails.getUserName());
            tenantToUpdate.setSummary(tenantDetails.getSummary());
            tenantToUpdate.setCv(tenantDetails.getCv());
            tenantToUpdate.setSearchingKeyWords(tenantDetails.getSearchingKeyWords());
            tenantToUpdate.setScreeningSkills(tenantDetails.getScreeningSkills());
            tenantToUpdate.setContainingSkills(tenantDetails.getContainingSkills());
            tenantToUpdate.setTotalTargetJobNumber(tenantDetails.getTotalTargetJobNumber());
            tenantToUpdate.setAddedDateTime(tenantDetails.getAddedDateTime());
            tenantToUpdate.setLanguageName(tenantDetails.getLanguageName());
            tenantToUpdate.setLocation(tenantDetails.getLocation());
            return ResponseEntity.ok(tenantService.save(tenantToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        if (tenantService.findById(id).isPresent()) {
            tenantService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
