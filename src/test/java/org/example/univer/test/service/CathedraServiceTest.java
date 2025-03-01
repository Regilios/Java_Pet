package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcCathedra;
import org.example.univer.exeption.CathedraExeption;
import org.example.univer.models.Cathedra;
import org.example.univer.services.CathedraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CathedraServiceTest {
    @Mock
    private JdbcCathedra mockJdbcCathedra;

    @InjectMocks
    private CathedraService cathedraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(cathedraService, "maxLengthNameCathedra", 10);
        ReflectionTestUtils.setField(cathedraService, "startSymbolNameCathedra", "А");
    }

    @Test
    void create_cathedraStartSymbolA_createCathedra() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Андромеда");

        when(mockJdbcCathedra.isSingle(cathedra)).thenReturn(false);
        cathedraService.create(cathedra);

        verify(mockJdbcCathedra, times(1)).create(cathedra);
    }

    @Test
    void create_cathedraStartSymbolA_throwException() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Персиваль");
        when(mockJdbcCathedra.isSingle(cathedra)).thenReturn(false);

        assertThrows(CathedraExeption.class, () -> {
            cathedraService.validate(cathedra, CathedraService.ValidationContext.METHOD_CREATE);
            cathedraService.create(cathedra);
        });
        verify(mockJdbcCathedra, never()).create(any(Cathedra.class));
    }

    @Test
    void isSingle_cathedraIsSingle_true() {
        Cathedra cathedra = new Cathedra();

        when(mockJdbcCathedra.isSingle(cathedra)).thenReturn(true);
        assertTrue(cathedraService.isSingle(cathedra));

        verify(mockJdbcCathedra, times(1)).isSingle(any(Cathedra.class));
    }

    @Test
    void deleteById_deletedCathedra_deleted() {
        cathedraService.deleteById(1L);

        verify(mockJdbcCathedra, times(1)).deleteById(1L);
    }

    @Test
    void findById_findCathedra_found() {
        Cathedra cathedra = new Cathedra();

        when(mockJdbcCathedra.findById(1L)).thenReturn(cathedra);
        Cathedra result = cathedraService.findById(1L);

        assertEquals(cathedra, result);
    }

    @Test
    void findAll_findAllCathedras_foundAll() {
        List<Cathedra> cathedraList = List.of(new Cathedra(), new Cathedra());

        when(mockJdbcCathedra.findAll()).thenReturn(cathedraList);
        List<Cathedra> result = cathedraService.findAll();

        assertEquals(cathedraList, result);
    }
}
