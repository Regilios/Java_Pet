package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcGroup;
import org.example.univer.models.Group;
import org.example.univer.services.GroupService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @Mock
    private JdbcGroup mockJdbcGroup;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(groupService, "minLengthNameAudience", 3);
    }
    @Test
    void create_groupNameMore3_createGroup() {
        Group group = new Group();
        group.setId(1L);
        group.setNameGroup("Абривель");

        when(mockJdbcGroup.isSingle(group)).thenReturn(false);
        groupService.create(group);

        verify(mockJdbcGroup, times(1)).create(group);
    }

    @Test
    void create_groupNameLess3_throwException() {
        Group group = new Group();
        group.setId(1L);
        group.setNameGroup("Аб");

        when(mockJdbcGroup.isSingle(group)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.validate(group, GroupService.ValidationContext.METHOD_CREATE);
            groupService.create(group);
        });
        verify(mockJdbcGroup, never()).create(any(Group.class));
    }


    @Test
    void isSingle_groupIsSingle_true() {
        Group group = new Group();
        group.setId(1L);

        when(mockJdbcGroup.isSingle(group)).thenReturn(true);
        assertTrue(groupService.isSingle(group));

        verify(mockJdbcGroup, times(1)).isSingle(any(Group.class));
    }

    @Test
    void deleteById_deletedGroup_deleted() {
        groupService.deleteById(1L);

        verify(mockJdbcGroup, times(1)).deleteById(1L);
    }

    @Test
    void findById_findGroup_found() {
        Group group = new Group();
        group.setId(1L);
        when(mockJdbcGroup.findById(1L)).thenReturn(group);
        Group result = groupService.findById(1L);

        assertEquals(group, result);
    }

    @Test
    void findAll_findAllAGroups_foundAll() {
        List<Group> groupsList = List.of(new Group(), new Group());

        when(mockJdbcGroup.findAll()).thenReturn(groupsList);
        List<Group> result = groupService.findAll();

        assertEquals(groupsList, result);
    }
}
