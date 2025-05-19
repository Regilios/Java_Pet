package org.example.univer.hibernate;

import jakarta.persistence.EntityManager;
import org.example.univer.UniverApplication;
import org.example.univer.dao.hibernate.HibernateStudent;
import org.example.univer.models.Group;
import org.example.univer.models.Student;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@SpringBootTest(classes = UniverApplication.class)
@ExtendWith(MockitoExtension.class)
public class HibernateStudentTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private HibernateStudent mockStudent;

    @Test
    void whenCreateStudent_thenStudentIsPersisted() {
        Student student = new Student();
        student.setId(1L);

        mockStudent.create(student);
        verify(entityManager).persist(student);
    }

    @Test
    void whenUpdateStudent_thenStudentIsUpdated() {
        Student student = new Student();
        student.setId(1L);

        mockStudent.update(student);
        verify(entityManager).merge(student);
    }

    @Test
    void whenDeleteByIdStudent_thenStudentIsRemoved() {
        Student student = new Student();
        student.setId(1L);

        when(entityManager.find(Student.class, 1L)).thenReturn(student);
        mockStudent.deleteById(1L);

        verify(entityManager).remove(student);
    }

    @Test
    void whenFindByIdStudent_thenStudentIsReturned() {
        Student student = new Student();
        student.setId(1L);

        when(entityManager.find(Student.class, student.getId())).thenReturn(student);
        Optional<Student> result = mockStudent.findById(1l);

        assertTrue(result.isPresent());
        assertEquals(student, result.get());
    }

    @Test
    void whenFindAllStudent_thenListOfStudentIsReturned() {
        Student student = new Student();
        student.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        Query<Student> query = mock(Query.class);
        when(entityManager.createNamedQuery("findAllStudents", Student.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(student,student2));

        List<Student> result = mockStudent.findAll();
        assertEquals(2,result.size());
        assertEquals(student, result.get(0));
        assertEquals(student2, result.get(1));
    }
    @Test
    void whenFindPaginatedStudents_thenListOfStudentIsReturned() {
        Student student = new Student();
        student.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        Pageable pageable = mock(Pageable.class);
        when(pageable.getOffset()).thenReturn(0L);
        when(pageable.getPageSize()).thenReturn(10);

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("countAllStudents", Long.class)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(2L);

        Query<Student> queryAll = mock(Query.class);
        when(entityManager.createNamedQuery("findAllStudentPaginated", Student.class)).thenReturn(queryAll);
        when(queryAll.setFirstResult(0)).thenReturn(queryAll);
        when(queryAll.setMaxResults(10)).thenReturn(queryAll);
        when(queryAll.getResultList()).thenReturn(List.of(student,student2));

        Page<Student> result = mockStudent.findPaginatedStudents(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(student, result.getContent().get(0));
        assertEquals(student2, result.getContent().get(1));
    }

    @Test
    void whenCheckIsSingleStudent_thenReturnsCorrectResult() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("test");
        student.setLastName("test2");

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("countStudentByName", Long.class)).thenReturn(query);
        when(query.setParameter("firstName", student.getFirstName())).thenReturn(query);
        when(query.setParameter("lastName", student.getLastName())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1l);

        boolean result = mockStudent.isSingle(student);
        assertTrue(result);
    }

    @Test
    void whenCheckGroupSizeStudent_thenReturnsCorrectResult() {
        Group group = new Group();
        group.setId(1L);
        Student student = new Student();
        student.setId(1L);
        student.setGroup(group);

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("findStudentsByGroupId", Long.class)).thenReturn(query);
        when(query.setParameter("groupId", student.getGroup().getId())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        Integer result = mockStudent.checkGroupSize(student);
        assertEquals(1, result);
    }
}
