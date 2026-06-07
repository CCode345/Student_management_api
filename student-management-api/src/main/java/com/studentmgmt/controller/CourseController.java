package com.studentmgmt.controller;

import com.studentmgmt.dto.ApiResponse;
import com.studentmgmt.dto.CourseDTO;
import com.studentmgmt.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Course Management", description = "APIs for managing courses and enrollments")
public class CourseController {

    private final CourseService courseService;

    // ========================
    // ADMIN Operations
    // ========================

    @PostMapping
    @Operation(summary = "Create a new course (Admin only)", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<CourseDTO.Response>> createCourse(
            @Valid @RequestBody CourseDTO.Request request) {
        CourseDTO.Response response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a course (Admin only)", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<CourseDTO.Response>> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseDTO.Request request) {
        CourseDTO.Response response = courseService.updateCourse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a course (Admin only)", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(ApiResponse.success("Course deleted successfully"));
    }

    @PostMapping("/{courseId}/assign/{studentId}")
    @Operation(summary = "Assign a course to a student (Admin only)", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<Void>> assignCourseToStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        courseService.assignCourseToStudent(courseId, studentId);
        return ResponseEntity.ok(ApiResponse.success("Course assigned to student successfully"));
    }

    // ========================
    // STUDENT Operations
    // ========================

    @DeleteMapping("/{courseId}/leave/{studentId}")
    @Operation(summary = "Student leaves a course", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<Void>> leaveCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        courseService.removeCourseFromStudent(courseId, studentId);
        return ResponseEntity.ok(ApiResponse.success("Student has left the course successfully"));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get courses for a specific student", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<List<CourseDTO.Summary>>> getCoursesForStudent(
            @PathVariable Long studentId) {
        List<CourseDTO.Summary> courses = courseService.getCoursesForStudent(studentId);
        return ResponseEntity.ok(ApiResponse.success("Courses fetched successfully", courses));
    }

    // ========================
    // General / Search
    // ========================

    @GetMapping
    @Operation(summary = "Get all courses", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<List<CourseDTO.Response>>> getAllCourses() {
        return ResponseEntity.ok(ApiResponse.success("All courses fetched", courseService.getAllCourses()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<CourseDTO.Response>> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Course fetched", courseService.getCourseById(id)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search courses by name", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<List<CourseDTO.Response>>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success("Courses fetched", courseService.searchCoursesByName(name)));
    }

    @GetMapping("/search/topic")
    @Operation(summary = "Search courses by topic", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<List<CourseDTO.Response>>> searchByTopic(@RequestParam String topic) {
        return ResponseEntity.ok(ApiResponse.success("Courses fetched", courseService.searchCoursesByTopic(topic)));
    }

    @GetMapping("/type/{courseType}")
    @Operation(summary = "Get courses by type", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<ApiResponse<List<CourseDTO.Response>>> getByType(@PathVariable String courseType) {
        return ResponseEntity.ok(ApiResponse.success("Courses fetched", courseService.getCoursesByType(courseType)));
    }
}
