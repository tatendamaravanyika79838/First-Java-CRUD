
package com.example.firstjavacrud.student;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get one student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a student
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
    if (student.getName() == null || student.getName().isBlank()
            || student.getEmail() == null || student.getEmail().isBlank()) {
        return ResponseEntity.badRequest().build();
    }

    student.setId(null);
    return ResponseEntity.ok(studentRepository.save(student));
    }

   // Update a student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable Long id,
            @RequestBody Student updatedStudent) {

        if (updatedStudent.getName() == null
                || updatedStudent.getName().isBlank()
                || updatedStudent.getEmail() == null
                || updatedStudent.getEmail().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setEmail(updatedStudent.getEmail());
                    return ResponseEntity.ok(studentRepository.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    
    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
