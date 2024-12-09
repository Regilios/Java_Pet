package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcAudience;
import org.example.univer.models.Audience;
import org.example.univer.services.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AudienceServiceTest {
    @Mock
    private JdbcAudience mockJdbcAudience;

    @InjectMocks
    private AudienceService audienceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String, Integer> mockRoomSettings =  Map.of("SIZE_MIN", 20,"SIZE_MAX", 100);
        ReflectionTestUtils.setField(audienceService, "roomSettings", mockRoomSettings);
    }

    @Test
    void create_audienceCapacity50_createAudience() {
        Audience audience = new Audience();
        audience.setId(1L);
        audience.setRoom(1);
        audience.setCapacity(50);

        when(mockJdbcAudience.isSingle(audience)).thenReturn(false);
        audienceService.create(audience);

        verify(mockJdbcAudience, times(1)).create(audience);
    }

    @Test
    void create_audienceCapacity200_throwException() {
        Audience audience = new Audience();
        audience.setId(1L);
        audience.setRoom(1);
        audience.setCapacity(200);

        when(mockJdbcAudience.isSingle(audience)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            audienceService.validate(audience, AudienceService.ValidationContext.METHOD_CREATE);
            audienceService.create(audience);
        });
        verify(mockJdbcAudience, never()).create(any(Audience.class));
    }

    @Test
    void isSingle_audienceIsSingle_true() {
        Audience audience = new Audience();
        audience.setId(1L);

        when(mockJdbcAudience.isSingle(audience)).thenReturn(true);
        assertTrue(audienceService.isSingle(audience));

        verify(mockJdbcAudience, times(1)).isSingle(any(Audience.class));
    }

    @Test
    void deleteById_deletedAudience_deleted() {
        audienceService.deleteById(1L);

        verify(mockJdbcAudience, times(1)).deleteById(1L);
    }

    @Test
    void findById_findAudience_found() {
        Audience audience = new Audience();
        audience.setId(1L);

        when(mockJdbcAudience.findById(1L)).thenReturn(audience);
        Audience result = audienceService.findById(1L);

        assertEquals(audience, result);
    }

    @Test
    void findAll_findAllAudiences_foundAll() {
        List<Audience> audiencesList = List.of(new Audience(), new Audience());

        when(mockJdbcAudience.findAll()).thenReturn(audiencesList);
        List<Audience> result = audienceService.findAll();

        assertEquals(audiencesList, result);
    }
}
