package com.example.integration.service;

import com.example.integration.entity.Tenant;
import com.example.integration.exception.exceptions.NotFoundException;
import com.example.integration.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    public Optional<Tenant> findById(Long id) {
        return tenantRepository.findById(id);
    }

    public Tenant save(Tenant tenant) {
        tenant.setAddedDateTime(LocalDateTime.now());
        return tenantRepository.save(tenant);
    }

    public void deleteById(Long id) {
        if (!tenantRepository.existsById(id)) {
            throw new NotFoundException("Could not delete the tenant. Tenant not found with id: " + id);
        }
        tenantRepository.deleteById(id);
    }
}
