package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoCathedraInterface;
import org.example.univer.models.Cathedra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CathedraService {
    private DaoCathedraInterface daoCathedraInterface;
    private static final Logger logger = LoggerFactory.getLogger(CathedraService.class);
    @Value("#{${maxLengthNameCathedra}}")
    private Integer maxLengthNameCathedra;

    @Value("#{${startSymbolNameCathedra}}")
    private String startSymbolNameCathedra;

    @Autowired
    public CathedraService(DaoCathedraInterface daoCathedraInterface) {
        this.daoCathedraInterface = daoCathedraInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE, METHOD_UPDATE
    }

    public String validate(Cathedra cathedra, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(cathedra)) {
                    throw new IllegalArgumentException("Невозможно создать кафедру! Кафедра с именем: " + cathedra.getName() + " уже существует.");
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
            throw new IllegalArgumentException("Наименование кафедры не может быть null.");
        }
        if (cathedra.getName().length() > maxLengthNameCathedra) {
            throw new IllegalArgumentException("Невозможно " + action + " кафедру! Наименование кафедры больше допустимой длины: " + maxLengthNameCathedra + ".");
        }
        if (!cathedra.getName().startsWith(startSymbolNameCathedra)) {
            throw new IllegalArgumentException("Невозможно " + action + " кафедру! Наименование кафедры не начинается с символа: " + startSymbolNameCathedra + ".");
        }
    }

    public void create(Cathedra cathedra) {
        logger.debug("Start create Cathedra");
        try {
            validate(cathedra, ValidationContext.METHOD_CREATE);
            daoCathedraInterface.create(cathedra);
            logger.debug("Cathedra created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
        logger.debug("Cathedra created");
    }

    public void update(Cathedra cathedra) {
        logger.debug("Start update Cathedra");
        try {
            validate(cathedra, ValidationContext.METHOD_UPDATE);
            daoCathedraInterface.update(cathedra);
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
        logger.debug("Cathedra updated");
    }

    public void deleteById(Long id) {
        logger.debug("Delete cathedra width id: {}", id);
        daoCathedraInterface.deleteById(id);
    }

    public Cathedra findById(Long id) {
        logger.debug("Find cathedra width id: {}", id);
        return daoCathedraInterface.findById(id);
    }

    public List<Cathedra> findAll() {
        logger.debug("Find all cathedrals");
        return daoCathedraInterface.findAll();
    }

    public boolean isSingle(Cathedra cathedra) {
        logger.debug("Check cathedra is single");
        return daoCathedraInterface.isSingle(cathedra);
    }
}
