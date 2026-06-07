package com.studentmgmt.serviceimpl;

import com.studentmgmt.dto.AddressDTO;
import com.studentmgmt.dto.CourseDTO;
import com.studentmgmt.dto.StudentDTO;
import com.studentmgmt.exception.DuplicateResourceException;
import com.studentmgmt.exception.ResourceNotFoundException;
import com.studentmgmt.model.Address;
import com.studentmgmt.model.Student;
import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentDTO.Response addStudent(StudentDTO.CreateRequest request) {
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new DuplicateResourceException("Student with code '" + request.getStudentCode() + "' already exists");
        }

        Student student = Student.builder()
                .name(request.getName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .studentCode(request.getStudentCode())
                .build();

        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            List<Address> addresses = request.getAddresses().stream()
                    .map(addrReq -> mapToAddressEntity(addrReq, student))
                    .collect(Collectors.toList());
            student.setAddresses(addresses);
        }

        Student saved = studentRepository.save(student);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO.Response getStudentById(Long id) {
        Student student = findStudentById(id);
        return mapToResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO.Response getStudentByCode(String studentCode) {
        Student student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new ResourceNotFoundException("Student with code '" + studentCode + "' not found"));
        return mapToResponse(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO.Response> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO.Response updateStudentProfile(Long id, StudentDTO.UpdateRequest request) {
        Student student = findStudentById(id);

        if (request.getEmail() != null) {
            if (studentRepository.existsByEmail(request.getEmail()) &&
                !request.getEmail().equals(student.getEmail())) {
                throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already in use");
            }
            student.setEmail(request.getEmail());
        }
        if (request.getMobileNumber() != null) student.setMobileNumber(request.getMobileNumber());
        if (request.getFatherName() != null) student.setFatherName(request.getFatherName());
        if (request.getMotherName() != null) student.setMotherName(request.getMotherName());

        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            student.getAddresses().clear();
            List<Address> newAddresses = request.getAddresses().stream()
                    .map(addrReq -> mapToAddressEntity(addrReq, student))
                    .collect(Collectors.toList());
            student.getAddresses().addAll(newAddresses);
        }

        Student updated = studentRepository.save(student);
        return mapToResponse(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO.Response> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO.Response> getStudentsByCourseId(Long courseId) {
        return studentRepository.findStudentsByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO.Response> getStudentsByCourseName(String courseName) {
        return studentRepository.findStudentsByCourseName(courseName).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO.Response validateStudent(StudentDTO.ValidationRequest request) {
        Student student = studentRepository
                .findByStudentCodeAndDateOfBirth(request.getStudentCode(), request.getDateOfBirth())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid student code or date of birth"));
        return mapToResponse(student);
    }

    // -------- Helpers --------

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
    }

    private Address mapToAddressEntity(AddressDTO.Request req, Student student) {
        return Address.builder()
                .addressType(req.getAddressType())
                .street(req.getStreet())
                .city(req.getCity())
                .state(req.getState())
                .pincode(req.getPincode())
                .country(req.getCountry())
                .student(student)
                .build();
    }

    public StudentDTO.Response mapToResponse(Student student) {
        List<AddressDTO.Response> addressResponses = student.getAddresses() == null
                ? new ArrayList<>()
                : student.getAddresses().stream()
                    .map(addr -> AddressDTO.Response.builder()
                            .id(addr.getId())
                            .addressType(addr.getAddressType())
                            .street(addr.getStreet())
                            .city(addr.getCity())
                            .state(addr.getState())
                            .pincode(addr.getPincode())
                            .country(addr.getCountry())
                            .build())
                    .collect(Collectors.toList());

        List<CourseDTO.Summary> courseSummaries = student.getCourses() == null
                ? new ArrayList<>()
                : student.getCourses().stream()
                    .map(course -> CourseDTO.Summary.builder()
                            .id(course.getId())
                            .courseName(course.getCourseName())
                            .courseType(course.getCourseType())
                            .duration(course.getDuration())
                            .build())
                    .collect(Collectors.toList());

        return StudentDTO.Response.builder()
                .id(student.getId())
                .name(student.getName())
                .dateOfBirth(student.getDateOfBirth())
                .gender(student.getGender())
                .studentCode(student.getStudentCode())
                .email(student.getEmail())
                .mobileNumber(student.getMobileNumber())
                .fatherName(student.getFatherName())
                .motherName(student.getMotherName())
                .addresses(addressResponses)
                .courses(courseSummaries)
                .build();
    }
}
