package com.studentmgmt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class CourseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Course name is required")
        private String courseName;

        private String description;
        private String courseType;
        private String duration;
        private String topics;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String courseName;
        private String description;
        private String courseType;
        private String duration;
        private String topics;
        private int enrolledStudents;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Summary {
        private Long id;
        private String courseName;
        private String courseType;
        private String duration;
    }
}
