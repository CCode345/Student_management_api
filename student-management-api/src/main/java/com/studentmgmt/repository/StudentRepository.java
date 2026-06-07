package com.studentmgmt.repository;

import com.studentmgmt.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentCode(String studentCode);

    boolean existsByStudentCode(String studentCode);

    boolean existsByEmail(String email);

    List<Student> findByNameContainingIgnoreCase(String name);

    Optional<Student> findByStudentCodeAndDateOfBirth(String studentCode, LocalDate dateOfBirth);

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s FROM Student s JOIN s.courses c WHERE LOWER(c.courseName) LIKE LOWER(CONCAT('%', :courseName, '%'))")
    List<Student> findStudentsByCourseName(@Param("courseName") String courseName);
}
