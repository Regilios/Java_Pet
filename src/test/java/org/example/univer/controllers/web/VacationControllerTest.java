package org.example.univer.controllers.web;

import org.example.univer.dto.TeacherDto;
import org.example.univer.dto.VacationDto;
import org.example.univer.mappers.TeacherMapper;
import org.example.univer.mappers.VacationMapper;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VacationControllerTest {
    private MockMvc mockMvc;
    @Mock
    private TeacherService teacherService;
    @Mock
    private VacationService vacationService;
    @Mock
    private LectureService lectureService;
    @Mock
    private VacationMapper vacationMapper;
    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private VacationController vacationController;

    private final Long teacherId = 1L;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController).build();
    }

    @Test
    void whenGetAllVacations_thenReturnVacationListView() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();

        when(teacherService.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        when(vacationService.findByTeacherId(teacherId)).thenReturn(List.of());

        mockMvc.perform(get("/teachers/{teacherId}/vacations", teacherId))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/index"))
                .andExpect(model().attribute("teacher", teacherDto))
                .andExpect(model().attribute("vacations", List.of()));
    }

    @Test
    void whenGetCreateVacationForm_thenReturnForm() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();

        when(teacherService.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/teachers/{teacherId}/vacations/new", teacherId))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/new"))
                .andExpect(model().attribute("teacher", teacherDto))
                .andExpect(model().attributeExists("vacationDto"));
    }

    @Test
    void whenDeleteVacation_thenRedirectToVacationList() throws Exception {
        mockMvc.perform(delete("/teachers/{teacherId}/vacations/{id}", teacherId, 5L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers/1/vacations"));

        verify(vacationService).deleteById(5L);
    }

    @Test
    void whenGetVacationEditForm_thenReturnEditPage() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();
        Vacation vacation = new Vacation();
        VacationDto vacationDto = new VacationDto();

        when(teacherService.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        when(vacationService.findById(2L)).thenReturn(Optional.of(vacation));
        when(vacationMapper.toDto(vacation)).thenReturn(vacationDto);

        mockMvc.perform(get("/teachers/{teacherId}/vacations/{id}/edit", teacherId, 2L))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/edit"))
                .andExpect(model().attribute("teacher", teacherDto))
                .andExpect(model().attribute("vacationDto", vacationDto));
    }

    @Test
    void whenGetVacationDetails_thenReturnShowView() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();
        Vacation vacation = new Vacation();
        VacationDto vacationDto = new VacationDto();

        when(teacherService.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
        when(vacationService.findById(3L)).thenReturn(Optional.of(vacation));
        when(vacationMapper.toDto(vacation)).thenReturn(vacationDto);

        mockMvc.perform(get("/teachers/{teacherId}/vacations/{id}", teacherId, 3L))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/vacations/show"))
                .andExpect(model().attribute("teacher", teacherDto))
                .andExpect(model().attribute("vacationDto", vacationDto));
    }
}
