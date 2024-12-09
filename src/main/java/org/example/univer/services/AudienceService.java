package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.models.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AudienceService {
    private DaoAudienceInterface daoAudienceInterfaces;
    private static final Logger logger = LoggerFactory.getLogger(AudienceService.class);
    @Value("#{${roomSettings}}")
    private Map<String, Integer> roomSettings;

    @Autowired
    public AudienceService(DaoAudienceInterface daoAudienceInterfaces) {
        this.daoAudienceInterfaces = daoAudienceInterfaces;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Audience audience, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(audience)) {
                    throw new IllegalArgumentException("Невозможно создать аудиенцию! Аудиенция с номером: " + audience.getRoom() + " уже существует");
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
        if (audience.getCapacity() < getMinSize() || audience.getCapacity() > getMaxSize()) {
            throw new IllegalArgumentException("Невозможно " + action + " аудиенцию! Размер аудиенции не попадает в рамки критериев!");
        }
    }

    public void create(Audience audience) {
        logger.debug("Start create Audience");
        try {
            validate(audience, ValidationContext.METHOD_CREATE);
            daoAudienceInterfaces.create(audience);
            logger.debug("Audience created");
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Audience audience) {
        logger.debug("Start update Audience");
        try {
            validate(audience, ValidationContext.METHOD_UPDATE);
            daoAudienceInterfaces.update(audience);
            logger.debug("Audience updated");
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete audience width id: {}", id);
        daoAudienceInterfaces.deleteById(id);
    }

    public Audience findById(Long id) {
        logger.debug("Find audience width id: {}", id);
        return daoAudienceInterfaces.findById(id);
    }

    public List<Audience> findAll() {
        logger.debug("Find all audiences");
        return daoAudienceInterfaces.findAll();
    }

    public boolean isSingle(Audience audience) {
        logger.debug("Check audience is single");
        return daoAudienceInterfaces.isSingle(audience);
    }

    public Integer getMaxSize() {
        return roomSettings.get("SIZE_MAX");
    }

    public Integer getMinSize() {
        return roomSettings.get("SIZE_MIN");
    }
}
