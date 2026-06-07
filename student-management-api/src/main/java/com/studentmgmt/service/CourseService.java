package com.studentmgmt.service;

import com.studentmgmt.dto.CourseDTO;

import java.util.List;

public interface CourseService {

    CourseDTO.Response createCourse(CourseDTO.Request request);

    CourseDTO.Response getCourseById(Long id);

    List<CourseDTO.Response> getAllCourses();

    CourseDTO.Response updateCourse(Long id, CourseDTO.Request request);

    void deleteCourse(Long id);

    void assignCourseToStudent(Long courseId, Long studentId);

    void removeCourseFromStudent(Long courseId, Long studentId);

    List<CourseDTO.Response> searchCoursesByName(String name);

    List<CourseDTO.Response> searchCoursesByTopic(String topic);

    List<CourseDTO.Response> getCoursesByType(String courseType);

    List<CourseDTO.Summary> getCoursesForStudent(Long studentId);
}
