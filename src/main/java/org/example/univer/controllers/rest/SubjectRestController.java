package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.SubjectDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.SubjectMapper;
import org.example.univer.models.Subject;
import org.example.univer.services.SubjectService;
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
@RequestMapping("/api/subjects")
public class SubjectRestController {
    private static final Logger logger = LoggerFactory.getLogger(SubjectRestController.class);

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    public SubjectRestController(SubjectService subjectService, SubjectMapper subjectMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<SubjectDto> create(@RequestBody @Valid SubjectDto dto) {
        Subject saved = subjectService.create(subjectMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created subject with id: {}", saved.getId());
        return ResponseEntity.created(location).body(subjectMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getAll() {
        List<SubjectDto> result = subjectService.findAll()
                .stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDto> getById(@PathVariable Long id) {
        return subjectService.findById(id)
                .map(subjectMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<SubjectDto> update(@PathVariable Long id,
                                           @RequestBody @Valid SubjectDto dto) {
        if (!subjectService.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with id " + id);
        }
        dto.setId(id);
        try {
            Subject updated = subjectService.update(subjectMapper.toEntity(dto));
            return ResponseEntity.ok(subjectMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!subjectService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        subjectService.deleteById(id);
        logger.debug("Deleted subject with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
