package com.studentmgmt.config;

import com.studentmgmt.model.Address;
import com.studentmgmt.model.Address.AddressType;
import com.studentmgmt.model.Course;
import com.studentmgmt.model.Student;
import com.studentmgmt.repository.CourseRepository;
import com.studentmgmt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) {
        log.info("Seeding initial data...");

        // Create Courses
        Course java = courseRepository.save(Course.builder()
                .courseName("Java Programming")
                .description("Core and advanced Java programming")
                .courseType("Technical")
                .duration("3 months")
                .topics("OOP, Collections, Streams, Spring")
                .build());

        Course web = courseRepository.save(Course.builder()
                .courseName("Web Development")
                .description("Full stack web development")
                .courseType("Technical")
                .duration("4 months")
                .topics("HTML, CSS, JavaScript, React, Spring Boot")
                .build());

        Course ds = courseRepository.save(Course.builder()
                .courseName("Data Science")
                .description("Introduction to data science and ML")
                .courseType("Analytics")
                .duration("6 months")
                .topics("Python, Pandas, NumPy, Machine Learning, Deep Learning")
                .build());

        // Create Students
        Student s1 = Student.builder()
                .name("Chandan Kumar Ram")
                .dateOfBirth(LocalDate.of(2000, 5, 15))
                .gender("Male")
                .studentCode("STU001")
                .email("chandan@gamil.com")
                .mobileNumber("1254565400")
                .fatherName("Premchand Ram")
                .motherName("Rita Devi")
                .build();
        Address addr1 = Address.builder()
                .addressType(AddressType.PERMANENT)
                .street("12, MG Road")
                .city("Bengaluru")
                .state("Karnataka")
                .pincode("560001")
                .country("India")
                .student(s1)
                .build();
        s1.setAddresses(List.of(addr1));
        s1.getCourses().add(java);
        s1.getCourses().add(web);
        studentRepository.save(s1);

        Student s2 = Student.builder()
                .name("Rahul Raj")
                .dateOfBirth(LocalDate.of(2001, 8, 22))
                .gender("Male")
                .studentCode("STU002")
                .email("rahul@gmail.com")
                .mobileNumber("1547854989")
                .fatherName("Ramesh Raj")
                .motherName("Meena Raj")
                .build();
        Address addr2 = Address.builder()
                .addressType(AddressType.CURRENT)
                .street("45, Brigade Road")
                .city("Bengaluru")
                .state("Karnataka")
                .pincode("560025")
                .country("India")
                .student(s2)
                .build();
        s2.setAddresses(List.of(addr2));
        s2.getCourses().add(ds);
        studentRepository.save(s2);

        log.info("Data seeding complete. Students: {}, Courses: {}", studentRepository.count(), courseRepository.count());
    }
}
