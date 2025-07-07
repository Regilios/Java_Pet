package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.StudentDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.StudentMapper;
import org.example.univer.models.Student;
import org.example.univer.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentRestController {
    private static final Logger logger = LoggerFactory.getLogger(StudentRestController.class);

    private final StudentService studentService;
    private final StudentMapper studentMapper;

    public StudentRestController(StudentService studentService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.studentMapper = studentMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<StudentDto> create(@RequestBody @Valid StudentDto dto) {
        Student saved = studentService.create(studentMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created student with id: {}", saved.getId());
        return ResponseEntity.created(location).body(studentMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAll() {
        List<StudentDto> result = studentService.findAll()
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getById(@PathVariable Long id) {
        return studentService.findById(id)
                .map(studentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<StudentDto> update(@PathVariable Long id,
                                              @RequestBody @Valid StudentDto dto) {
        if (!studentService.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id " + id);
        }
        dto.setId(id);
        try {
            Student updated = studentService.update(studentMapper.toEntity(dto));
            return ResponseEntity.ok(studentMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!studentService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentService.deleteById(id);
        logger.debug("Deleted student with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
