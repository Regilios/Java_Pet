package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.univer.controllers.rest.SubjectRestController;
import org.example.univer.dto.SubjectDto;
import org.example.univer.mappers.SubjectMapper;
import org.example.univer.models.Subject;
import org.example.univer.services.SubjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class SubjectRestControllerTest {
    @InjectMocks
    private SubjectRestController controller;
    @Mock
    private SubjectService subjectService;
    @Mock
    private SubjectMapper subjectMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private SubjectDto subjectDto;
    private Subject subject;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        subject = new Subject();
        subject.setId(1L);
        subject.setName("Test");
        subject.setDescription("test test test test test test");

        subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setName("Test");
        subjectDto.setDescription("test test test test test test");
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(subjectService.findAll()).thenReturn(List.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        when(subjectMapper.toEntity(any())).thenReturn(subject);
        when(subjectService.create(any())).thenReturn(subject);
        when(subjectMapper.toDto(any())).thenReturn(subjectDto);

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test"));

        verify(subjectService).create(subject);
    }

    @Test
    void whenGetById_thenReturnSubject() throws Exception {
        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(subjectDto);

        mockMvc.perform(get("/api/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        when(subjectService.existsById(1L)).thenReturn(true);
        when(subjectMapper.toEntity(any())).thenReturn(subject);
        when(subjectService.update(any())).thenReturn(subject);
        when(subjectMapper.toDto(any())).thenReturn(subjectDto);

        subjectDto.setName("Test Beta");

        mockMvc.perform(put("/api/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subjectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Beta"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(subjectService.existsById(1L)).thenReturn(true);
        doNothing().when(subjectService).deleteById(1L);

        mockMvc.perform(delete("/api/subjects/1"))
                .andExpect(status().isNoContent());
    }
}