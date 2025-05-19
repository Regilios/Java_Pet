package org.example.univer.hibernate;

import jakarta.persistence.EntityManager;
import org.example.univer.UniverApplication;
import org.example.univer.dao.hibernate.HibernateCathedra;
import org.example.univer.models.Cathedra;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@SpringBootTest(classes = UniverApplication.class)
@ExtendWith(MockitoExtension.class)
public class HibernateCathedraTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    HibernateCathedra mockCathedra;

    @Test
    void whenCreateCathedra_thenCathedraIsPersisted() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        mockCathedra.create(cathedra);
        verify(entityManager).persist(cathedra);
    }

    @Test
    void whenUpdateCathedra_thenCathedraIsMerged() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        mockCathedra.update(cathedra);
        verify(entityManager).merge(cathedra);
    }

    @Test
    void whenDeleteByIdCathedra_thenCathedraIsRemoved() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        when(entityManager.find(Cathedra.class, 1L)).thenReturn(cathedra);
        mockCathedra.deleteById(1L);

        verify(entityManager).remove(cathedra);
    }

    @Test
    void whenFindByIdCathedra_thenCathedraIsReturned() {
        Long id = 1L;
        Cathedra cathedra = new Cathedra();
        cathedra.setId(id);
        when(entityManager.find(Cathedra.class, id)).thenReturn(cathedra);

        Optional<Cathedra> result = mockCathedra.findById(id);

        assertTrue(result.isPresent());
        assertEquals(cathedra, result.get());
    }

    @Test
    void whenFindAllCathedra_thenListOfCathedraIsReturned() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        Cathedra cathedra2 = new Cathedra();
        cathedra2.setId(2L);
        Query<Cathedra> query =  mock(Query.class);
        when(entityManager.createNamedQuery("findAllCathedras", Cathedra.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(cathedra, cathedra2));

        List<Cathedra> result = mockCathedra.findAll();

        assertEquals(2, result.size());
        assertEquals(cathedra, result.get(0));
        assertEquals(cathedra2, result.get(1));
    }


    @Test
    void whenCheckIsSingleCathedra_thenReturnsCorrectResult() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        Query<Long> query =  mock(Query.class);

        when(entityManager.createNamedQuery("findCathedraByName", Long.class)).thenReturn(query);
        when(query.setParameter("name", cathedra.getName())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result = mockCathedra.isSingle(cathedra);

        assertTrue(result);
    }
}

