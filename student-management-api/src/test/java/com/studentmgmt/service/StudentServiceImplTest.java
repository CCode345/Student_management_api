package com.studentmgmt.service;

import com.studentmgmt.dto.StudentDTO;
import com.studentmgmt.exception.DuplicateResourceException;
import com.studentmgmt.exception.ResourceNotFoundException;
import com.studentmgmt.model.Student;
import com.studentmgmt.repository.StudentRepository;
import com.studentmgmt.serviceimpl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .name("Chandan Kumar Ram")
                .dateOfBirth(LocalDate.of(1999, 6, 2))
                .gender("Male")
                .studentCode("STU001")
                .email("chandan@gmail.com")
                .build();
    }

    @Test
    void addStudent_ShouldCreateStudent_WhenValidRequest() {
        StudentDTO.CreateRequest request = StudentDTO.CreateRequest.builder()
                .name("Chandan Kumar Ram")
                .dateOfBirth(LocalDate.of(1999, 6, 2))
                .gender("Male")
                .studentCode("STU001")
                .build();

        when(studentRepository.existsByStudentCode("STU001")).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO.Response response = studentService.addStudent(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Chandan Kumar Ram");
        assertThat(response.getStudentCode()).isEqualTo("STU001");
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void addStudent_ShouldThrowException_WhenDuplicateStudentCode() {
        StudentDTO.CreateRequest request = StudentDTO.CreateRequest.builder()
                .name("Test Student")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender("Male")
                .studentCode("STU001")
                .build();

        when(studentRepository.existsByStudentCode("STU001")).thenReturn(true);

        assertThatThrownBy(() -> studentService.addStudent(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("STU001");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void getStudentById_ShouldReturnStudent_WhenExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO.Response response = studentService.getStudentById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Chandan Kumar Ram");
    }

    @Test
    void getStudentById_ShouldThrowException_WhenNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllStudents_ShouldReturnAllStudents() {
        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<StudentDTO.Response> responses = studentService.getAllStudents();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo("Chandan Kumar Ram");
    }

    @Test
    void searchStudentsByName_ShouldReturnMatchingStudents() {
        when(studentRepository.findByNameContainingIgnoreCase("chandan")).thenReturn(List.of(student));

        List<StudentDTO.Response> responses = studentService.searchStudentsByName("chandan");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).containsIgnoringCase("chandan");
    }

    @Test
    void validateStudent_ShouldReturnStudent_WhenValid() {
        StudentDTO.ValidationRequest request = new StudentDTO.ValidationRequest("STU001", LocalDate.of(1999, 6, 2));

        when(studentRepository.findByStudentCodeAndDateOfBirth("STU001", LocalDate.of(1999, 6, 2)))
                .thenReturn(Optional.of(student));

        StudentDTO.Response response = studentService.validateStudent(request);

        assertThat(response).isNotNull();
        assertThat(response.getStudentCode()).isEqualTo("STU001");
    }

    @Test
    void validateStudent_ShouldThrowException_WhenInvalidCredentials() {
        StudentDTO.ValidationRequest request = new StudentDTO.ValidationRequest("WRONG", LocalDate.of(2002, 1, 1));

        when(studentRepository.findByStudentCodeAndDateOfBirth(anyString(), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.validateStudent(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Invalid student code or date of birth");
    }

    @Test
    void deleteStudent_ShouldDelete_WhenExists() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void deleteStudent_ShouldThrow_WhenNotFound() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(studentRepository, never()).deleteById(any());
    }
}
