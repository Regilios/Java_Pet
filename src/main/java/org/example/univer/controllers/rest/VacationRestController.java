package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.VacationDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.VacationMapper;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.example.univer.services.VacationService;
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
@RequestMapping("/api/vacations")
public class VacationRestController {
    private static final Logger logger = LoggerFactory.getLogger(VacationRestController.class);

    private final VacationService vacationService;
    private final VacationMapper vacationMapper;

    public VacationRestController(VacationService vacationService, VacationMapper vacationMapper) {
        this.vacationService = vacationService;
        this.vacationMapper = vacationMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<VacationDto> create(@RequestBody @Valid VacationDto dto) {
        Teacher teacher = vacationService.findTeacherById(dto.getTeacherId());
        Vacation vacation = vacationMapper.toEntity(dto);
        vacation.setTeacher(teacher);

        Vacation saved = vacationService.create(vacation);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created vacation with id: {}", saved.getId());
        return ResponseEntity.created(location).body(vacationMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<VacationDto>> getAll() {
        List<VacationDto> result = vacationService.findAll()
                .stream()
                .map(vacationMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacationDto> getById(@PathVariable Long id) {
        return vacationService.findById(id)
                .map(vacationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<VacationDto> update(@PathVariable Long id,
                                                 @RequestBody @Valid VacationDto dto) {
        if (!vacationService.existsById(id)) {
            throw new ResourceNotFoundException("Vacation not found with id " + id);
        }
        dto.setId(id);
        try {
            Vacation updated = vacationService.update(vacationMapper.toEntity(dto));
            return ResponseEntity.ok(vacationMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!vacationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vacationService.deleteById(id);
        logger.debug("Deleted vacation with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
