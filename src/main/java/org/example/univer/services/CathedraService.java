package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoCathedraInterface;
import org.example.univer.models.Cathedra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CathedraService {
    private DaoCathedraInterface daoCathedraInterface;

    @Value("${maxLengthNameCathedra}")
    private Integer maxLengthNameCathedra;
    //maxLengthNameCathedra=10
    @Autowired
    public CathedraService(DaoCathedraInterface daoCathedraInterface) {
        this.daoCathedraInterface = daoCathedraInterface;
    }

    public void create(Cathedra cathedra) {
        if (!isSingle(cathedra)) {
            if (cathedra.getName().length() <= maxLengthNameCathedra) {
                daoCathedraInterface.create(cathedra);
                System.out.println("Кафедра создана");
            } else {
                System.out.println("Невозможно создать кафедру! Наименование кафедры больше заданных настроек!");
            }
        } else {
            System.out.println(String.format("Невозможно создать кафедру! Кафедра с именем %s уже существует", cathedra.getName()));
        }
    }
    public void update(Cathedra cathedra) {
        if (!isSingle(cathedra)) {
            if (cathedra.getName().length() <= maxLengthNameCathedra) {
                daoCathedraInterface.update(cathedra);
                System.out.println("Кафедра обновленна");
            } else {
                System.out.println("Невозможно обновить кафедру! Наименование кафедры больше заданных настроек!");
            }
        } else {
            System.out.println(String.format("Невозможно обновить кафедру! Кафедра с именем %s уже существует", cathedra.getName()));
        }
    }

    public boolean isSingle(Cathedra cathedra) {
        return daoCathedraInterface.findRoom(cathedra);
    }

    public void deleteById(Long id) {
        daoCathedraInterface.deleteById(id);
    }

    public Cathedra findById(Long id) {
        return daoCathedraInterface.findById(id);
    }

    public List<Cathedra> findAll() {
        return daoCathedraInterface.findAll();
    }
}
