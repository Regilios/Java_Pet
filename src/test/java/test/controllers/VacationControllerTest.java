package test.controllers;

import org.example.univer.controllers.VacationController;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.example.univer.services.LectureService;
import org.example.univer.services.TeacherService;
import org.example.univer.services.VacationService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class VacationControllerTest {
    private MockMvc mockMvc;
    @Mock
    private VacationService vacationService;
    @Mock
    private LectureService lectureService;
    @Mock
    private TeacherService teacherService;
    @InjectMocks
    private VacationController vacationController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController).build();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(vacationService, "minVacationDay", 7);
        ReflectionTestUtils.setField(vacationService, "maxVacationDay", 20);
    }

    @Test
    public void whenGetAllVacations_thenAllVacationsReturned() throws Exception {
        Teacher teacher = new Teacher();
        Long teacherId = 1L;

        Vacation vacation1 = new Vacation();
        vacation1.setStartJob(LocalDate.parse("2024-07-01"));
        vacation1.setEndJob(LocalDate.parse("2024-07-14"));
        vacation1.setTeacher(teacher);
        vacationService.create(vacation1);

        Vacation vacation2 = new Vacation();
        vacation2.setStartJob(LocalDate.parse("2025-07-01"));
        vacation2.setEndJob(LocalDate.parse("2025-07-14"));
        vacation2.setTeacher(teacher);
        vacationService.create(vacation2);

        List<Vacation> vacationList = List.of(vacation1, vacation2);

        when(vacationService.findByTeacherId(teacherId)).thenReturn(vacationList);
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/{teacherId}/vacations", teacherId))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/index"))
                .andExpect(model().attribute("vacations", vacationList))
                .andExpect(model().attribute("teacher", teacher))
                .andDo(print());

        verify(vacationService, times(1)).findByTeacherId(teacherId);
        verify(teacherService, times(1)).findById(teacherId);
    }

    @Test
    public void whenGetOneVacation_thenOneVacationReturned() throws Exception {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-14"));
        vacation.setTeacher(teacher);
        vacationService.create(vacation);

        when(vacationService.findById(1L)).thenReturn(Optional.of(vacation));

        mockMvc.perform(get("/teachers/{teacherId}/vacations/{id}", 1, 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/show"))
                .andExpect(model().attributeExists("vacation"))
                .andExpect(model().attribute("vacation", vacation))
                .andExpect(forwardedUrl("teachers/vacations/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewVacation_thenNewVacationCreated() throws Exception {
        Teacher teacher = new Teacher();
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/{teacherId}/vacations/new", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/new"))
                .andExpect(model().attributeExists("vacation"))
                .andExpect(model().attributeExists("teacher"))
                .andDo(print());

        verify(teacherService, times(1)).findById(1L);
    }

    @Test
    void whenEditVacation_thenVacationFound() throws Exception {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-14"));
        vacation.setTeacher(teacher);

        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
        when(vacationService.findById(1L)).thenReturn(Optional.of(vacation));

        mockMvc.perform(get("/teachers/{teacherId}/vacations/{id}/edit", 1, 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/edit"))
                .andExpect(model().attributeExists("vacation"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(forwardedUrl("teachers/vacations/edit"))
                .andExpect(model().attribute("vacation", vacation))
                .andExpect(model().attribute("teacher", teacher))
                .andDo(print());

        verify(teacherService, times(1)).findById(1L);
        verify(vacationService, times(1)).findById(1L);
    }

    @Test
    public void whenUpdateVacation_thenVacationUpdated() throws Exception {
        Long teacherId = 1L;
        Long vacationId = 1L;

        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-14"));
        vacation.setTeacher(teacher);

        when(teacherService.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(lectureService.findByTeacherIdAndPeriod(teacher, vacation.getStartJob(), vacation.getEndJob())).thenReturn(List.of());

        mockMvc.perform(patch("/teachers/{teacherId}/vacations/{id}", teacherId, vacationId)
                        .flashAttr("vacation", vacation))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers/" + teacherId + "/vacations"));

        verify(vacationService, times(1)).update(vacation);
    }

    @Test
    void whenDeleteVacation_thenVacationDeleted() throws Exception {
        Vacation vacation = new Vacation();
        vacation.setId(1L);

        when(vacationService.findById(1L)).thenReturn(Optional.of(vacation));
        doNothing().when(vacationService).deleteById(vacation);

        mockMvc.perform(delete("/teachers/{teacherId}/vacations/{id}", 1, 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers/1/vacations"));

        verify(vacationService).findById(1L);
        verify(vacationService).deleteById(vacation);

    }
}
