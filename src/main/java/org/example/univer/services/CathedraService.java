package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.CathedraExeption;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.models.Cathedra;
import org.example.univer.repositories.CathedraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CathedraService {
    private final CathedraRepository cathedraRepository;
    private static final Logger logger = LoggerFactory.getLogger(CathedraService.class);
    private final AppSettings appSettings;
    private Integer maxLengthNameCathedra;
    private String startSymbolNameCathedra;

    @PostConstruct
    public void init() {
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
                    throw new InvalidParameterException(String.format("Невозможно создать кафедру! Кафедра с именем: %s уже существует.", cathedra.getName()));
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

    public Cathedra create(Cathedra cathedra) {
        logger.debug("Creating cathedra: {}", cathedra);
        validate(cathedra, ValidationContext.METHOD_CREATE);
        return cathedraRepository.save(cathedra);
    }

    public Cathedra update(Cathedra cathedra) {
        logger.debug("Updating cathedra: {}", cathedra);
        validate(cathedra, ValidationContext.METHOD_CREATE);
        return cathedraRepository.save(cathedra);
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
