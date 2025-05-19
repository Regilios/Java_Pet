package org.example.univer.controllers;

import org.example.univer.models.*;
import org.example.univer.services.*;
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
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LectureControllerTest {
    private MockMvc mockMvc;
    @Mock
    private LectureService lectureService;
    @Mock
    private CathedraService cathedraService;
    @Mock
    private TeacherService teacherService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private LectureTimeService lectureTimeService;
    @Mock
    private AudienceService audienceService;
    @Mock
    private GroupService groupService;
    @InjectMocks
    private LectureController lectureController;
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    public void setUp() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 3));

        mockMvc = MockMvcBuilders.standaloneSetup(lectureController)
                .setConversionService(conversionService)
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    public void whenGetAllLectures_thenAllLecturesReturned() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Андромеда");

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));

        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");

        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setRoomNumber(1);
        audience.setCapacity(100);

        LectureTime mockLectureTime = new LectureTime();
        mockLectureTime.setStartLecture(LocalDateTime.parse("2025-03-02 14:30:00", formatter1));
        mockLectureTime.setEndLecture(LocalDateTime.parse("2025-03-02 16:30:00", formatter1));

        Lecture lecture1 = new Lecture();
        lecture1.setCathedra(cathedra);
        lecture1.setTeacher(teacher);
        lecture1.setSubject(subject);
        lecture1.setTime(lectureTime);
        lecture1.setAudience(audience);

        Lecture lecture2 = new Lecture();
        lecture2.setCathedra(cathedra);
        lecture2.setTeacher(teacher);
        lecture2.setSubject(subject);
        lecture2.setTime(lectureTime);
        lecture2.setAudience(audience);

        lectureService.create(lecture1);

        List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
        Page<Lecture> page = new PageImpl<>(lectures, PageRequest.of(0, 1), 2);
        when(lectureService.findAllWithGroup(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/lectures"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/index"))
                .andExpect(forwardedUrl("lectures/index"))
                .andExpect(model().attribute("lectures", page))
                .andDo(print());
    }

    @Test
    public void whenGetOneLecture_thenOneLectureReturned() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Андромеда");

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));

        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");

        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setRoomNumber(1);
        audience.setCapacity(100);

        LectureTime mockLectureTime = new LectureTime();
        mockLectureTime.setStartLecture(LocalDateTime.parse("2025-03-02 14:30:00", formatter1));
        mockLectureTime.setEndLecture(LocalDateTime.parse("2025-03-02 16:30:00", formatter1));

        Lecture lecture = new Lecture();
        lecture.setCathedra(cathedra);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);
        lecture.setAudience(audience);

        lectureService.create(lecture);

        when(lectureService.findById(1L)).thenReturn(Optional.of(lecture));

        mockMvc.perform(get("/lectures/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/show"))
                .andExpect(model().attributeExists("lecture"))
                .andExpect(model().attribute("lecture", lecture))
                .andExpect(forwardedUrl("lectures/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewLecture_thenNewLectureCreated() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Андромеда");

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));

        Group group1 = new Group();
        group1.setName("Абривель");

        Group group2 = new Group();
        group2.setName("Абривель");

        List<Group> groups = Arrays.asList(group1, group2);

        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");

        LectureTime time = new LectureTime();
        time.setStartLecture(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        time.setEndLecture(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setRoomNumber(1);
        audience.setCapacity(100);

        LectureTime mockLectureTime = new LectureTime();
        mockLectureTime.setStartLecture(LocalDateTime.parse("2025-03-02 14:30:00", formatter1));
        mockLectureTime.setEndLecture(LocalDateTime.parse("2025-03-02 16:30:00", formatter1));

        when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
        when(subjectService.findAll()).thenReturn(Arrays.asList(subject));
        when(lectureTimeService.findAll()).thenReturn(Arrays.asList(time));
        when(audienceService.findAll()).thenReturn(Arrays.asList(audience));
        when(groupService.findAll()).thenReturn(groups);

        mockMvc.perform(get("/lectures/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/new"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("cathedras"))
                .andExpect(model().attributeExists("subjects"))
                .andExpect(model().attributeExists("times"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("audiences"))
                .andExpect(model().attributeExists("lecture"))
                .andDo(print());

        verify(teacherService, times(1)).findAll();
        verify(cathedraService, times(1)).findAll();
        verify(subjectService, times(1)).findAll();
        verify(lectureTimeService, times(1)).findAll();
        verify(audienceService, times(1)).findAll();
        verify(groupService, times(1)).findAll();
    }

    @Test
    void whenEditLecture_thenLectureFound() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Андромеда");

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));

        Group group1 = new Group();
        group1.setName("Абривель");

        Group group2 = new Group();
        group2.setName("Абривель");

        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");

        LectureTime time = new LectureTime();
        time.setStartLecture(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        time.setEndLecture(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setRoomNumber(1);
        audience.setCapacity(100);

        LectureTime mockLectureTime = new LectureTime();
        mockLectureTime.setStartLecture(LocalDateTime.parse("2025-03-02 14:30:00", formatter1));
        mockLectureTime.setEndLecture(LocalDateTime.parse("2025-03-02 16:30:00", formatter1));

        List<Cathedra> cathedraList = Arrays.asList(cathedra);
        List<Teacher> teacherList = Arrays.asList(teacher);
        List<Subject> subjectList = Arrays.asList(subject);
        List<LectureTime> timeList = Arrays.asList(time);
        List<Group> groupList = Arrays.asList(group1, group2);
        List<Audience> audienceList = Arrays.asList(audience);

        Lecture lecture = new Lecture();
        lecture.setCathedra(cathedra);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(time);
        lecture.setAudience(audience);

        when(teacherService.findAll()).thenReturn(Arrays.asList(teacher));
        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
        when(subjectService.findAll()).thenReturn(Arrays.asList(subject));
        when(lectureTimeService.findAll()).thenReturn(Arrays.asList(time));
        when(audienceService.findAll()).thenReturn(Arrays.asList(audience));
        when(lectureService.findById(1L)).thenReturn(Optional.of(lecture));

        mockMvc.perform(get("/lectures/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("lectures/edit"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("cathedras"))
                .andExpect(model().attributeExists("subjects"))
                .andExpect(model().attributeExists("times"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("audiences"))
                .andExpect(model().attributeExists("lecture"))
                .andExpect(forwardedUrl("lectures/edit"))
                .andExpect(model().attribute("teachers", teacherList))
                .andExpect(model().attribute("cathedras", cathedraList))
                .andExpect(model().attribute("subjects", subjectList))
                .andExpect(model().attribute("times", timeList))
                .andExpect(model().attribute("audiences", audienceList))
                .andExpect(model().attribute("lecture", lecture))
                .andDo(print());

        verify(lectureService, times(1)).findById(1L);
        verify(teacherService, times(1)).findAll();
        verify(cathedraService, times(1)).findAll();
        verify(subjectService, times(1)).findAll();
        verify(lectureTimeService, times(1)).findAll();
        verify(audienceService, times(1)).findAll();
        verify(groupService, times(1)).findAll();
    }


    @Test
    public void whenUpdateLecture_thenLectureUpdated() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Андромеда");

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));

        Subject subject = new Subject();
        subject.setName("Test");
        subject.setDescription("Test Test Test Test Test Test Test");

        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setRoomNumber(1);
        audience.setCapacity(100);

        LectureTime mockLectureTime = new LectureTime();
        mockLectureTime.setStartLecture(LocalDateTime.parse("2025-03-02 14:30:00", formatter1));
        mockLectureTime.setEndLecture(LocalDateTime.parse("2025-03-02 16:30:00", formatter1));

        Group group1 = new Group();
        group1.setName("Абривель");

        Group group2 = new Group();
        group2.setName("Абривель");
        List<Long> groups = Arrays.asList(group1.getId(), group2.getId());

        Lecture lecture = new Lecture();
        lecture.setCathedra(cathedra);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);
        lecture.setAudience(audience);


        mockMvc.perform(patch("/lectures/{id}", 1)
                        .flashAttr("lecture", lecture)
                        .param("groups", "1", "2")
                        .sessionAttr("model", new RedirectAttributesModelMap()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lectures"))
                .andDo(print());

        verify(lectureService, times(1)).update(lecture);
    }

    @Test
    void whenDeleteLecture_thenLectureDeleted() throws Exception {
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        mockMvc.perform(delete("/lectures/{id}", 1))
                .andExpect(redirectedUrl("/lectures"));

        verify(lectureService).deleteById(1L);
    }
}
