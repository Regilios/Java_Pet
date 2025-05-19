package org.example.univer.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoCathedraInterface;
import org.example.univer.exeption.CathedraExeption;
import org.example.univer.models.Cathedra;
import org.example.univer.services.CathedraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class CathedraServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoCathedraInterface mockCathedra;
    @InjectMocks
    private CathedraService cathedraService;

    @BeforeEach
    void setUp() {
        appSettings.setMaxLengthNameCathedra(10);
        appSettings.setStartSymbolNameCathedra("А");
        cathedraService = new CathedraService(mockCathedra, appSettings);
    }

    @Test
    void create_cathedraStartSymbolA_createCathedra() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Андромеда");

        when(mockCathedra.isSingle(cathedra)).thenReturn(false);
        cathedraService.create(cathedra);

        verify(mockCathedra, times(1)).create(cathedra);
    }

    @Test
    void create_cathedraStartSymbolA_throwException() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Персиваль");
        when(mockCathedra.isSingle(cathedra)).thenReturn(false);

        assertThrows(CathedraExeption.class, () -> {
            cathedraService.validate(cathedra, CathedraService.ValidationContext.METHOD_CREATE);
            cathedraService.create(cathedra);
        });
        verify(mockCathedra, never()).create(any(Cathedra.class));
    }

    @Test
    void isSingle_cathedraIsSingle_true() {
        Cathedra cathedra = new Cathedra();

        when(mockCathedra.isSingle(cathedra)).thenReturn(true);
        assertTrue(cathedraService.isSingle(cathedra));

        verify(mockCathedra, times(1)).isSingle(any(Cathedra.class));
    }

    @Test
    void deleteById_deletedCathedra_deleted() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        cathedraService.deleteById(1L);

        verify(mockCathedra, times(1)).deleteById(1L);
    }

    @Test
    void findById_findCathedra_found() {
        Cathedra cathedra = new Cathedra();

        when(mockCathedra.findById(1L)).thenReturn(Optional.of(cathedra));
        Optional<Cathedra> result = cathedraService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(cathedra, result.get());
    }

    @Test
    void findAll_findAllCathedras_foundAll() {
        List<Cathedra> cathedraList = List.of(new Cathedra(), new Cathedra());

        when(mockCathedra.findAll()).thenReturn(cathedraList);
        List<Cathedra> result = cathedraService.findAll();

        assertEquals(cathedraList, result);
    }
}
