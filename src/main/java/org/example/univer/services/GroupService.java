package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.models.Group;
import org.example.univer.repositories.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private final AppSettings appSettings;
    private Integer minLengthNameGroup;

    @PostConstruct
    public void init() {
        this.minLengthNameGroup = appSettings.getMinLengthNameGroup();
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Group group, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(group)) {
                    throw new InvalidParameterException(String.format("Невозможно создать группу! Группа с именем: %s уже существует",group.getName()));
                }
                if (group.getName().length() < minLengthNameGroup) {
                    throw new InvalidParameterException("Невозможно создать группу! Имя группы меньше заданных настроек!");
                }
            case METHOD_UPDATE:
                if (group.getName().length() < minLengthNameGroup) {
                    throw new InvalidParameterException("Невозможно обновить группу! Имя группы меньше заданных настроек!");
                }
            default:
                return "Контекст валидации отсутствует или неизвестен: " + context;
        }
    }

    public Group create(Group group) {
        logger.debug("Creating group: {}", group);
        validate(group, ValidationContext.METHOD_CREATE);
        return groupRepository.save(group);
    }

    public Group update(Group group) {
        logger.debug("Updating group: {}", group);
        validate(group, ValidationContext.METHOD_UPDATE);
        return groupRepository.save(group);
    }

    public void deleteById(Long id) {
        logger.debug("Delete group width id: {}", id);
        groupRepository.deleteById(id);
    }

    public Optional<Group> findById(Long id) {
        logger.debug("Find group width id: {}", id);
        return groupRepository.findById(id);
    }

    public List<Group> findAll() {
        logger.debug("Find all groups");
        return groupRepository.findAll();
    }

    public boolean isSingle(Group group) {
        logger.debug("Check group is single");
        return groupRepository.existsByName(group.getName());
    }

    public boolean existsById(Long id) {
        logger.debug("Check group is single");
        return groupRepository.existsById(id);
    }
}
