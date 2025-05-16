package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoGroupInterface;
import org.example.univer.exeption.*;
import org.example.univer.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private DaoGroupInterface daoGroupInterface;
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);
    private AppSettings appSettings;
    private Integer minLengthNameGroup;

    @Autowired
    public GroupService(DaoGroupInterface daoGroupInterface, AppSettings appSettings) {
        this.daoGroupInterface = daoGroupInterface;
        this.appSettings = appSettings;
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
                    throw new InvalidParameterException("Невозможно создать группу! Группа с именем: " + group.getName() + " уже существует");
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

    public void create(Group group) {
        logger.debug("Start create Group");
        try {
            validate(group, ValidationContext.METHOD_CREATE);
            daoGroupInterface.create(group);
            logger.debug("Group created");
        } catch (GroupExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта группы: {}", e.getMessage(), e);
            throw new NullEntityException("Объект группы не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта праздника: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта группы", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект группы не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта группы", e);
        }
    }

    public void update(Group group) {
        logger.debug("Start update Group");
        try {
            validate(group, ValidationContext.METHOD_UPDATE);
            daoGroupInterface.update(group);
            logger.debug("Group updated");
        } catch (GroupExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта группы: {}", e.getMessage(), e);
            throw new NullEntityException("Объект группы не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта праздника: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта группы", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект группы не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта группы", e);
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete group width id: {}", id);
        daoGroupInterface.deleteById(id);
    }

    public Optional<Group> findById(Long id) {
        logger.debug("Find group width id: {}", id);
        return daoGroupInterface.findById(id);
    }

    public List<Group> getGroupById(List<Long> groupIds) {
        logger.debug("Find groups by id");
        return daoGroupInterface.getGroupById(groupIds);
    }

    public List<Group> findAll() {
        logger.debug("Find all groups");
        return daoGroupInterface.findAll();
    }

    public boolean isSingle(Group group) {
        logger.debug("Check group is single");
        return daoGroupInterface.isSingle(group);
    }
}
