package test.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.exeption.AudienceExeption;
import org.example.univer.models.Audience;
import org.example.univer.services.AudienceService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class AudienceServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoAudienceInterface mockAudience;
    @InjectMocks
    private AudienceService audienceService;

    @BeforeEach
    void setUp() {
        AppSettings.RoomSettings settings = new AppSettings.RoomSettings();
        settings.setSizeMin(20);
        settings.setSizeMax(100);
        appSettings.setRoomSettings(settings);

        audienceService = new AudienceService(mockAudience, appSettings);
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
        audience.setId(1L);
        audienceService.deleteById(1L);

        verify(mockAudience, times(1)).deleteById(1L);
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
