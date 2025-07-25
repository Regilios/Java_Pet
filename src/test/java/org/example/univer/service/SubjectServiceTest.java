package org.example.univer.service;

import org.example.univer.config.AppSettings;
import org.example.univer.exeption.SubjectExeption;
import org.example.univer.models.Subject;
import org.example.univer.repositories.SubjectRepository;
import org.example.univer.services.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private SubjectRepository mockSubject;
    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        appSettings.setMinSizeDescription(20);
        subjectService.init();
    }

    @Test
    void create_subjectDescriptionNotEmpty_createCathedra() {
        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");
        when(mockSubject.existsByName(subject.getName())).thenReturn(false);
        subjectService.create(subject);

        verify(mockSubject, times(1)).save(subject);
    }

    @Test
    void create_subjectDescriptionNotEmpty_throwException() {
        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("");
        when(mockSubject.existsByName(subject.getName())).thenReturn(false);

        assertThrows(SubjectExeption.class, () -> {
            subjectService.validate(subject, SubjectService.ValidationContext.METHOD_CREATE);
            subjectService.create(subject);
        });
        verify(mockSubject, never()).save(any(Subject.class));
    }

    @Test
    void isSingle_subjectIsSingle_true() {
        Subject subject = new Subject();
        subject.setName("test");
        when(mockSubject.existsByName(subject.getName())).thenReturn(true);
        assertTrue(subjectService.isSingle(subject));

        verify(mockSubject, times(1)).existsByName(anyString());
    }

    @Test
    void deleteById_deletedSubject_deleted() {
        Subject subject = new Subject();
        subject.setId(1L);
        subjectService.deleteById(1L);

        verify(mockSubject, times(1)).deleteById(1L);
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
