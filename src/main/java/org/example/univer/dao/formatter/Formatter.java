package org.example.univer.dao.formatter;

import org.example.univer.dao.models.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.lang.String.*;

@Component
public class Formatter {
    private final String COL = "  |  ";

    public String formatListGroup(List<Group> list) {
        int maxColName = getMaxCol(list, Group::getName);
        return list.stream().map(group -> format(
            "ID: %-3s" +
            "GROUP_NAME: %-"+ maxColName + "s" + COL +
            "CATHEDRA: %s",
            group.getId(),
            group.getName(),
            group.getCathedra().getName()
        )).collect(Collectors.joining(System.lineSeparator()));
    }
    public String formatListLecture(List<Lecture> list) {
        int maxColCathedra = getMaxCol(list, Lecture::getCathedraName);
        int maxColTeacherFirstName = getMaxCol(list, Lecture::getTeacherFirstName);
        int maxColTeacherLastName = getMaxCol(list, Lecture::getTeacherLastName);
        int maxColSubject = getMaxCol(list, Lecture::getSubjectName);
        int maxColLectureTimeStart = getMaxCol(list, Lecture::getTimeStart);
        int maxColLectureTimeEnd = getMaxCol(list, Lecture::getTimeEnd);
        int maxColAudience = getMaxCol(list, Lecture::getAudienceRoom);

        return list.stream().map(group -> format(
            "ID: %-3s" +
            "CATHEDRA: %-"+ maxColCathedra + "s" + COL +
            "TEACHER: %-"+ maxColTeacherFirstName + "s " + "%-"+ maxColTeacherLastName + "s" + COL +
            "SUBJECT: %-"+ maxColSubject + "s" + COL +
            "LECTION_TIME: %-"+ maxColLectureTimeStart + "s -/- " + "%-"+ maxColLectureTimeEnd + "s" + COL +
            "AUDIENCE: %-"+ maxColAudience + "s",
            group.getId(),
            group.getCathedra().getName(),
            group.getTeacher().getFirstName(),
            group.getTeacher().getLastName(),
            group.getSubject().getName(),
            group.getTime().getStart(),
            group.getTime().getEnd(),
            group.getAudience().getRoom()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListStudent(List<Student> list) {
        int maxColFirstName = getMaxCol(list, Student::getFirstName);
        int maxColLastName = getMaxCol(list, Student::getLastName);
        int maxColGender = getMaxCol(list, Student::getGender);
        int maxColFAddress = getMaxCol(list, Student::getAddres);
        int maxColEmail = getMaxCol(list, Student::getEmail);
        int maxColPhone = getMaxCol(list, Student::getPhone);
        int maxColGroup = getMaxCol(list, (Student::getGroupName));

        return list.stream().map(student -> format(
            "ID: %-3s" +
            "GROUP: %-" + maxColGroup + "s" + COL +
            "FIRSTNAME: %-" + maxColFirstName + "s" + COL +
            "LASTNAME: %-" + maxColLastName + "s" + COL +
            "GENDER:%-" + maxColGender + "s" + COL +
            "ADDRESS: %-" + maxColFAddress + "s" + COL +
            "EMAIL: %-" + maxColEmail + "s" + COL +
            "PHONE: %-" + maxColPhone + "s" + COL +
            "BIRTHDAY: %s",
            student.getId(),
            student.getGroup().getName(),
            student.getFirstName(),
            student.getLastName(),
            student.getGender(),
            student.getAddres(),
            student.getEmail(),
            student.getPhone(),
            student.getBirthday()
        )).collect(Collectors.joining(System.lineSeparator()));
    }
    public String formatListTeacher(List<Teacher> list) {
        int maxColFirstName = getMaxCol(list, Teacher::getFirstName);
        int maxColLastName = getMaxCol(list, Teacher::getLastName);
        int maxColGender = getMaxCol(list, Teacher::getGender);
        int maxColFAddress = getMaxCol(list, Teacher::getAddres);
        int maxColEmail = getMaxCol(list, Teacher::getEmail);
        int maxColPhone = getMaxCol(list, Teacher::getPhone);
        int maxColCathedra = getMaxCol(list, (Teacher::getCathedraName));

        return list.stream().map(teacher -> format(
            "ID: %-3s" +
            "CATHEDRA: %-" + maxColCathedra + "s" + COL +
            "FIRSTNAME: %-" + maxColFirstName + "s" + COL +
            "LASTNAME: %-" + maxColLastName + "s" + COL +
            "GENDER:%-" + maxColGender + "s" + COL +
            "ADDRESS: %-" + maxColFAddress + "s" + COL +
            "EMAIL: %-" + maxColEmail + "s" + COL +
            "PHONE: %-" + maxColPhone + "s" + COL +
            "BIRTHDAY: %s",
            teacher.getId(),
            teacher.getCathedra().getName(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getGender(),
            teacher.getAddres(),
            teacher.getEmail(),
            teacher.getPhone(),
            teacher.getBirthday()
        )).collect(Collectors.joining(System.lineSeparator()));
    }
    public String formatListVacation(List<Vacation> list) {
        int maxStartJob = getMaxCol(list, Vacation::getStartJobString);
        int maxEndJob = getMaxCol(list, Vacation::getEndJobString);
        int maxFirstName = getMaxCol(list, Vacation::getTeacherFirstName);
        int maxLastName = getMaxCol(list, Vacation::getTeacherLastName);

        return list.stream().map(vacation -> format(
            "ID: %-3s" +
            "START JOB: %-" + maxStartJob + "s" + COL +
            "END JOB: %-" + maxEndJob + "s" + COL +
            "TEACHER: %-" + maxFirstName + "s %-" + maxLastName +"s",
            vacation.getId(),
            vacation.getStartJobLocal(),
            vacation.getEndJobLocal(),
            vacation.getTeacher().getFirstName(),
            vacation.getTeacher().getLastName()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListSubject(List<Subject> list) {
        int maxName = getMaxCol(list, Subject::getName);
        int maxDescription = getMaxCol(list, Subject::getDescription);

        return list.stream().map(subject -> format(
            "ID: %-3s" +
            "NAME: %-" + maxName + "s" + COL +
            "DESCRIPTION: %-" + maxDescription + "s",
            subject.getId(),
            subject.getName(),
            subject.getDescription()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListLectionTime(List<LectureTime> list) {
        int maxSrartLection = getMaxCol(list, LectureTime::getStart);
        int maxEndlection = getMaxCol(list, LectureTime::getEnd);
        return list.stream().map(lectureTime -> format(
            "ID: %-3s" +
            "START LECTION: %-" + maxSrartLection + "s" + COL +
            "END LECTION: %-" + maxEndlection + "s",
            lectureTime.getId(),
            lectureTime.getStart(),
            lectureTime.getEnd()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListHoliday(List<Holiday> list) {
        int maxDesc = getMaxCol(list, Holiday::getDesc);
        int maxStart = getMaxCol(list, Holiday::getStartHoliday);
        int maxEnd = getMaxCol(list, Holiday::getEndHoliday);


        return list.stream().map(holiday -> format(
            "ID: %-3s" +
            "DESCRIPTION: %-" + maxDesc + "s" + COL +
            "START HOLIDAY: %-" + maxStart + "s" + COL +
            "END HOLIDAY: %-" + maxEnd + "s",
            holiday.getId(),
            holiday.getDesc(),
            holiday.getStartHoliday(),
            holiday.getEndHoliday()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatAudience(List<Audience> list) {
        int maxRoom= getMaxCol(list, Audience::getRoomString);
        int maxSize = getMaxCol(list, Audience::getSizeString);
        return list.stream().map(lectureTime -> format(
            "ID: %-3s" +
            "ROOM: %-" + maxRoom + "s" + COL +
            "SIZE: %-" + maxSize + "s",
            lectureTime.getId(),
            lectureTime.getRoom(),
            lectureTime.getSize()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatLectionTime(LectureTime lectureTime) {
        return String.format(
            "ID: %-3s" +
            "START LECTION: %s" + COL +
            "END LECTION: %s",
            lectureTime.getId(),
            lectureTime.getStart(),
            lectureTime.getEnd()
        );
    }

    public String formatTeacher(Teacher teacher) {
        return String.format(
            "ID: %-3s" +
            "GROUP: %s" + COL +
            "FIRSTNAME: %s" + COL +
            "LASTNAME: %s" + COL +
            "GENDER:%s" + COL +
            "ADDRESS: %s" + COL +
            "EMAIL: %s" + COL +
            "PHONE: %s" + COL +
            "BIRTHDAY: %s%n",
            teacher.getId(),
            teacher.getCathedra().getName(),
            teacher.getFirstName(),
            teacher.getLastName(),
            teacher.getGender(),
            teacher.getAddres(),
            teacher.getEmail(),
            teacher.getPhone(),
            teacher.getBirthday()
        );
    }

    public String formatStudent(Student student) {
        return String.format(
            "ID: %-3s" +
            "GROUP: %s" + COL +
            "FIRSTNAME: %s" + COL +
            "LASTNAME: %s" + COL +
            "GENDER:%s" + COL +
            "ADDRESS: %s" + COL +
            "EMAIL: %s" + COL +
            "PHONE: %s" + COL +
            "BIRTHDAY: %s%n",
            student.getId(),
            student.getGroup().getName(),
            student.getFirstName(),
            student.getLastName(),
            student.getGender(),
            student.getAddres(),
            student.getEmail(),
            student.getPhone(),
            student.getBirthday()
        );
    }


    private <T> Integer getMaxCol(List<T> list, Function<T, String> col) {
        return list.stream().map(col).mapToInt(x -> valueOf(x).length()).max().orElse(0);
    }
}
