package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.LectureTimeDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.LectureTimeMapper;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
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
@RequestMapping("/api/lecturetimes")
public class LectureTimeRestController {
    private static final Logger logger = LoggerFactory.getLogger(LectureTimeRestController.class);

    private final LectureTimeService lectureTimeService;
    private final LectureTimeMapper lectureTimeMapper;

    public LectureTimeRestController(LectureTimeService lectureTimeService, LectureTimeMapper lectureTimeMapper) {
        this.lectureTimeService = lectureTimeService;
        this.lectureTimeMapper = lectureTimeMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<LectureTimeDto> create(@RequestBody @Valid LectureTimeDto dto) {
        LectureTime saved = lectureTimeService.create(lectureTimeMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created lectureTime with id: {}", saved.getId());
        return ResponseEntity.created(location).body(lectureTimeMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<LectureTimeDto>> getAll() {
        List<LectureTimeDto> result = lectureTimeService.findAll()
                .stream()
                .map(lectureTimeMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureTimeDto> getById(@PathVariable Long id) {
        return lectureTimeService.findById(id)
                .map(lectureTimeMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<LectureTimeDto> update(@PathVariable Long id,
                                           @RequestBody @Valid LectureTimeDto dto) {
        if (!lectureTimeService.existsById(id)) {
            throw new ResourceNotFoundException("LectureTime not found with id " + id);
        }
        dto.setId(id);
        try {
            LectureTime updated = lectureTimeService.update(lectureTimeMapper.toEntity(dto));
            return ResponseEntity.ok(lectureTimeMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!lectureTimeService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        lectureTimeService.deleteById(id);
        logger.debug("Deleted lectureTime with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
