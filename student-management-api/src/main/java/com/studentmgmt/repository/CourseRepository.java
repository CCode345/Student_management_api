package com.studentmgmt.repository;

import com.studentmgmt.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCourseNameContainingIgnoreCase(String courseName);

    List<Course> findByCourseType(String courseType);

    @Query("SELECT c FROM Course c WHERE LOWER(c.topics) LIKE LOWER(CONCAT('%', :topic, '%'))")
    List<Course> findByTopicsContaining(@Param("topic") String topic);

    boolean existsByCourseName(String courseName);
}
