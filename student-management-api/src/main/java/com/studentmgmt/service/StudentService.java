package com.studentmgmt.service;

import com.studentmgmt.dto.StudentDTO;

import java.util.List;

public interface StudentService {

    StudentDTO.Response addStudent(StudentDTO.CreateRequest request);

    StudentDTO.Response getStudentById(Long id);

    StudentDTO.Response getStudentByCode(String studentCode);

    List<StudentDTO.Response> getAllStudents();

    StudentDTO.Response updateStudentProfile(Long id, StudentDTO.UpdateRequest request);

    void deleteStudent(Long id);

    List<StudentDTO.Response> searchStudentsByName(String name);

    List<StudentDTO.Response> getStudentsByCourseId(Long courseId);

    List<StudentDTO.Response> getStudentsByCourseName(String courseName);

    StudentDTO.Response validateStudent(StudentDTO.ValidationRequest request);
}
