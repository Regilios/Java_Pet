package test.controllers;

import org.example.univer.controllers.HolidayController;
import org.example.univer.models.Holiday;
import org.example.univer.services.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Disabled
@ExtendWith(MockitoExtension.class)
public class HolidayControllerTest {
    private MockMvc mockMvc;

    @Mock
    private HolidayService holidayService;

    @InjectMocks
    private HolidayController holidayController;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).build();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(holidayService, "maxDayHoliday", 14);
        ReflectionTestUtils.setField(holidayService, "startDayHoliday", "MONDAY");
    }

    @Test
    public void whenGetAllHolidays_thenAllHolidaysReturned() throws Exception {
        Holiday holiday1 = new Holiday();
        holiday1.setDescription("test");
        holiday1.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday1.setEnd_holiday(LocalDate.parse("2024-01-30"));
        holidayService.create(holiday1);

        Holiday holiday2 = new Holiday();
        holiday2.setDescription("test");
        holiday2.setStart_holiday(LocalDate.parse("2024-01-02"));
        holiday2.setEnd_holiday(LocalDate.parse("2024-02-03"));
        holidayService.create(holiday2);

        List<Holiday> holidays = Arrays.asList(holiday1, holiday2);

        when(holidayService.findAll()).thenReturn(holidays);

        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/index"))
                .andExpect(forwardedUrl("holidays/index"))
                .andExpect(model().attribute("holidays", holidays))
                .andDo(print());
    }

    @Test
    public void whenGetOneHoliday_thenOneGroupReturned() throws Exception {
        Holiday holiday = new Holiday();
        holiday.setDescription("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-30"));
        holidayService.create(holiday);

        when(holidayService.findById(1L)).thenReturn(Optional.of(holiday));

        mockMvc.perform(get("/holidays/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/show"))
                .andExpect(model().attributeExists("holiday"))
                .andExpect(model().attribute("holiday", holiday))
                .andExpect(forwardedUrl("holidays/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewHoliday_thenNewHolidayCreated() throws Exception {
        mockMvc.perform(get("/holidays/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/new"))
                .andExpect(model().attributeExists("holiday"))
                .andDo(print());
    }

    @Test
    void whenEditHoliday_thenHolidayFound() throws Exception {
        Holiday holiday = new Holiday();
        holiday.setDescription("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-30"));

        when(holidayService.findById(1L)).thenReturn(Optional.of(holiday));

        mockMvc.perform(get("/holidays/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/edit"))
                .andExpect(model().attributeExists("holiday"))
                .andExpect(forwardedUrl("holidays/edit"))
                .andExpect(model().attribute("holiday", holiday))
                .andDo(print());

        verify(holidayService, times(1)).findById(1L);
    }

    @Test
    public void whenUpdateHoliday_thenHolidayUpdated() throws Exception {
        Holiday holiday = new Holiday();
        holiday.setDescription("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-30"));

        mockMvc.perform(patch("/holidays/{id}", 1)
                        .flashAttr("holiday", holiday))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/holidays"))
                .andDo(print());

        verify(holidayService, times(1)).update(holiday);
    }

    @Test
    void whenDeleteHoliday_thenHolidayDeleted() throws Exception {
        Holiday holiday = new Holiday();
        holiday.setId(1L);
        mockMvc.perform(delete("/holidays/{id}", 1))
                .andExpect(redirectedUrl("/holidays"));

        verify(holidayService).deleteById(holiday);
    }

}
