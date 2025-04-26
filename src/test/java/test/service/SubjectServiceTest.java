package test.service;

import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.exeption.SubjectExeption;
import org.example.univer.models.Subject;
import org.example.univer.services.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Mock
    private DaoSubjectInterface mockSubject;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(subjectService, "minSizeDescription", 20);
    }

    @Test
    void create_subjectDescriptionNotEmpty_createCathedra() {
        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");
        when(mockSubject.isSingle(subject)).thenReturn(false);
        subjectService.create(subject);

        verify(mockSubject, times(1)).create(subject);
    }

    @Test
    void create_subjectDescriptionNotEmpty_throwException() {
        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("");
        when(mockSubject.isSingle(subject)).thenReturn(false);

        assertThrows(SubjectExeption.class, () -> {
            subjectService.validate(subject, SubjectService.ValidationContext.METHOD_CREATE);
            subjectService.create(subject);
        });
        verify(mockSubject, never()).create(any(Subject.class));
    }

    @Test
    void isSingle_subjectIsSingle_true() {
        Subject subject = new Subject();

        when(mockSubject.isSingle(subject)).thenReturn(true);
        assertTrue(subjectService.isSingle(subject));

        verify(mockSubject, times(1)).isSingle(any(Subject.class));
    }

    @Test
    void deleteById_deletedSubject_deleted() {
        Subject subject = new Subject();

        subjectService.deleteById(subject);

        verify(mockSubject, times(1)).deleteById(subject);
    }

    @Test
    void findById_findSubject_found() {
        Subject subject = new Subject();

        when(mockSubject.findById(1L)).thenReturn(Optional.of(subject));
        Optional<Subject> result = subjectService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(subject, result.get());
    }

    @Test
    void findAll_findAllSubjects_foundAll() {
        List<Subject> subjectList = List.of(new Subject(), new Subject());

        when(mockSubject.findAll()).thenReturn(subjectList);
        List<Subject> result = subjectService.findAll();

        assertEquals(subjectList, result);
    }
}
