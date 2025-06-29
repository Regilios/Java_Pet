package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.AudienceDto;
import org.example.univer.exeption.AudienceExeption;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.AudienceMapper;
import org.example.univer.models.Audience;
import org.example.univer.services.AudienceService;
import org.example.univer.util.ErrorResponse;
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
@RequestMapping("/api/audiences")
public class AudienceRestController {

    private static final Logger logger = LoggerFactory.getLogger(AudienceRestController.class);

    private final AudienceService audienceService;
    private final AudienceMapper audienceMapper;

    public AudienceRestController(AudienceService audienceService, AudienceMapper audienceMapper) {
        this.audienceService = audienceService;
        this.audienceMapper = audienceMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AudienceDto> create(@RequestBody @Valid AudienceDto dto) {
        Audience saved = audienceService.create(audienceMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created audience with id: {}", saved.getId());
        return ResponseEntity.created(location).body(audienceMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<AudienceDto>> getAll() {
        List<AudienceDto> result = audienceService.findAll()
                .stream()
                .map(audienceMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AudienceDto> getById(@PathVariable Long id) {
        return audienceService.findById(id)
                .map(audienceMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new AudienceExeption("Audience not found with id " + id));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<AudienceDto> update(@PathVariable Long id,
                                              @RequestBody @Valid AudienceDto dto) {
        if (!audienceService.existsById(id)) {
            throw new ResourceNotFoundException("Audience not found with id " + id);
        }
        dto.setId(id);
        try {
            Audience updated = audienceService.update(audienceMapper.toEntity(dto));
            return ResponseEntity.ok(audienceMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!audienceService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        audienceService.deleteById(id);
        logger.debug("Deleted audience with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler // пример своей реализации
    private ResponseEntity<ErrorResponse> handleException(AudienceExeption e) {
        ErrorResponse response = new ErrorResponse(
            "Аудитория не найдена",
               e.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
