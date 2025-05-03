package test.controllers;

import org.example.univer.controllers.LectureTimeController;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LectureTimeControllerTest {
    private MockMvc mockMvc;
    @Mock
    private LectureTimeService lectureTimeService;
    @InjectMocks
    private LectureTimeController lectureTimeController;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureTimeController).build();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(lectureTimeService, "minimumLectureTimeMinutes", 30);
    }
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void whenGetAllLectureTimes_thenAllLectureTimesReturned() throws Exception {
        LectureTime lectureTime1 = new LectureTime();
        lectureTime1.setStartLection(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime1.setEndLection(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));
        lectureTimeService.create(lectureTime1);

        LectureTime lectureTime2 = new LectureTime();
        lectureTime2.setStartLection(LocalDateTime.parse("2026-02-02 14:30:00", formatter1));
        lectureTime2.setEndLection(LocalDateTime.parse("2026-02-02 16:30:00", formatter1));
        lectureTimeService.create(lectureTime2);

        List<LectureTime> lectureTimes = Arrays.asList(lectureTime1, lectureTime2);

        when(lectureTimeService.findAll()).thenReturn(lectureTimes);

        mockMvc.perform(get("/lecturetimes"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/index"))
                .andExpect(forwardedUrl("lecturetimes/index"))
                .andExpect(model().attribute("lectureTimes", lectureTimes))
                .andDo(print());
    }

    @Test
    public void whenGetOneLectureTime_thenOneLectureTimeReturned() throws Exception {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLection(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEndLection(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));
        lectureTimeService.create(lectureTime);

        when(lectureTimeService.findById(1L)).thenReturn(Optional.of(lectureTime));

        mockMvc.perform(get("/lecturetimes/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/show"))
                .andExpect(model().attributeExists("lectureTime"))
                .andExpect(model().attribute("lectureTime", lectureTime))
                .andExpect(forwardedUrl("lecturetimes/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewLectureTime_thenNewLectureTimeCreated() throws Exception {
        mockMvc.perform(get("/lecturetimes/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/new"))
                .andExpect(model().attribute("lectureTime", instanceOf(LectureTime.class)))
                .andDo(print());
    }

    @Test
    void whenEditLectureTime_thenLectureTimeFound() throws Exception {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLection(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEndLection(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        when(lectureTimeService.findById(1L)).thenReturn(Optional.of(lectureTime));

        mockMvc.perform(get("/lecturetimes/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/edit"))
                .andExpect(model().attributeExists("lectureTime"))
                .andExpect(forwardedUrl("lecturetimes/edit"))
                .andExpect(model().attribute("lectureTime", lectureTime))
                .andDo(print());

        verify(lectureTimeService, times(1)).findById(1L);
    }

    @Test
    public void whenUpdateLectureTime_thenLectureTimeUpdated() throws Exception {
        Long id = 1L;
        String startDate = "2023-10-01";
        String startTime = "10:00";
        String endDate = "2023-10-01";
        String endTime = "12:00";


        mockMvc.perform(patch("/lecturetimes/{id}", id)
                        .param("start_date", startDate)
                        .param("start_time", startTime)
                        .param("end_date", endDate)
                        .param("end_time", endTime))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturetimes"));

        LocalDateTime startLection = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.parse(startTime));
        LocalDateTime endLection = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.parse(endTime));

        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(id);
        lectureTime.setStartLection(startLection);
        lectureTime.setEndLection(endLection);

        verify(lectureTimeService, times(1)).update(lectureTime);
    }

    @Test
    void whenDeleteLectureTime_thenLectureTimeDeleted() throws Exception {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        mockMvc.perform(delete("/lecturetimes/{id}", 1))
                .andExpect(redirectedUrl("/lecturetimes"));

        verify(lectureTimeService).deleteEntity(lectureTime);
    }
}
