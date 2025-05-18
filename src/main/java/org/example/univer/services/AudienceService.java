package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.exeption.*;
import org.example.univer.models.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AudienceService {
    private DaoAudienceInterface daoAudienceInterfaces;
    private static final Logger logger = LoggerFactory.getLogger(AudienceService.class);
    private AppSettings appSettings;
    @Autowired
    public AudienceService(DaoAudienceInterface daoAudienceInterfaces, AppSettings appSettings) {
        this.daoAudienceInterfaces = daoAudienceInterfaces;
        this.appSettings = appSettings;
    }
    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Audience audience, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(audience)) {
                    throw new InvalidParameterException("Невозможно создать аудиенцию! Аудиенция с номером: " + audience.getRoomNumber() + " уже существует");
                }
                validateCommon(audience, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(audience, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Audience audience, String action) {
        if (audience.getCapacity() < appSettings.getRoomSettings().getSizeMin() || audience.getCapacity() > appSettings.getRoomSettings().getSizeMax()) {
            throw new AudienceExeption("Невозможно " + action + " аудиенцию! Размер аудиенции не попадает в рамки критериев!");
        }
    }

    public void create(Audience audience) {
        logger.debug("Start create Audience");
        try {
            validate(audience, ValidationContext.METHOD_CREATE);
            daoAudienceInterfaces.create(audience);
            logger.debug("Audience created");
        } catch (AudienceExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта аудиенции: {}", e.getMessage(), e);
            throw new NullEntityException("Объект аудиенции не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта аудиенции: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта аудиенции", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект аудиенции не найдена", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта аудиенции", e);
        }
    }

    public void update(Audience audience) {
        logger.debug("Start update Audience");
        try {
            validate(audience, ValidationContext.METHOD_UPDATE);
            daoAudienceInterfaces.update(audience);
            logger.debug("Audience updated");
        } catch (AudienceExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта аудиенции: {}", e.getMessage(), e);
            throw new NullEntityException("Объект аудиенции не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта аудиенции: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта аудиенции", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект аудиенции не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта аудиенции", e);
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete audience width id: {}", id);
        daoAudienceInterfaces.deleteById(id);
    }

    public Optional<Audience> findById(Long id) {
        logger.debug("Find audience width id: {}", id);
        return daoAudienceInterfaces.findById(id);
    }

    public List<Audience> findAll() {
        logger.debug("Find all audiences");
        return daoAudienceInterfaces.findAll();
    }

    public Page<Audience> findAll(Pageable pageable) {
        logger.debug("Find all audiences paginated");
        return daoAudienceInterfaces.findPaginatedAudience(pageable);
    }

    public boolean isSingle(Audience audience) {
        logger.debug("Check audience is single");
        return daoAudienceInterfaces.isSingle(audience);
    }

}
