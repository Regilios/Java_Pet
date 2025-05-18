package test.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoGroupInterface;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.models.Group;
import org.example.univer.services.GroupService;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoGroupInterface mockGroup;
    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        appSettings.setMinLengthNameGroup(3);
        groupService = new GroupService(mockGroup, appSettings);
    }

    @Test
    void create_groupNameMore3_createGroup() {
        Group group = new Group();
        group.setName("Абривель");

        when(mockGroup.isSingle(group)).thenReturn(false);
        groupService.create(group);

        verify(mockGroup, times(1)).create(group);
    }

    @Test
    void create_groupNameLess3_throwException() {
        Group group = new Group();
        group.setName("Аб");

        when(mockGroup.isSingle(group)).thenReturn(false);
        assertThrows(InvalidParameterException.class, () -> {
            groupService.validate(group, GroupService.ValidationContext.METHOD_CREATE);
            groupService.create(group);
        });
        verify(mockGroup, never()).create(any(Group.class));
    }


    @Test
    void isSingle_groupIsSingle_true() {
        Group group = new Group();

        when(mockGroup.isSingle(group)).thenReturn(true);
        assertTrue(groupService.isSingle(group));

        verify(mockGroup, times(1)).isSingle(any(Group.class));
    }

    @Test
    void deleteById_deletedGroup_deleted() {
        Group group = new Group();
        group.setId(1L);
        groupService.deleteById(1L);

        verify(mockGroup, times(1)).deleteById(1L);
    }

    @Test
    void findById_findGroup_found() {
        Group group = new Group();

        when(mockGroup.findById(1L)).thenReturn(Optional.of(group));
        Optional<Group> result = groupService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(group, result.get());
    }

    @Test
    void findAll_findAllAGroups_foundAll() {
        List<Group> groupsList = List.of(new Group(), new Group());

        when(mockGroup.findAll()).thenReturn(groupsList);
        assertThat(groupsList, is(not(empty())));
        assertThat(groupsList.size(), is(greaterThan(0)));
        List<Group> result = groupService.findAll();

        assertEquals(groupsList, result);
    }
}
