package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.models.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AudienceService {
    private DaoAudienceInterface daoAudienceInterfaces;

    @Value("#{${roomSettings}}")
    private Map<String, Integer> roomSettings;

    @Autowired
    public AudienceService(DaoAudienceInterface daoAudienceInterfaces) {
        this.daoAudienceInterfaces = daoAudienceInterfaces;
    }

    public void create(Audience audience) {
        if (!isSingle(audience)) {
            if(audience.getCapacity() >= getMinSize() && audience.getCapacity() <= getMaxSize()) {
                daoAudienceInterfaces.create(audience);
                System.out.println("Аудиенция создана");
            } else {
                System.out.println("Невозможно создать аудиенцию! Размер аудиенции не попадает в рамки критериев!");
            }
        } else {
            System.out.println(String.format("Невозможно создать аудиенцию! Аудиенция с номером %s уже существует", audience.getRoom()));
        }
    }

    public void update(Audience audience) {
        if (!isSingle(audience)) {
            if(audience.getCapacity() >= getMinSize() && audience.getCapacity() <= getMaxSize()) {
                daoAudienceInterfaces.update(audience);
                System.out.println("Аудиенция обновленна");
            } else {
                System.out.println("Невозможно обновить аудиенцию! Размер аудиенции не попадает в рамки критериев!");
            }
        } else {
            System.out.println(String.format("Невозможно обновить аудиенцию! Аудиенция с номером %s уже существует", audience.getRoom()));
        }
    }

    public void deleteById(Long id) {
        daoAudienceInterfaces.deleteById(id);
    }

    public Audience findById(Long id) {
        return daoAudienceInterfaces.findById(id);
    }

    public List<Audience> findAll() {
        return daoAudienceInterfaces.findAll();
    }

    public boolean isSingle(Audience audience) {
        return daoAudienceInterfaces.findRoom(audience);
    }

    public Integer getMaxSize() {
        return roomSettings.get("SIZE_MAX");
    }

    public Integer getMinSize() {
        return roomSettings.get("SIZE_MIN");
    }
}
