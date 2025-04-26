package test.hibernate;

import org.example.univer.dao.hibernate.HibernateStudent;
import org.example.univer.models.Group;
import org.example.univer.models.Student;
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
public class HibernateStudentTest {
    @Mock
    private Session session;
    @Mock
    private SessionFactory sessionFactory;
    @InjectMocks
    private HibernateStudent mockStudent;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void whenCreateStudent_thenStudentIsPersisted() {
        Student student = new Student();
        student.setId(1L);

        mockStudent.create(student);
        verify(session).persist(student);
    }

    @Test
    void whenUpdateStudent_thenStudentIsUpdated() {
        Student student = new Student();
        student.setId(1L);

        mockStudent.update(student);
        verify(session).merge(student);
    }

    @Test
    void whenDeleteByIdStudent_thenStudentIsRemoved() {
        Student student = new Student();
        student.setId(1L);

        mockStudent.deleteById(student);
        verify(session).remove(student);
    }

    @Test
    void whenFindByIdStudent_thenStudentIsReturned() {
        Student student = new Student();
        student.setId(1L);

        when(session.get(Student.class, student.getId())).thenReturn(student);
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
        when(session.createNamedQuery("findAllStudents", Student.class)).thenReturn(query);
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
        when(session.createNamedQuery("countAllStudents", Long.class)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(2L);

        Query<Student> queryAll = mock(Query.class);
        when(session.createNamedQuery("findAllStudentPaginated", Student.class)).thenReturn(queryAll);
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
        when(session.createNamedQuery("countStudentByName", Long.class)).thenReturn(query);
        when(query.setParameter("firstName", student.getFirstName())).thenReturn(query);
        when(query.setParameter("lastName", student.getLastName())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1l);

        boolean result = mockStudent.isSingle(student);
        assertTrue(result);
    }

    @Test
    void whenCheckGroupSizeStudent_thenReturnsCorrectResult() {
        Group group = new Group();
        group.setId(1l);
        Student student = new Student();
        student.setId(1L);
        student.setGroup(group);


        Query<Integer> query = mock(Query.class);
        when(session.createNamedQuery("findStudentsByGroupId", Integer.class)).thenReturn(query);
        when(query.setParameter("group_id", student.getGroup().getId())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1);

        Integer result = mockStudent.checkGroupSize(student);
        assertEquals(1, result);
    }
}
