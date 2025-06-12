package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.HolidayDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.HolidayMapper;
import org.example.univer.models.Holiday;
import org.example.univer.services.HolidayService;
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
@RequestMapping("/api/holidays")
public class HolidayRestController {
    private static final Logger logger = LoggerFactory.getLogger(AudienceRestController.class);

    private final HolidayService holidayService;
    private final HolidayMapper holidayMapper;

    public HolidayRestController(HolidayService holidayService, HolidayMapper holidayMapper) {
        this.holidayService = holidayService;
        this.holidayMapper = holidayMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<HolidayDto> create(@RequestBody @Valid HolidayDto dto) {
        Holiday saved = holidayService.create(holidayMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created holiday with id: {}", saved.getId());
        return ResponseEntity.created(location).body(holidayMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<HolidayDto>> getAll() {
        List<HolidayDto> result = holidayService.findAll()
                .stream()
                .map(holidayMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HolidayDto> getById(@PathVariable Long id) {
        return holidayService.findById(id)
                .map(holidayMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<HolidayDto> update(@PathVariable Long id,
                                           @RequestBody @Valid HolidayDto dto) {
        if (!holidayService.existsById(id)) {
            throw new ResourceNotFoundException("Holiday not found with id " + id);
        }
        dto.setId(id);
        try {
            Holiday updated = holidayService.update(holidayMapper.toEntity(dto));
            return ResponseEntity.ok(holidayMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!holidayService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        holidayService.deleteById(id);
        logger.debug("Deleted holiday with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
