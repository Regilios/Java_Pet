package org.example.univer.controllers.rest;

import jakarta.validation.Valid;
import org.example.univer.dto.GroupDto;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.ServiceException;
import org.example.univer.mappers.GroupMapper;
import org.example.univer.models.Group;
import org.example.univer.services.GroupService;
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
@RequestMapping("/api/groups")
public class GroupRestController {
    private static final Logger logger = LoggerFactory.getLogger(GroupRestController.class);

    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public GroupRestController(GroupService groupService, GroupMapper groupMapper) {
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<GroupDto> create(@RequestBody @Valid GroupDto dto) {
        Group saved = groupService.create(groupMapper.toEntity(dto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        logger.debug("Created group with id: {}", saved.getId());
        return ResponseEntity.created(location).body(groupMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAll() {
        List<GroupDto> result = groupService.findAll()
                .stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> getById(@PathVariable Long id) {
        return groupService.findById(id)
                .map(groupMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<GroupDto> update(@PathVariable Long id,
                                           @RequestBody @Valid GroupDto dto) {
        if (!groupService.existsById(id)) {
            throw new ResourceNotFoundException("Group not found with id " + id);
        }
        dto.setId(id);
        try {
            Group updated = groupService.update(groupMapper.toEntity(dto));
            return ResponseEntity.ok(groupMapper.toDto(updated));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!groupService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        groupService.deleteById(id);
        logger.debug("Deleted group with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
