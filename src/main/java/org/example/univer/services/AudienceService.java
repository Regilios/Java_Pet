package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.AudienceExeption;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.models.Audience;
import org.example.univer.repositories.AudienceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AudienceService {
    private final AudienceRepository audienceRepository;
    private static final Logger logger = LoggerFactory.getLogger(AudienceService.class);
    private final AppSettings appSettings;
    private Integer maxSize;
    private Integer minSize;

    @PostConstruct
    public void init() {
        this.maxSize = appSettings.getRoomSettings().getSizeMax();
        this.minSize = appSettings.getRoomSettings().getSizeMin();
    }


    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Audience audience, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(audience)) {
                    throw new InvalidParameterException(String.format("Невозможно создать аудиенцию! Аудиенция с номером: %s уже существует", audience.getRoomNumber()));
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
        if (audience.getCapacity() < minSize || audience.getCapacity() > maxSize) {
            throw new AudienceExeption("Невозможно " + action + " аудиенцию! Размер аудиенции не попадает в рамки критериев!");
        }
    }

    public Audience create(Audience audience) {
        logger.debug("Creating Audience: {}", audience);
        validate(audience, ValidationContext.METHOD_CREATE);
        return audienceRepository.save(audience);
    }

    public Audience update(Audience audience) {
        logger.debug("Updating Audience: {}", audience);
        validate(audience, ValidationContext.METHOD_UPDATE);
        return audienceRepository.save(audience);
    }

    public void deleteById(Long id) {
        logger.debug("Delete audience width id: {}", id);
        audienceRepository.deleteById(id);
    }

    public Optional<Audience> findById(Long id) {
        logger.debug("Find audience width id: {}", id);
        return audienceRepository.findById(id);
    }

    public boolean existsById(Long id) {
        logger.debug("Check audience is single");
        return audienceRepository.existsById(id);
    }

    public List<Audience> findAll() {
        logger.debug("Find all audiences");
        return audienceRepository.findAll();
    }

    public Page<Audience> findAll(Pageable pageable) {
        logger.debug("Find all audiences paginated");
        return audienceRepository.findAll(pageable);
    }

    public boolean isSingle(Audience audience) {
        logger.debug("Check audience is single");
        return audienceRepository.existsByRoomNumber(audience.getRoomNumber());
    }

}
