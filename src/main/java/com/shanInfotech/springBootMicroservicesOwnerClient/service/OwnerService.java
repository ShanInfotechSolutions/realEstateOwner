package com.shanInfotech.springBootMicroservicesOwnerClient.service;

import org.springframework.stereotype.Service;

import com.shanInfotech.springBootMicroservicesOwnerClient.entity.Owner;
import com.shanInfotech.springBootMicroservicesOwnerClient.records.OwnerRecord;
import com.shanInfotech.springBootMicroservicesOwnerClient.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    
// constructor based injection
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    // Convert entity to record
    private OwnerRecord toRecord(Owner owner) {
        return new OwnerRecord(
            owner.getOwnerId(),
            owner.getOwnerName(),
            owner.getOwnerPhone(),
            owner.getOwnerEmail()
        );
    }

    // Convert record to entity
    private Owner toEntity(OwnerRecord record) {
        Owner owner = new Owner();
        // For updates, set ID if present
        if (record.ownerId() != null) {
            owner.setOwnerId(record.ownerId());
        }
        owner.setOwnerName(record.ownerName());
        owner.setOwnerPhone(record.ownerPhone());
        owner.setOwnerEmail(record.ownerEmail());
        return owner;
    }

    public List<OwnerRecord> getAllOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(this::toRecord)
                .collect(Collectors.toList());
    }

    public Optional<OwnerRecord> getOwnerById(Integer id) {
        return ownerRepository.findById(id)
                .map(this::toRecord);
    }

    public OwnerRecord createOwner(OwnerRecord ownerRecord) {
        Owner owner = toEntity(ownerRecord);
        Owner saved = ownerRepository.save(owner);
        return toRecord(saved);
    }

    public Optional<OwnerRecord> updateOwner(Integer id, OwnerRecord ownerRecord) {
        return ownerRepository.findById(id).map(existingOwner -> {
            existingOwner.setOwnerName(ownerRecord.ownerName());
            existingOwner.setOwnerPhone(ownerRecord.ownerPhone());
            existingOwner.setOwnerEmail(ownerRecord.ownerEmail());
            Owner updated = ownerRepository.save(existingOwner);
            return toRecord(updated);
        });
    }

    public boolean deleteOwner(Integer id) {
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

