package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.univer.controllers.rest.AudienceRestController;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class AudienceRestControllerTest {
    @InjectMocks
    private AudienceRestController controller;
    @Mock
    AudienceService audienceService;
    @Mock
    AudienceMapper audienceMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private AudienceDto audienceDto;
    private Audience audience;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        audienceDto = new AudienceDto();
        audienceDto.setId(1L);
        audienceDto.setRoomNumber(255);
        audienceDto.setCapacity(30);

        audience = new Audience();
        audience.setId(1L);
        audience.setRoomNumber(255);
        audience.setCapacity(30);
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(audienceService.findAll()).thenReturn(List.of(audience));
        when(audienceMapper.toDto(audience)).thenReturn(audienceDto);

        mockMvc.perform(get("/api/audiences"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].capacity").value("30"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        when(audienceMapper.toEntity(any())).thenReturn(audience);
        when(audienceService.create(any())).thenReturn(audience);
        when(audienceMapper.toDto(any())).thenReturn(audienceDto);

        mockMvc.perform(post("/api/audiences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audienceDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.capacity").value("30"));

        verify(audienceService).create(audience);
    }

    @Test
    void whenGetById_thenReturnAudience() throws Exception {
        when(audienceService.findById(1L)).thenReturn(Optional.of(audience));
        when(audienceMapper.toDto(audience)).thenReturn(audienceDto);

        mockMvc.perform(get("/api/audiences/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value("30"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        when(audienceService.existsById(1L)).thenReturn(true);
        when(audienceMapper.toEntity(any())).thenReturn(audience);
        when(audienceService.update(any())).thenReturn(audience);
        when(audienceMapper.toDto(any())).thenReturn(audienceDto);

        audienceDto.setCapacity(40);

        mockMvc.perform(put("/api/audiences/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(audienceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value("40"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(audienceService.existsById(1L)).thenReturn(true);
        doNothing().when(audienceService).deleteById(1L);

        mockMvc.perform(delete("/api/audiences/1"))
                .andExpect(status().isNoContent());
    }
}