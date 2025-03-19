package org.example.univer.test.controllers;

import org.example.univer.controllers.GroupController;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Group;
import org.example.univer.services.CathedraService;
import org.example.univer.services.GroupService;
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

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {
    private MockMvc mockMvc;
    @Mock
    private GroupService groupService;
    @Mock
    private CathedraService cathedraService;
    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(groupService, "minLengthNameGroup", 3);
    }

    @Test
    public void whenGetAllGroups_thenAllGroupsReturned() throws Exception {
        Group group1 = new Group();
        group1.setName("test");
        group1.setCathedra(cathedraService.findById(1L));
        groupService.create(group1);

        Group group2 = new Group();
        group2.setName("test2");
        group2.setCathedra(cathedraService.findById(1L));
        groupService.create(group2);

        List<Group> groups = Arrays.asList(group1, group2);

        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/index"))
                .andExpect(forwardedUrl("groups/index"))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attribute("groups", notNullValue()))
                .andExpect(model().attribute("groups", hasItems(group1, group2)))
                .andDo(print());
    }

    @Test
    public void whenGetOneGroup_thenOneGroupReturned() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        Group group = new Group();
        group.setName("test");
        group.setCathedra(cathedra);
        groupService.create(group);

        when(groupService.findById(1L)).thenReturn(group);

        mockMvc.perform(get("/groups/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/show"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", group))
                .andExpect(forwardedUrl("groups/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewGroup_thenNewGroupCreated() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");
        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));

        mockMvc.perform(get("/groups/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/new"))
                .andExpect(model().attributeExists("cathedras"))
                .andExpect(model().attributeExists("group"))
                .andDo(print());

        verify(cathedraService, times(1)).findAll();
    }

    @Test
    void whenEditGroup_thenGroupFound() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        Group group = new Group();
        group.setName("test");
        group.setCathedra(cathedra);
        List<Cathedra> cathedrasList = List.of(cathedra);

        when(groupService.findById(1L)).thenReturn(group);
        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));

        mockMvc.perform(get("/groups/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("groups/edit"))
                .andExpect(model().attributeExists("cathedras"))
                .andExpect(model().attributeExists("group"))
                .andExpect(forwardedUrl("groups/edit"))
                .andExpect(model().attribute("cathedras", cathedrasList))
                .andExpect(model().attribute("group", group))
                .andDo(print());

        verify(groupService, times(1)).findById(1L);
        verify(cathedraService, times(1)).findAll();
    }

    @Test
    public void whenUpdateGroup_thenGroupUpdated() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        Group group = new Group();
        group.setName("test");
        group.setCathedra(cathedra);

        mockMvc.perform(patch("/groups/{id}", 1)
                        .flashAttr("group", group))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/groups"))
                .andDo(print());

        verify(groupService, times(1)).update(group);
    }

    @Test
    void whenDeleteGroup_thenGroupDeleted() throws Exception {
        mockMvc.perform(delete("/groups/{id}", 1))
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).deleteById(1L);
    }
}
