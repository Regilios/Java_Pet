package test.hibernate;

import jakarta.persistence.EntityManager;
import org.example.univer.UniverApplication;
import org.example.univer.dao.hibernate.HibernateGroup;
import org.example.univer.models.Group;
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
public class HibernateGroupTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    HibernateGroup mockGroup;

    @Test
    void whenCreateGroup_thenGroupIsPersisted() {
        Group group = new Group();
        group.setId(1L);

        mockGroup.create(group);
        verify(entityManager).persist(group);
    }

    @Test
    void whenUpdateGroup_thenGroupIsUpdated() {
        Group group = new Group();
        group.setId(1L);

        mockGroup.update(group);
        verify(entityManager).merge(group);
    }

    @Test
    void whenDeleteByIdGroup_thenGroupIsRemoved() {
        Group group = new Group();
        group.setId(1L);

        when(entityManager.find(Group.class, 1L)).thenReturn(group);
        mockGroup.deleteById(1L);

        verify(entityManager).remove(group);
    }

    @Test
    void whenFindByIdGroup_thenGroupIsReturned() {
        Group group = new Group();
        group.setId(1L);

        when(entityManager.find(Group.class, group.getId())).thenReturn(group);
        Optional<Group> result =  mockGroup.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(group, result.get());
    }

    @Test
    void whenFindAllGroup_thenListOfGroupIsReturned() {
        Group group = new Group();
        group.setId(1L);
        Group group2 = new Group();
        group2.setId(2L);

        Query<Group> query =  mock(Query.class);

        when(entityManager.createNamedQuery("findAllGroups", Group.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(group,group2));

        List<Group> result =  mockGroup.findAll();

        assertEquals(2, result.size());
        assertEquals(group, result.get(0));
        assertEquals(group2, result.get(1));
    }

    @Test
    void whenCheckIsSingleGroup_thenReturnsCorrectResult() {
        Group group = new Group();
        group.setId(1L);

        Query<Long> query =  mock(Query.class);

        when(entityManager.createNamedQuery("findGroupByName", Long.class)).thenReturn(query);
        when(query.setParameter("name", group.getName())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result = mockGroup.isSingle(group);
        assertTrue(result);
    }

    @Test
    void whenGetGroupById_thenGroupIsReturned() {
        Group group = new Group();
        group.setId(1L);
        Group group2 = new Group();
        group2.setId(2L);

        Query<Group> query =  mock(Query.class);
        List<Long> groupIds = List.of(1L,2L);

        when(entityManager.createNamedQuery("findGroupsByIds", Group.class)).thenReturn(query);
        when(query.setParameter("ids", groupIds)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(group,group2));

        List<Group> result = mockGroup.getGroupById(groupIds);

        assertEquals(2, result.size());
        assertEquals(group, result.get(0));
        assertEquals(group2, result.get(1));


    }
}
