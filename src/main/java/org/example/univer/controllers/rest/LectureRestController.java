package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.LectureDto;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.models.Lecture;
import org.example.univer.services.LectureService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lectures")
public class LectureRestController {
    private final LectureService lectureService;
    private final LectureMapper lectureMapper;

    public LectureRestController(LectureService lectureService, LectureMapper lectureMapper) {
        this.lectureService = lectureService;
        this.lectureMapper = lectureMapper;
    }

    @PostMapping
    public ResponseEntity<LectureDto> create(@RequestBody @Valid LectureDto dto) {
        Lecture entity = lectureMapper.toEntity(dto);
        lectureService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(lectureMapper.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<List<LectureDto>> getAll(Pageable pageable) {
        List<LectureDto> result = lectureService.findAllWithGroups(pageable)
                .stream()
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LectureDto> getById(@PathVariable Long id) {
        return lectureService.findEntityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LectureDto> update(@PathVariable Long id, @RequestBody @Valid LectureDto dto) {
        dto.setId(id);
        lectureService.update(lectureMapper.toEntity(dto));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        lectureService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}