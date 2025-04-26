package test.controllers;

import org.example.univer.controllers.SubjectController;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SubjectControllerTest {
    private MockMvc mockMvc;
    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(subjectService, "minSizeDescription", 20);
    }

    @Test
    public void whenGetAllSubjects_thenAllSubjectsReturned() throws Exception {
        Subject subject1 = new Subject();
        subject1.setName("Test");
        subject1.setDescription("Test Test Test Test Test Test Test");
        subjectService.create(subject1);

        Subject subject2 = new Subject();
        subject2.setName("Test22");
        subject2.setDescription("Test Test Test Test Test Test Test");
        subjectService.create(subject2);

        List<Subject> subjects = Arrays.asList(subject1, subject2);

        when(subjectService.findAll()).thenReturn(subjects);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/index"))
                .andExpect(forwardedUrl("subjects/index"))
                .andExpect(model().attribute("subjects", subjects))
                .andDo(print());
    }

    @Test
    public void whenGetOneSubject_thenOneSubjectReturned() throws Exception {
        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");
        subjectService.create(subject);

        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));

        mockMvc.perform(get("/subjects/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/show"))
                .andExpect(model().attributeExists("subject"))
                .andExpect(model().attribute("subject", subject))
                .andExpect(forwardedUrl("subjects/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewSubject_thenNewSubjectCreated() throws Exception {
        mockMvc.perform(get("/subjects/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/new"))
                .andExpect(model().attributeExists("subject"))
                .andDo(print());
    }

    @Test
    void whenEditSubject_thenSubjectFound() throws Exception {
        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");

        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));


        mockMvc.perform(get("/subjects/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/edit"))
                .andExpect(model().attributeExists("subject"))
                .andExpect(forwardedUrl("subjects/edit"))
                .andExpect(model().attribute("subject", subject))
                .andDo(print());

        verify(subjectService, times(1)).findById(1L);
    }

    @Test
    public void whenUpdateSubject_thenSubjectUpdated() throws Exception {
        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");

        mockMvc.perform(patch("/subjects/{id}", 1)
                        .flashAttr("subject", subject))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"))
                .andDo(print());

        verify(subjectService, times(1)).update(subject);
    }

    @Test
    void whenDeleteSubject_thenSubjectDeleted() throws Exception {
        Subject subject = new Subject();
        subject.setId(1L);
        mockMvc.perform(delete("/subjects/{id}", 1))
                .andExpect(redirectedUrl("/subjects"));

        verify(subjectService).deleteById(subject);
    }
}
