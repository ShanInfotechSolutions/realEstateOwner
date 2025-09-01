package com.shanInfotech.springBootMicroservicesOwnerClient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shanInfotech.springBootMicroservicesOwnerClient.records.OwnerRecord;
import com.shanInfotech.springBootMicroservicesOwnerClient.service.OwnerService;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/owners")
public class OwnerController {

	@Autowired
    private OwnerService ownerService;

	@GetMapping("/health")
	  public Map<String,String> health() { 
		return Map.of("status", "UP"); 
		}
	
    @GetMapping
    public ResponseEntity<List<OwnerRecord>> getAllOwners() {
        List<OwnerRecord> owners = ownerService.getAllOwners();
        return ResponseEntity.ok(owners);
    }

    // GET /owners/{id} — get owner by id
    @GetMapping("/{id}")
    public ResponseEntity<OwnerRecord> getOwnerById(@PathVariable Integer id) {
        return ownerService.getOwnerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /owners — create a new owner
    @PostMapping
    public ResponseEntity<OwnerRecord> createOwner(@RequestBody OwnerRecord ownerRecord) {
        OwnerRecord created = ownerService.createOwner(ownerRecord);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

//    @PostMapping(value = "/echo", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
//    public ResponseEntity<String> createOwner(@Valid @RequestBody String body) {
//        try {
//            String[] parts = body.split(",");
//            if (parts.length != 3) {
//                return ResponseEntity.badRequest().body("Invalid format. Expected: name, phone, email");
//            }
//            String name = parts[0].trim();
//            String phone = parts[1].trim();
//            String email = parts[2].trim();
//
//            OwnerRecord ownerRecord = new OwnerRecord(null, name, phone, email);
//            ownerService.createOwner(ownerRecord);
//
//            return ResponseEntity.ok("Owner saved: " + name + ", " + phone + ", " + email);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
//        }
//    }
    // PUT /owners/{id} — update an existing owner
    @PutMapping("/{id}")
    public ResponseEntity<OwnerRecord> updateOwner(@PathVariable Integer id, @RequestBody OwnerRecord ownerRecord) {
        return ownerService.updateOwner(id, ownerRecord)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /owners/{id} — delete owner by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Integer id) {
        boolean deleted = ownerService.deleteOwner(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

