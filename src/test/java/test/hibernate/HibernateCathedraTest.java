package test.hibernate;

import org.example.univer.dao.hibernate.HibernateCathedra;
import org.example.univer.models.Cathedra;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HibernateCathedraTest {
    @Mock
    Session session;
    @Mock
    SessionFactory sessionFactory;
    @InjectMocks
    HibernateCathedra mockCthedra;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void whenCreateCathedra_thenCathedraIsPersisted() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        mockCthedra.create(cathedra);
        verify(session).persist(cathedra);
    }

    @Test
    void whenUpdateCathedra_thenCathedraIsMerged() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        mockCthedra.update(cathedra);
        verify(session).merge(cathedra);
    }

    @Test
    void whenDeleteByIdCathedra_thenCathedraIsRemoved() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        mockCthedra.deleteEntity(cathedra);
        verify(session).remove(cathedra);
    }

    @Test
    void whenFindByIdCathedra_thenCathedraIsReturned() {
        Long id = 1L;
        Cathedra cathedra = new Cathedra();
        cathedra.setId(id);
        when(session.get(Cathedra.class, id)).thenReturn(cathedra);

        Optional<Cathedra> result = mockCthedra.findById(id);

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
        when(session.createNamedQuery("findAllCathedras", Cathedra.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(cathedra, cathedra2));

        List<Cathedra> result = mockCthedra.findAll();

        assertEquals(2, result.size());
        assertEquals(cathedra, result.get(0));
        assertEquals(cathedra2, result.get(1));
    }


    @Test
    void whenCheckIsSingleCathedra_thenReturnsCorrectResult() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        Query<Long> query =  mock(Query.class);

        when(session.createNamedQuery("findCathedraByName", Long.class)).thenReturn(query);
        when(query.setParameter("name", cathedra.getName())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result = mockCthedra.isSingle(cathedra);

        assertTrue(result);
    }
}

