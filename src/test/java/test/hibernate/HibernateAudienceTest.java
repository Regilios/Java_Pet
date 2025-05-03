package test.hibernate;

import org.example.univer.dao.hibernate.HibernateAudience;
import org.example.univer.models.Audience;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HibernateAudienceTest {
    @Mock
    private Session session;
    @Mock
    private SessionFactory sessionFactory;
    @InjectMocks
    private HibernateAudience mockAudience;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void whenCreateAudience_thenAudienceIsPersisted() {
        Audience audience = new Audience();
        audience.setId(1L);
        audience.setCapacity(200);

        mockAudience.create(audience);

        verify(session).persist(audience);
    }

    @Test
    void whenUpdateAudience_thenAudienceIsMerged() {
        Audience audience = new Audience();
        audience.setId(1L);
        audience.setCapacity(200);

        mockAudience.update(audience);

        verify(session).merge(audience);
    }

    @Test
    void whenDeleteAudience_thenAudienceIsRemoved() {
        Audience audience = new Audience();
        audience.setId(1L);

        mockAudience.deleteEntity(audience);

        verify(session).remove(audience);
    }

    @Test
    void whenFindAudienceById_thenAudienceIsReturned() {
        Long id = 1L;
        Audience audience = new Audience();
        audience.setId(id);
        when(session.get(Audience.class, id)).thenReturn(audience);

        Optional<Audience> result = mockAudience.findById(id);

        assertTrue(result.isPresent());
        assertEquals(audience, result.get());
    }

    @Test
    void whenFindAllAudiences_thenListOfAudiencesIsReturned() {
        Audience audience1 = new Audience();
        audience1.setId(1L);
        Audience audience2 = new Audience();
        audience2.setId(2L);
        Query<Audience> audienceQuery = mock(Query.class);

        when(session.createNamedQuery("findAllAudiences", Audience.class)).thenReturn(audienceQuery);
        when(audienceQuery.getResultList()).thenReturn(List.of(audience1, audience2));

        List<Audience> result = mockAudience.findAll();

        assertEquals(2, result.size());
        assertEquals(audience1, result.get(0));
        assertEquals(audience2, result.get(1));
    }

    @Test
    void whenFindPaginatedAudiences_thenPageOfAudiencesIsReturned() {
        Audience audience1 = new Audience();
        audience1.setId(1L);
        Audience audience2 = new Audience();
        audience2.setId(2L);
        Pageable pageable = mock(Pageable.class);
        when(pageable.getOffset()).thenReturn(0L);
        when(pageable.getPageSize()).thenReturn(10);

        Query<Long> countQuery = mock(Query.class);
        when(session.createNamedQuery("countAllAudiences", Long.class)).thenReturn(countQuery);
        when(countQuery.uniqueResult()).thenReturn(2L);

        Query<Audience> audienceQuery = mock(Query.class);
        when(session.createNamedQuery("findAllAudiencesPaginated", Audience.class)).thenReturn(audienceQuery);
        when(audienceQuery.setFirstResult(0)).thenReturn(audienceQuery);
        when(audienceQuery.setMaxResults(10)).thenReturn(audienceQuery);
        when(audienceQuery.getResultList()).thenReturn(List.of(audience1, audience2));

        Page<Audience> result = mockAudience.findPaginatedAudience(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(audience1, result.getContent().get(0));
        assertEquals(audience2, result.getContent().get(1));
    }

    @Test
    void whenCheckIsSingleAudience_thenReturnsCorrectResult() {
        Audience audience = new Audience();
        audience.setCapacity(101);
        audience.setRoom(10);

        Query<Long> query = mock(Query.class);
        when(session.createNamedQuery("countAudiencesByRoomNumber", Long.class)).thenReturn(query);
        when(query.setParameter("roomNumber", audience.getRoom())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);


        boolean result = mockAudience.isSingle(audience);

        assertTrue(result);
    }

}
