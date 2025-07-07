package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.TeacherDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.TeacherMapper;
import org.example.univer.models.Teacher;
import org.example.univer.services.TeacherService;
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
@RequestMapping("/api/teachers")
public class TeacherRestController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherRestController.class);

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    public TeacherRestController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TeacherDto> create(@RequestBody @Valid TeacherDto dto) {
        Teacher saved = teacherService.create(teacherMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created teacher with id: {}", saved.getId());
        return ResponseEntity.created(location).body(teacherMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAll() {
        List<TeacherDto> result = teacherService.findAll()
                .stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getById(@PathVariable Long id) {
        return teacherService.findById(id)
                .map(teacherMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TeacherDto> update(@PathVariable Long id,
                                                 @RequestBody @Valid TeacherDto dto) {
        if (!teacherService.existsById(id)) {
            throw new ResourceNotFoundException("Teacher not found with id " + id);
        }
        dto.setId(id);
        try {
            Teacher updated = teacherService.update(teacherMapper.toEntity(dto));
            return ResponseEntity.ok(teacherMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!teacherService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        teacherService.deleteById(id);
        logger.debug("Deleted teacher with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
