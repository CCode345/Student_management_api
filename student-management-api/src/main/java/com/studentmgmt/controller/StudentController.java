package com.studentmgmt.controller;

import com.studentmgmt.dto.ApiResponse;
import com.studentmgmt.dto.StudentDTO;
import com.studentmgmt.service.StudentService;
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
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "APIs for managing students")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final StudentService studentService;

    // ADMIN Operations

    @PostMapping
    @Operation(summary = "Add a new student (Admin only)")
    public ResponseEntity<ApiResponse<StudentDTO.Response>> addStudent(
            @Valid @RequestBody StudentDTO.CreateRequest request) {

        StudentDTO.Response response = studentService.addStudent(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student added successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return ResponseEntity.ok(
                ApiResponse.success("Student deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name (Admin only)")
    public ResponseEntity<ApiResponse<List<StudentDTO.Response>>> searchByName(
            @RequestParam String name) {

        List<StudentDTO.Response> students =
                studentService.searchStudentsByName(name);

        return ResponseEntity.ok(
                ApiResponse.success("Students fetched successfully", students));
    }

    @GetMapping("/by-course/{courseId}")
    @Operation(summary = "Get students assigned to a course (Admin only)")
    public ResponseEntity<ApiResponse<List<StudentDTO.Response>>> getStudentsByCourse(
            @PathVariable Long courseId) {

        List<StudentDTO.Response> students =
                studentService.getStudentsByCourseId(courseId);

        return ResponseEntity.ok(
                ApiResponse.success("Students fetched successfully", students));
    }

    @GetMapping("/by-course-name")
    @Operation(summary = "Get students by course name (Admin only)")
    public ResponseEntity<ApiResponse<List<StudentDTO.Response>>> getStudentsByCourseName(
            @RequestParam String courseName) {

        List<StudentDTO.Response> students =
                studentService.getStudentsByCourseName(courseName);

        return ResponseEntity.ok(
                ApiResponse.success("Students fetched successfully", students));
    }

    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<ApiResponse<List<StudentDTO.Response>>> getAllStudents() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "All students fetched",
                        studentService.getAllStudents()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<ApiResponse<StudentDTO.Response>> getStudentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Student fetched",
                        studentService.getStudentById(id)));
    }

    @GetMapping("/code/{studentCode}")
    @Operation(summary = "Get student by student code")
    public ResponseEntity<ApiResponse<StudentDTO.Response>> getStudentByCode(
            @PathVariable String studentCode) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Student fetched",
                        studentService.getStudentByCode(studentCode)));
    }

    // STUDENT Self Operations

    @PutMapping("/{id}/profile")
    @Operation(summary = "Update student profile (Student self-update)")
    public ResponseEntity<ApiResponse<StudentDTO.Response>> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO.UpdateRequest request) {

        StudentDTO.Response updated =
                studentService.updateStudentProfile(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile updated successfully",
                        updated));
    }

    // PUBLIC API - No JWT Required

    @PostMapping("/validate")
    @Operation(
            summary = "Validate student identity using student_code and date_of_birth",
            security = {}
    )
    public ResponseEntity<ApiResponse<StudentDTO.Response>> validateStudent(
            @Valid @RequestBody StudentDTO.ValidationRequest request) {

        StudentDTO.Response response =
                studentService.validateStudent(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Student validated successfully",
                        response));
    }
}