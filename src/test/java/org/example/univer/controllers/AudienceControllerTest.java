package org.example.univer.controllers;

import org.example.univer.dto.AudienceDto;
import org.example.univer.mappers.AudienceMapper;
import org.example.univer.models.Audience;
import org.example.univer.services.AudienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AudienceControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AudienceService audienceService;
    @Mock
    private AudienceMapper audienceMapper;
    @InjectMocks
    private AudienceController audienceController;

    @BeforeEach
    void setUp() {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));
        mockMvc = MockMvcBuilders
                .standaloneSetup(audienceController)
                .setCustomArgumentResolvers(pageableResolver)
                .build();
    }

    @Test
    void whenFindAllAudiences_thenAllAudiencesReturned() throws Exception {
        Audience audience = new Audience();
        AudienceDto dto = new AudienceDto();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Audience> audiencePage = new PageImpl<>(List.of(audience), pageable, 1);

        when(audienceService.findAll(any(Pageable.class))).thenReturn(audiencePage);
        when(audienceMapper.toDto(audience)).thenReturn(dto);

        mockMvc.perform(get("/audiences").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/index"))
                .andExpect(model().attributeExists("audiencesDto"))
                .andExpect(model().attribute("title", "All Audiences"));
    }

    @Test
    void whenGetCreateForm_thenEmptyFormReturned() throws Exception {
        mockMvc.perform(get("/audiences/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/new"))
                .andExpect(model().attributeExists("audienceDto"))
                .andExpect(model().attribute("title", "All Audiences"));
    }

    @Test
    void whenPostNewValidAudience_thenRedirectToIndex() throws Exception {
        AudienceDto dto = new AudienceDto();
        Audience entity = new Audience();
        when(audienceMapper.toEntity(any())).thenReturn(entity);

        mockMvc.perform(post("/audiences")
                        .param("roomNumber", "101")
                        .param("capacity", "50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/audiences"));

        verify(audienceService).create(any(Audience.class));
    }

    @Test
    void whenEditAudience_thenAudienceReturnedToForm() throws Exception {
        Audience audience = new Audience();
        AudienceDto dto = new AudienceDto();
        when(audienceService.findById(1L)).thenReturn(Optional.of(audience));
        when(audienceMapper.toDto(audience)).thenReturn(dto);

        mockMvc.perform(get("/audiences/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/edit"))
                .andExpect(model().attributeExists("audienceDto"));
    }

    @Test
    void whenUpdateValidAudience_thenRedirectToIndex() throws Exception {
        AudienceDto dto = new AudienceDto();
        Audience entity = new Audience();
        when(audienceMapper.toEntity(any())).thenReturn(entity);

        mockMvc.perform(patch("/audiences/1")
                        .param("roomNumber", "101")
                        .param("capacity", "50"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/audiences"));

        verify(audienceService).update(any(Audience.class));
    }

    @Test
    void whenFindById_thenAudienceReturned() throws Exception {
        Audience audience = new Audience();
        AudienceDto dto = new AudienceDto();
        when(audienceService.findById(1L)).thenReturn(Optional.of(audience));
        when(audienceMapper.toDto(audience)).thenReturn(dto);

        mockMvc.perform(get("/audiences/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("audiences/show"))
                .andExpect(model().attributeExists("audienceDto"));
    }

    @Test
    void whenDeleteAudience_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/audiences/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/audiences"));

        verify(audienceService).deleteById(1L);
    }

}
