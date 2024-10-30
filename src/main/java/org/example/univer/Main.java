package org.example.univer;

import org.example.univer.dao.formatter.Formatter;
import org.example.univer.dao.jdbc.*;
import org.example.univer.dao.models.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.example.univer.config.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CheckFunction checkFunction = new CheckFunction();
        checkFunction.startCheck();
        // новая группа
//        grop.setNameGroup("Aurora");
//        grop.setCathedra(jdbcCathedra.findById(1L));
//        jdbcGroup.create(grop);
//
//        // новый студент
//        student.setFirstName("Pavel");
//        student.setLastName("Yarinov");
//        student.setGender("MALE");
//        student.setAddres("Armany str 24");
//        student.setEmail("pavel@gmail.com");
//        student.setPhone("8978474666");
//        student.setBirthday(LocalDate.of(1991,10,17));
//        student.setGroup(jdbcGroup.findById(3L));
//        jdbcStudent.create(student);

        //обновляем данные студента
//        Student st = jdbcStudent.findById(1L);
//        st.setEmail("1111@ffff");
//        jdbcStudent.update(st);


        //получаем список студентов 1 группы и внедряем его в группу
//        Group group_1 = jdbcGroup.findById(1L);
//        List<Student> studentList = jdbcStudent.findAllStudentByIdGroup(group_1);
//        group_1.setStudents(studentList);

        // новый учитель
//        teacher.setFirstName("Pavel");
//        teacher.setLastName("Yarinov");
//        teacher.setGender(Gender.FEMALE);
//        teacher.setAddres("Armany str 24");
//        teacher.setEmail("pavel@gmail.com");
//        teacher.setPhone("8978474666");
//        teacher.setBirthday(LocalDate.of(1991,10,17));
//        teacher.setCathedra(jdbcCathedra.findById(1L));
//        jdbcTeacher.create(teacher);

        // обновляем учителя
//        Teacher teacher1 = jdbcTeacher.findById(3L);
//        teacher1.setFirstName("Иван");
//        teacher1.setLastName("Патрушев");
//        jdbcTeacher.update(teacher1);

        // новый предмет
//        subject.setName("Алхимия");
//        subject.setDescription("Варим самогон");
//        jdbcSubject.create(subject);
//
//        Subject subject1 = jdbcSubject.findById(5L);
//        subject1.setName("Базовая алхмия");
//        subject1.setDescription("Теория и практика алхимии");
//        jdbcSubject.update(subject1);

        // добавляем время занятия
//        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        lectureTime.setStart(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
//        lectureTime.setEnd(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));
//        jdbsLectureTime.create(lectureTime);
//
//        // меняем время занятия
//        LectureTime lectureTime1 = jdbsLectureTime.findById(1L);
//        lectureTime1.setStart(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
//        lectureTime1.setEnd(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));
//        jdbsLectureTime.update(lectureTime1);



          //Выводим 1 учителя
//        Teacher teacherList1 = jdbcTeacher.findById(1L);
//        System.out.println(formatter.formatTeacher(teacherList1));

//        System.out.println(formatter.formatListStudent(group_1.getStudents()));
//        System.out.println();

        //обновляем группу
//        Group group_2 = jdbcGroup.findById(3L);
//        group_2.setNameGroup("Парсель");
//        jdbcGroup.update(group_2);
//
//        List<Group> groupList_1 = jdbcGroup.findAll();
//        System.out.println(formatter.formatListGroup(groupList_1));
//        System.out.println();

          // удаление группы и студента
//        jdbcGroup.deleteById(3L);
//        jdbcStudent.deleteById(7L);
//
//        studentsList = jdbcStudent.findAll();
//        groupList = jdbcGroup.findAll();
//
//        System.out.println(formatter.formatListStudent(studentsList));
//        System.out.println();
//        System.out.println(formatter.formatListGroup(groupList));
//        System.out.println();


      //  List<Student> studentsList2 = JdbcGroup.findAllStudentByIdGroup(1L);

    }

}