package com.shanInfotech.springBootMicroservicesOwnerClient.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OwnerRecord(

        Integer ownerId,

        @NotBlank(message = "Owner name is required")
        @Size(min = 3, max = 50, message = "Owner name must be between 3 and 50 characters")
        String ownerName,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
        String ownerPhone,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String ownerEmail
) {}
