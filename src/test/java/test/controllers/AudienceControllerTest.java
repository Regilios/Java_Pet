package test.controllers;

import org.example.univer.controllers.AudienceController;
import org.example.univer.models.Audience;
import org.example.univer.services.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Disabled
@ExtendWith(MockitoExtension.class)
public class AudienceControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AudienceService audienceService;
    @InjectMocks
    private AudienceController audienceController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Map<String, Integer> mockRoomSettings = Map.of("SIZE_MIN", 20, "SIZE_MAX", 100);
        ReflectionTestUtils.setField(audienceService, "roomSettings", mockRoomSettings);
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(audienceController).setCustomArgumentResolvers(resolver).build();
    }

    @Test
    public void whenGetAllAudiences_thenAllAudiencesReturned() throws Exception {
        Audience audience1 = new Audience();
        audience1.setRoom(1);
        audience1.setCapacity(50);
        audienceService.create(audience1);

        Audience audience2 = new Audience();
        audience2.setRoom(3);
        audience2.setCapacity(33);
        audienceService.create(audience2);

        List<Audience> audiences = Arrays.asList(audience1, audience2);
        Page<Audience> page = new PageImpl<>(audiences, PageRequest.of(0, 1), 2);
        when(audienceService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/audiences"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/index"))
                .andExpect(forwardedUrl("audiences/index"))
                .andExpect(model().attribute("audiences", page))
                .andDo(print());
    }

    @Test
    public void whenGetOneAudience_thenOneAudienceReturned() throws Exception {
        Audience audience = new Audience();
        audience.setRoom(1);
        audience.setCapacity(50);
        audienceService.create(audience);

        when(audienceService.findById(1L).orElse(null)).thenReturn(audience);

        mockMvc.perform(get("/audiences/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/show"))
                .andExpect(model().attributeExists("audience"))
                .andExpect(model().attribute("audience", audience))
                .andExpect(forwardedUrl("audiences/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewAudience_thenNewAudienceCreated() throws Exception {
        mockMvc.perform(get("/audiences/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/new"))
                .andExpect(model().attributeExists("audience"))
                .andDo(print());
    }

    @Test
    void whenEditAudience_thenAudienceFound() throws Exception {
        Audience audience = new Audience();
        audience.setRoom(1);
        audience.setCapacity(50);

        when(audienceService.findById(1L)).thenReturn(Optional.of(audience));

        mockMvc.perform(get("/audiences/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/edit"))
                .andExpect(model().attributeExists("audience"))
                .andExpect(forwardedUrl("audiences/edit"))
                .andExpect(model().attribute("audience", audience))
                .andDo(print());

        verify(audienceService, times(1)).findById(1L);
    }

    @Test
    public void whenUpdateAudience_thenAudienceUpdated() throws Exception {
        Audience audience = new Audience();
        audience.setRoom(1);
        audience.setCapacity(50);

        mockMvc.perform(patch("/audiences/{id}", 1)
                        .flashAttr("audience", audience))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/audiences"))
                .andDo(print());

        verify(audienceService, times(1)).update(audience);
    }

    @Test
    void whenDeleteAudience_thenGAudienceDeleted() throws Exception {
        Audience audience = new Audience();
        audience.setId(1L);
        mockMvc.perform(delete("/audiences/{id}", 1))
                .andExpect(redirectedUrl("/audiences"));
        verify(audienceService).deleteById(1L);
    }
}
