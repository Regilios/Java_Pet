package org.example.univer.controllers;

import org.example.univer.dto.LectureDto;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.models.Lecture;
import org.example.univer.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LectureControllerTest {
    private MockMvc mockMvc;
    @Mock private TeacherService teacherService;
    @Mock private CathedraService cathedraService;
    @Mock private SubjectService subjectService;
    @Mock private LectureTimeService lectureTimeService;
    @Mock private AudienceService audienceService;
    @Mock private LectureService lectureService;
    @Mock private GroupService groupService;
    @Mock private LectureMapper lectureMapper;
    @InjectMocks
    private LectureController lectureController;

    @BeforeEach
    void setUp() {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));
        mockMvc = MockMvcBuilders
                .standaloneSetup(lectureController)
                .setCustomArgumentResolvers(pageableResolver)
                .build();
    }

    @Test
    void whenGetIndex_thenLectureListReturned() throws Exception {
        LectureDto dto = new LectureDto();
        Page<LectureDto> page = new PageImpl<>(List.of(dto));
        when(lectureService.findAllWithGroups(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/lectures").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/index"))
                .andExpect(model().attributeExists("lecturesDto"))
                .andExpect(model().attribute("title", "All Lectures"));
    }

    @Test
    void whenGetCreateForm_thenFormAttributesPresent() throws Exception {
        when(teacherService.findAll()).thenReturn(List.of());
        when(cathedraService.findAll()).thenReturn(List.of());
        when(subjectService.findAll()).thenReturn(List.of());
        when(lectureTimeService.findAll()).thenReturn(List.of());
        when(audienceService.findAll()).thenReturn(List.of());
        when(groupService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/lectures/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/new"))
                .andExpect(model().attributeExists(
                        "lectureDto", "teachers", "cathedras",
                        "subjects", "times", "audiences", "groups"));
    }

    @Test
    void whenPostValidLecture_thenRedirectToIndex() throws Exception {
        LectureDto dto = new LectureDto();
        Lecture entity = new Lecture();
        when(lectureMapper.toEntity(any(LectureDto.class))).thenReturn(entity);

        mockMvc.perform(post("/lectures")
                        .param("groupIds", "1", "2")
                        .param("teacher.id","1")
                        .param("cathedra.id","1")
                        .param("audience.id","1")
                        .param("subject.id","1")
                        .param("time.id","1")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"));

        verify(lectureService).create(any(Lecture.class));
    }

    @Test
    void whenGetEditForm_thenLectureDataReturned() throws Exception {
        LectureDto dto = new LectureDto();
        when(lectureService.findById(1L)).thenReturn(dto);
        when(teacherService.findAll()).thenReturn(List.of());
        when(cathedraService.findAll()).thenReturn(List.of());
        when(subjectService.findAll()).thenReturn(List.of());
        when(lectureTimeService.findAll()).thenReturn(List.of());
        when(audienceService.findAll()).thenReturn(List.of());
        when(groupService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/lectures/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/edit"))
                .andExpect(model().attributeExists(
                        "lectureDto", "teachers", "cathedras",
                        "subjects", "times", "audiences", "groups"));
    }

    @Test
    void whenUpdateValidLecture_thenRedirectToIndex() throws Exception {
        LectureDto dto = new LectureDto();
        Lecture entity = new Lecture();
        when(lectureMapper.toEntity(any(LectureDto.class))).thenReturn(entity);

        mockMvc.perform(patch("/lectures/1")
                        .param("groupIds", "1", "2")
                        .param("teacher.id","1")
                        .param("cathedra.id","1")
                        .param("subject.id","1")
                        .param("audience.id","1")
                        .param("time.id","1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"));

        verify(lectureService).update(any(Lecture.class));
    }

    @Test
    void whenShowLecture_thenLectureReturned() throws Exception {
        LectureDto dto = new LectureDto();
        when(lectureService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/lectures/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/show"))
                .andExpect(model().attributeExists("lectureDto"));
    }

    @Test
    void whenDeleteLecture_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/lectures/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"));

        verify(lectureService).deleteById(1L);
    }
}
