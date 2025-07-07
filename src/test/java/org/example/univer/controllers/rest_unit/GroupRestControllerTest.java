package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.univer.controllers.rest.GroupRestController;
import org.example.univer.dto.CathedraDto;
import org.example.univer.dto.GroupDto;
import org.example.univer.mappers.GroupMapper;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Group;
import org.example.univer.services.GroupService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class GroupRestControllerTest {
    @InjectMocks
    private GroupRestController controller;
    @Mock
    private GroupService groupService;
    @Mock
    private GroupMapper groupMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private GroupDto groupDto;
    private Group group;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(1L);
        cathedraDto.setName("Math");

        groupDto = new GroupDto();
        groupDto.setId(1L);
        groupDto.setName("Alpha");
        groupDto.setCathedra(cathedraDto);

        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        cathedra.setName("Math");

        group = new Group();
        group.setId(1L);
        group.setName("Alpha");
        group.setCathedra(cathedra);
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(groupService.findAll()).thenReturn(List.of(group));
        when(groupMapper.toDto(group)).thenReturn(groupDto);

        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alpha"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        when(groupMapper.toEntity(any())).thenReturn(group);
        when(groupService.create(any())).thenReturn(group);
        when(groupMapper.toDto(any())).thenReturn(groupDto);

        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Alpha"));

        verify(groupService).create(group);
    }

    @Test
    void whenGetById_thenReturnGroup() throws Exception {
        when(groupService.findById(1L)).thenReturn(Optional.of(group));
        when(groupMapper.toDto(group)).thenReturn(groupDto);

        mockMvc.perform(get("/api/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alpha"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        when(groupService.existsById(1L)).thenReturn(true);
        when(groupMapper.toEntity(any())).thenReturn(group);
        when(groupService.update(any())).thenReturn(group);
        when(groupMapper.toDto(any())).thenReturn(groupDto);

        groupDto.setName("Beta");

        mockMvc.perform(put("/api/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Beta"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(groupService.existsById(1L)).thenReturn(true);
        doNothing().when(groupService).deleteById(1L);

        mockMvc.perform(delete("/api/groups/1"))
                .andExpect(status().isNoContent());
    }
}


