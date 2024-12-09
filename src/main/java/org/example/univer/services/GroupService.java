package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoGroupInterface;
import org.example.univer.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private DaoGroupInterface daoGroupInterface;
    private static final Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Value("#{${minLengthNameAudience}}")
    private Integer minLengthNameAudience;

    @Autowired
    public GroupService(DaoGroupInterface daoGroupInterface) {
        this.daoGroupInterface = daoGroupInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Group group, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(group)) {
                    throw new IllegalArgumentException("Невозможно создать группу! Группа с именем: " + group.getName() + " уже существует");
                }
                if (group.getName().length() < minLengthNameAudience) {
                    throw new IllegalArgumentException("Невозможно создать группу! Имя группы меньше заданных настроек!");
                }
            case METHOD_UPDATE:
                if (group.getName().length() < minLengthNameAudience) {
                    throw new IllegalArgumentException("Невозможно обновить группу! Имя группы меньше заданных настроек!");
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
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Group group) {
        logger.debug("Start update Group");
        try {
            validate(group, ValidationContext.METHOD_UPDATE);
            daoGroupInterface.update(group);
            logger.debug("Group updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete group width id: {}", id);
        daoGroupInterface.deleteById(id);
    }

    public Group findById(Long id) {
        logger.debug("Find group width id: {}", id);
        return daoGroupInterface.findById(id);
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
