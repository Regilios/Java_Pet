package test.hibernate;

import jakarta.persistence.EntityManager;
import org.example.univer.UniverApplication;
import org.example.univer.dao.hibernate.HibernateTeacher;
import org.example.univer.models.Teacher;
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
public class HibernateTeacherTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    HibernateTeacher mockTeacher;

    @Test
    void whenCreateTeacher_thenTeacherIsPersisted() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        mockTeacher.create(teacher);
        verify(entityManager).persist(teacher);
    }

    @Test
    void whenUpdateTeacher_thenTeacherIsUpdated() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        mockTeacher.update(teacher);
        verify(entityManager).merge(teacher);
    }

    @Test
    void whenDeleteByIdTeacher_thenTeacherIsRemoved() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(entityManager.find(Teacher.class, 1L)).thenReturn(teacher);
        mockTeacher.deleteById(1L);

        verify(entityManager).remove(teacher);
    }

    @Test
    void whenFindByIdTeacher_thenTeacherIsReturned() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(entityManager.find(Teacher.class, 1L)).thenReturn(teacher);
        Optional<Teacher> result = mockTeacher.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(teacher, result.get());
    }

    @Test
    void whenFindAllTeacher_thenListOfTeachersIsReturned() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setId(1L);

        Query<Teacher> query = mock(Query.class);
        when(entityManager.createNamedQuery("findAllTeachers", Teacher.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(teacher,teacher2));

        List<Teacher> result = mockTeacher.findAll();

        assertEquals(2, result.size());
        assertEquals(teacher, result.get(0));
        assertEquals(teacher2, result.get(1));
    }

    @Test
    void whenCheckIsSingleTeacher_thenReturnsCorrectResult() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("test");
        teacher.setLastName("test2");

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("countTeachers", Long.class)).thenReturn(query);
        when(query.setParameter("firstName", teacher.getFirstName())).thenReturn(query);
        when(query.setParameter("lastName", teacher.getLastName())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result = mockTeacher.isSingle(teacher);
        assertTrue(result);
    }
}
