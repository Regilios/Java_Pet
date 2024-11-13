package org.example.univer;

import org.example.univer.config.SpringConfig;
import org.example.univer.formatter.Formatter;
import org.example.univer.dao.jdbc.*;
import org.example.univer.models.*;
import org.example.univer.services.AudienceService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CheckFunction {
    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
    private JdbcStudent jdbcStudent = context.getBean(JdbcStudent.class);
    private JdbcGroup jdbcGroup = context.getBean(JdbcGroup.class);
    private JdbcCathedra jdbcCathedra = context.getBean(JdbcCathedra.class);
    private JdbcTeacher jdbcTeacher = context.getBean(JdbcTeacher.class);
    private JdbcVacation jdbcVacation = context.getBean(JdbcVacation.class);
    private JdbcSubject jdbcSubject = context.getBean(JdbcSubject.class);
    private JdbsLectureTime jdbsLectureTime = context.getBean(JdbsLectureTime.class);
    private JdbcAudience jdbcAudience = context.getBean(JdbcAudience.class);
    private JdbcHoliday jdbcHoliday = context.getBean(JdbcHoliday.class);
    private JdbcLecture jdbcLecture = context.getBean(JdbcLecture.class);

    private AudienceService audienceService = context.getBean(AudienceService.class);

    private Cathedra cathedra = context.getBean("cathedra", Cathedra.class);
    private Group grop = context.getBean("group", Group.class);
    private Student student = context.getBean("student", Student.class);
    private Teacher teacher = context.getBean("teacher", Teacher.class);
    private Vacation vacation = context.getBean("vacation", Vacation.class);
    private Subject subject = context.getBean("subject", Subject.class);
    private LectureTime lectureTime = context.getBean("lectureTime", LectureTime.class);
    private Audience audience = context.getBean("audience", Audience.class);
    private Holiday holiday = context.getBean("holiday", Holiday.class);
    private Lecture lecture = context.getBean("lecture", Lecture.class);
    private Formatter formatter = context.getBean(Formatter.class);

    List<Student> studentsList = jdbcStudent.findAll();
    List<Group> groupList = jdbcGroup.findAll();
    List<Teacher> teacherList = jdbcTeacher.findAll();
    List<Vacation> vacationList = jdbcVacation.findAll();
    List<Subject> subjectList = jdbcSubject.findAll();
    List<LectureTime> lectureTimes = jdbsLectureTime.findAll();
    List<Audience> audiences = jdbcAudience.findAll();
    List<Holiday> holidays = jdbcHoliday.findAll();
    List<Lecture> lectures = jdbcLecture.findAll();

    public void startCheck() {
//        System.out.println(formatter.formatListStudent(studentsList));
//        System.out.println();
//        System.out.println(formatter.formatGroupList(groupList));
//        System.out.println();
//        System.out.println(formatter.formatListTeacher(teacherList));
//        System.out.println();
//        System.out.println(formatter.formatListVacation(vacationList));
//        System.out.println();
//        System.out.println(formatter.formatListSubject(subjectList));
//        System.out.println();
//        System.out.println(formatter.formatListLectionTime(lectureTimes));
//        System.out.println();
//        System.out.println(formatter.formatAudience(audiences));
//        System.out.println();
//        System.out.println(formatter.formatListHoliday(holidays));
//        System.out.println();
//        System.out.println(formatter.formatListLecture(lectures));
//        System.out.println();
//        System.out.println(formatter.formatListLecture(getTimetableDay(jdbcStudent.findById(1L), LocalDate.parse("2024-02-02"))));
//        System.out.println();
//        System.out.println(formatter.formatListLecture(getTimetableDay(jdbcTeacher.findById(2L), LocalDate.parse("2024-02-03"))));
//          Audience audience1 = jdbcAudience.findById(1L);
//          audience1.setRoom(402);
//          audienceService.update(audience1);
//        System.out.println(jdbcAudience.findRoom(jdbcAudience.findById(1L)));
        System.out.println(formatter.formatListLecture(jdbcLecture.getTimetable(jdbcStudent.findById(1L), LocalDate.parse("2024-02-02"))));
        System.out.println();
        System.out.println(formatter.formatListLecture(jdbcLecture.getTimetable(jdbcTeacher.findById(1L), LocalDate.parse("2024-02-02"))));
    }
}
