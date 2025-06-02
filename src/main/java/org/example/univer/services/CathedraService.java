package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.repositories.CathedraRepository;
import org.example.univer.exeption.*;
import org.example.univer.models.Cathedra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CathedraService {
    private CathedraRepository cathedraRepository;
    private static final Logger logger = LoggerFactory.getLogger(CathedraService.class);
    private AppSettings appSettings;
    private Integer maxLengthNameCathedra;
    private String startSymbolNameCathedra;

    @Autowired
    public CathedraService(CathedraRepository cathedraRepository, AppSettings appSettings) {
        this.cathedraRepository = cathedraRepository;
        this.appSettings = appSettings;
        this.maxLengthNameCathedra = appSettings.getMaxLengthNameCathedra();
        this.startSymbolNameCathedra = appSettings.getStartSymbolNameCathedra();
    }

    public enum ValidationContext {
        METHOD_CREATE, METHOD_UPDATE
    }

    public String validate(Cathedra cathedra, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(cathedra)) {
                    throw new InvalidParameterException("Невозможно создать кафедру! Кафедра с именем: " + cathedra.getName() + " уже существует.");
                }
                validateCommon(cathedra, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(cathedra, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен: " + context;
        }
        return null;
    }

    private void validateCommon(Cathedra cathedra, String action) {
        if (cathedra.getName() == null) {
            throw new CathedraExeption("Наименование кафедры не может быть null.");
        }
        if (cathedra.getName().length() > maxLengthNameCathedra) {
            throw new CathedraExeption("Невозможно " + action + " кафедру! Наименование кафедры больше допустимой длины: " + maxLengthNameCathedra + ".");
        }
        if (!cathedra.getName().startsWith(startSymbolNameCathedra)) {
            throw new CathedraExeption("Невозможно " + action + " кафедру! Наименование кафедры не начинается с символа: " + startSymbolNameCathedra + ".");
        }
    }

    public void create(Cathedra cathedra) {
        logger.debug("Start create Cathedra");
        try {
            validate(cathedra, ValidationContext.METHOD_CREATE);
            cathedraRepository.save(cathedra);
            logger.debug("Cathedra created");
        } catch (CathedraExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта праздника: {}", e.getMessage(), e);
            throw new NullEntityException("Объект кафедры не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта кафедры: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта кафедры", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект кафедры не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта кафедры", e);
        }
        logger.debug("Cathedra created");
    }

    public void update(Cathedra cathedra) {
        logger.debug("Start update Cathedra");
        try {
            validate(cathedra, ValidationContext.METHOD_UPDATE);
            cathedraRepository.save(cathedra);
        } catch (CathedraExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта праздника: {}", e.getMessage(), e);
            throw new NullEntityException("Объект кафедры не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта кафедры: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта кафедры", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект кафедры не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта кафедры", e);
        }
        logger.debug("Cathedra updated");
    }

    public void deleteById(Long id) {
        logger.debug("Delete cathedra width id: {}", id);
        cathedraRepository.deleteById(id);
    }

    public Optional<Cathedra> findById(Long id) {
        logger.debug("Find cathedra width id: {}", id);
        return cathedraRepository.findById(id);
    }

    public List<Cathedra> findAll() {
        logger.debug("Find all cathedrals");
        return cathedraRepository.findAll();
    }

    public boolean isSingle(Cathedra cathedra) {
        logger.debug("Check cathedra is single");
        return cathedraRepository.existsByName(cathedra.getName());
    }
}
