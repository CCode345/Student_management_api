package com.studentmgmt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class StudentDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotNull(message = "Date of birth is required")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;

        @NotBlank(message = "Gender is required")
        private String gender;

        @NotBlank(message = "Student code is required")
        private String studentCode;

        private List<AddressDTO.Request> addresses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String email;
        private String mobileNumber;
        private String fatherName;
        private String motherName;
        private List<AddressDTO.Request> addresses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;
        private String gender;
        private String studentCode;
        private String email;
        private String mobileNumber;
        private String fatherName;
        private String motherName;
        private List<AddressDTO.Response> addresses;
        private List<CourseDTO.Summary> courses;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ValidationRequest {
        @NotBlank(message = "Student code is required")
        private String studentCode;

        @NotNull(message = "Date of birth is required")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;
    }
}
