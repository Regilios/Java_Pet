package test.service;

import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.exeption.AudienceExeption;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AudienceServiceTest {
    @Mock
    private DaoAudienceInterface mockAudience;

    @InjectMocks
    private AudienceService audienceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(audienceService, "minSize", 20);
        ReflectionTestUtils.setField(audienceService, "maxSize", 100);
    }

    @Test
    void create_audienceCapacity50_createAudience() {
        Audience audience = new Audience();
        audience.setRoom(1);
        audience.setCapacity(50);

        when(mockAudience.isSingle(audience)).thenReturn(false);
        audienceService.create(audience);

        verify(mockAudience, times(1)).create(audience);
    }

    @Test
    void create_audienceCapacity200_throwException() {
        Audience audience = new Audience();
        audience.setRoom(1);
        audience.setCapacity(200);

        when(mockAudience.isSingle(audience)).thenReturn(false);

        assertThrows(AudienceExeption.class, () -> {
            audienceService.validate(audience, AudienceService.ValidationContext.METHOD_CREATE);
            audienceService.create(audience);
        });
        verify(mockAudience, never()).create(any(Audience.class));
    }

    @Test
    void isSingle_audienceIsSingle_true() {
        Audience audience = new Audience();

        when(mockAudience.isSingle(audience)).thenReturn(true);
        assertTrue(audienceService.isSingle(audience));

        verify(mockAudience, times(1)).isSingle(any(Audience.class));
    }

    @Test
    void deleteById_deletedAudience_deleted() {
        Audience audience = new Audience();
        audienceService.deleteById(audience);

        verify(mockAudience, times(1)).deleteById(audience);
    }

    @Test
    void findById_findAudience_found() {
        Audience audience = new Audience();

        when(mockAudience.findById(1L)).thenReturn(Optional.of(audience));
        Optional<Audience> result = audienceService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(audience, result.get());
    }

    @Test
    void findAll_findAllAudiences_foundAll() {
        List<Audience> audiencesList = List.of(new Audience(), new Audience());

        when(mockAudience.findAll()).thenReturn(audiencesList);
        List<Audience> result = audienceService.findAll();

        assertEquals(audiencesList, result);
    }
}
