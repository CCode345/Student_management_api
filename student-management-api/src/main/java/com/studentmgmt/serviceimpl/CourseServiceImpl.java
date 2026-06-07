package com.studentmgmt.serviceimpl;

import com.studentmgmt.dto.CourseDTO;
import com.studentmgmt.exception.DuplicateResourceException;
import com.studentmgmt.exception.ResourceNotFoundException;
import com.studentmgmt.model.Course;
import com.studentmgmt.model.Student;
import com.studentmgmt.repository.CourseRepository;
import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public CourseDTO.Response createCourse(CourseDTO.Request request) {
        Course course = Course.builder()
                .courseName(request.getCourseName())
                .description(request.getDescription())
                .courseType(request.getCourseType())
                .duration(request.getDuration())
                .topics(request.getTopics())
                .build();
        Course saved = courseRepository.save(course);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO.Response getCourseById(Long id) {
        return mapToResponse(findCourseById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO.Response> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO.Response updateCourse(Long id, CourseDTO.Request request) {
        Course course = findCourseById(id);
        if (request.getCourseName() != null) course.setCourseName(request.getCourseName());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getCourseType() != null) course.setCourseType(request.getCourseType());
        if (request.getDuration() != null) course.setDuration(request.getDuration());
        if (request.getTopics() != null) course.setTopics(request.getTopics());
        return mapToResponse(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course with id " + id + " not found");
        }
        courseRepository.deleteById(id);
    }

    @Override
    public void assignCourseToStudent(Long courseId, Long studentId) {
        Course course = findCourseById(courseId);
        Student student = findStudentById(studentId);
        if (student.getCourses().contains(course)) {
            throw new DuplicateResourceException("Student is already enrolled in this course");
        }
        student.getCourses().add(course);
        studentRepository.save(student);
    }

    @Override
    public void removeCourseFromStudent(Long courseId, Long studentId) {
        Course course = findCourseById(courseId);
        Student student = findStudentById(studentId);
        if (!student.getCourses().contains(course)) {
            throw new ResourceNotFoundException("Student is not enrolled in this course");
        }
        student.getCourses().remove(course);
        studentRepository.save(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO.Response> searchCoursesByName(String name) {
        return courseRepository.findByCourseNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO.Response> searchCoursesByTopic(String topic) {
        return courseRepository.findByTopicsContaining(topic).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO.Response> getCoursesByType(String courseType) {
        return courseRepository.findByCourseType(courseType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO.Summary> getCoursesForStudent(Long studentId) {
        Student student = findStudentById(studentId);
        return student.getCourses().stream()
                .map(course -> CourseDTO.Summary.builder()
                        .id(course.getId())
                        .courseName(course.getCourseName())
                        .courseType(course.getCourseType())
                        .duration(course.getDuration())
                        .build())
                .collect(Collectors.toList());
    }

    // -------- Helpers --------

    private Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id " + id + " not found"));
    }

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
    }

    private CourseDTO.Response mapToResponse(Course course) {
        return CourseDTO.Response.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .description(course.getDescription())
                .courseType(course.getCourseType())
                .duration(course.getDuration())
                .topics(course.getTopics())
                .enrolledStudents(course.getStudents() != null ? course.getStudents().size() : 0)
                .build();
    }
}
