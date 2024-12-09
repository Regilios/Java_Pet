package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcSubject;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Mock
    private JdbcSubject mockJdbcSubject;

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
        subject.setId(1L);
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");
        when(mockJdbcSubject.isSingle(subject)).thenReturn(false);
        subjectService.create(subject);

        verify(mockJdbcSubject, times(1)).create(subject);
    }

    @Test
    void create_subjectDescriptionNotEmpty_throwException() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Test");
        subject.setDescription("");
        when(mockJdbcSubject.isSingle(subject)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            subjectService.validate(subject, SubjectService.ValidationContext.METHOD_CREATE);
            subjectService.create(subject);
        });
        verify(mockJdbcSubject, never()).create(any(Subject.class));
    }

    @Test
    void isSingle_subjectIsSingle_true() {
        Subject subject = new Subject();
        subject.setId(1L);

        when(mockJdbcSubject.isSingle(subject)).thenReturn(true);
        assertTrue(subjectService.isSingle(subject));

        verify(mockJdbcSubject, times(1)).isSingle(any(Subject.class));
    }

    @Test
    void deleteById_deletedSubject_deleted() {
        subjectService.deleteById(1L);

        verify(mockJdbcSubject, times(1)).deleteById(1L);
    }

    @Test
    void findById_findSubject_found() {
        Subject subject = new Subject();
        subject.setId(1L);

        when(mockJdbcSubject.findById(1L)).thenReturn(subject);
        Subject result = subjectService.findById(1L);

        assertEquals(subject, result);
    }

    @Test
    void findAll_findAllSubjects_foundAll() {
        List<Subject> subjectList = List.of(new Subject(), new Subject());

        when(mockJdbcSubject.findAll()).thenReturn(subjectList);
        List<Subject> result = subjectService.findAll();

        assertEquals(subjectList, result);
    }
}
