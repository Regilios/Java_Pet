package org.example.univer.controllers;

import org.example.univer.dto.GroupDto;
import org.example.univer.mappers.GroupMapper;
import org.example.univer.models.Group;
import org.example.univer.services.CathedraService;
import org.example.univer.services.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {
    private MockMvc mockMvc;
    private GroupService groupService;
    private GroupMapper groupMapper;
    private GroupController groupController;
    private CathedraService cathedraService;

    @BeforeEach
    void setUp() {
        groupService = mock(GroupService.class);
        groupMapper = mock(GroupMapper.class);
        cathedraService =  mock(CathedraService.class);
        groupController = new GroupController(groupService, cathedraService,groupMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void whenFindAll_thenAllGroupsReturned() throws Exception {
        Group group = new Group();
        GroupDto dto = new GroupDto();
        when(groupService.findAll()).thenReturn(List.of(group));
        when(groupMapper.toDto(group)).thenReturn(dto);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/index"))
                .andExpect(model().attributeExists("groupsDto"))
                .andExpect(model().attribute("title", "All Groups"));
    }

    @Test
    void whenGetCreateForm_thenEmptyFormReturned() throws Exception {
        mockMvc.perform(get("/groups/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/new"))
                .andExpect(model().attributeExists("groupDto"));
    }

    @Test
    void whenPostNewValidGroup_thenRedirectToIndex() throws Exception {
        GroupDto dto = new GroupDto();
        Group entity = new Group();
        when(groupMapper.toEntity(any(GroupDto.class))).thenReturn(entity);

        mockMvc.perform(post("/groups")
                        .param("name", "Alpa")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).create(any(Group.class));
    }


    @Test
    void whenEditGroup_thenGroupReturnedToForm() throws Exception {
        Group group = new Group();
        GroupDto dto = new GroupDto();
        when(groupService.findById(1L)).thenReturn(Optional.of(group));
        when(groupMapper.toDto(group)).thenReturn(dto);

        mockMvc.perform(get("/groups/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/edit"))
                .andExpect(model().attributeExists("groupDto"));
    }

    @Test
    void whenUpdateValidGroup_thenRedirectToIndex() throws Exception {
        GroupDto dto = new GroupDto();
        Group entity = new Group();
        when(groupMapper.toEntity(any(GroupDto.class))).thenReturn(entity);

        mockMvc.perform(patch("/groups/1")
                        .param("name", "Alpa"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).update(any(Group.class));
    }

    @Test
    void whenFindById_thenGroupReturned() throws Exception {
        Group group = new Group();
        GroupDto dto = new GroupDto();
        when(groupService.findById(1L)).thenReturn(Optional.of(group));
        when(groupMapper.toDto(group)).thenReturn(dto);

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/show"))
                .andExpect(model().attributeExists("groupDto"));
    }

    @Test
    void whenDeleteGroup_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/groups/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).deleteById(1L);
    }
}
