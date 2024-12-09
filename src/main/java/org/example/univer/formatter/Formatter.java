package org.example.univer.formatter;

import org.example.univer.models.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.*;

@Component
public class Formatter {
    private final String COLUMN = "  |  ";

    public String formatGroupList(List<Group> list) {
        Integer maxColName = getMaxCol(list, Group::getName);
        return list.stream().map(group -> format(
                "ID: %-3s" +
                "GROUP_NAME: %-" + maxColName + "s" + COLUMN +
                "CATHEDRA: %s",
                group.getId(),
                group.getName(),
                group.getCathedra().getName()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListLecture(List<Lecture> list) {
        Integer maxColCathedra = getMaxCol(list, Lecture::getCathedraName);
        Integer maxColTeacherFirstName = getMaxCol(list, Lecture::getTeacherFirstName);
        Integer maxColTeacherLastName = getMaxCol(list, Lecture::getTeacherLastName);
        Integer maxColSubject = getMaxCol(list, Lecture::getSubjectName);
        Integer maxColLectureTimeStart = getMaxCol(list, Lecture::getTimeStart);
        Integer maxColLectureTimeEnd = getMaxCol(list, Lecture::getTimeEnd);
        Integer maxColAudience = getMaxCol(list, Lecture::getAudienceRoom);

        return list.stream().map(group -> format(
                "ID: %-3s" +
                "CATHEDRA: %-" + maxColCathedra + "s" + COLUMN +
                "TEACHER: %-" + maxColTeacherFirstName + "s " + "%-" + maxColTeacherLastName + "s" + COLUMN +
                "SUBJECT: %-" + maxColSubject + "s" + COLUMN +
                "LECTION_TIME: %-" + maxColLectureTimeStart + "s -/- " + "%-" + maxColLectureTimeEnd + "s" + COLUMN +
                "AUDIENCE: %-" + maxColAudience + "s",
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
        Integer maxColFirstName = getMaxCol(list, Student::getFirstName);
        Integer maxColLastName = getMaxCol(list, Student::getLastName);
        Integer maxColGender = getMaxCol(list, Student::getGender);
        Integer maxColFAddress = getMaxCol(list, Student::getAddres);
        Integer maxColEmail = getMaxCol(list, Student::getEmail);
        Integer maxColPhone = getMaxCol(list, Student::getPhone);
        Integer maxColGroup = getMaxCol(list, (Student::getGroupName));

        return list.stream().map(student -> format(
                "ID: %-3s" +
                "GROUP: %-" + maxColGroup + "s" + COLUMN +
                "FIRSTNAME: %-" + maxColFirstName + "s" + COLUMN +
                "LASTNAME: %-" + maxColLastName + "s" + COLUMN +
                "GENDER:%-" + maxColGender + "s" + COLUMN +
                "ADDRESS: %-" + maxColFAddress + "s" + COLUMN +
                "EMAIL: %-" + maxColEmail + "s" + COLUMN +
                "PHONE: %-" + maxColPhone + "s" + COLUMN +
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
        Integer maxColFirstName = getMaxCol(list, Teacher::getFirstName);
        Integer maxColLastName = getMaxCol(list, Teacher::getLastName);
        Integer maxColGender = getMaxCol(list, Teacher::getGender);
        Integer maxColFAddress = getMaxCol(list, Teacher::getAddres);
        Integer maxColEmail = getMaxCol(list, Teacher::getEmail);
        Integer maxColPhone = getMaxCol(list, Teacher::getPhone);
        Integer maxColCathedra = getMaxCol(list, (Teacher::getCathedraName));

        return list.stream().map(teacher -> format(
                "ID: %-3s" +
                "CATHEDRA: %-" + maxColCathedra + "s" + COLUMN +
                "FIRSTNAME: %-" + maxColFirstName + "s" + COLUMN +
                "LASTNAME: %-" + maxColLastName + "s" + COLUMN +
                "GENDER:%-" + maxColGender + "s" + COLUMN +
                "ADDRESS: %-" + maxColFAddress + "s" + COLUMN +
                "EMAIL: %-" + maxColEmail + "s" + COLUMN +
                "PHONE: %-" + maxColPhone + "s" + COLUMN +
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
        Integer maxStartJob = getMaxCol(list, Vacation::getStartJobString);
        Integer maxEndJob = getMaxCol(list, Vacation::getEndJobString);
        Integer maxFirstName = getMaxCol(list, Vacation::getTeacherFirstName);
        Integer maxLastName = getMaxCol(list, Vacation::getTeacherLastName);

        return list.stream().map(vacation -> format(
                "ID: %-3s" +
                "START JOB: %-" + maxStartJob + "s" + COLUMN +
                "END JOB: %-" + maxEndJob + "s" + COLUMN +
                "TEACHER: %-" + maxFirstName + "s %-" + maxLastName + "s",
                vacation.getId(),
                vacation.getStartJobLocal(),
                vacation.getEndJobLocal(),
                vacation.getTeacher().getFirstName(),
                vacation.getTeacher().getLastName()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListSubject(List<Subject> list) {
        Integer maxName = getMaxCol(list, Subject::getName);
        Integer maxDescription = getMaxCol(list, Subject::getDescription);

        return list.stream().map(subject -> format(
                "ID: %-3s" +
                "NAME: %-" + maxName + "s" + COLUMN +
                "DESCRIPTION: %-" + maxDescription + "s",
                subject.getId(),
                subject.getName(),
                subject.getDescription()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListLectionTime(List<LectureTime> list) {
        Integer maxSrartLection = getMaxCol(list, LectureTime::getStart);
        Integer maxEndlection = getMaxCol(list, LectureTime::getEnd);

        return list.stream().map(lectureTime -> format(
                "ID: %-3s" +
                "START LECTION: %-" + maxSrartLection + "s" + COLUMN +
                "END LECTION: %-" + maxEndlection + "s",
                lectureTime.getId(),
                lectureTime.getStart(),
                lectureTime.getEnd()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListHoliday(List<Holiday> list) {
        Integer maxDesc = getMaxCol(list, Holiday::getDesc);
        Integer maxStart = getMaxCol(list, Holiday::getStartHoliday);
        Integer maxEnd = getMaxCol(list, Holiday::getEndHoliday);

        return list.stream().map(holiday -> format(
                "ID: %-3s" +
                "DESCRIPTION: %-" + maxDesc + "s" + COLUMN +
                "START HOLIDAY: %-" + maxStart + "s" + COLUMN +
                "END HOLIDAY: %-" + maxEnd + "s",
                holiday.getId(),
                holiday.getDesc(),
                holiday.getStartHoliday(),
                holiday.getEndHoliday()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatListAudience(List<Audience> list) {
        Integer maxRoom = getMaxCol(list, Audience::getRoomString);
        Integer maxSize = getMaxCol(list, Audience::getCapacityString);

        return list.stream().map(lectureTime -> format(
                "ID: %-3s" +
                "ROOM: %-" + maxRoom + "s" + COLUMN +
                "SIZE: %-" + maxSize + "s",
                lectureTime.getId(),
                lectureTime.getRoom(),
                lectureTime.getCapacity()
        )).collect(Collectors.joining(System.lineSeparator()));
    }

    public String formatLectionTime(LectureTime lectureTime) {
        return String.format(
                "ID: %-3s" +
                "START LECTION: %s" + COLUMN +
                "END LECTION: %s",
                lectureTime.getId(),
                lectureTime.getStart(),
                lectureTime.getEnd()
        );
    }
    public String formatSubject(Subject subject) {
        return String.format(
                "ID: %-3s" +
                "NAME: %s" + COLUMN +
                "DESCRIPTION: %s",
                subject.getId(),
                subject.getName(),
                subject.getDescription()
        );
    }


    public String formatTeacher(Teacher teacher) {
        return String.format(
                "ID: %-3s" +
                "GROUP: %s" + COLUMN +
                "FIRSTNAME: %s" + COLUMN +
                "LASTNAME: %s" + COLUMN +
                "GENDER:%s" + COLUMN +
                "ADDRESS: %s" + COLUMN +
                "EMAIL: %s" + COLUMN +
                "PHONE: %s" + COLUMN +
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
                "GROUP: %s" + COLUMN +
                "FIRSTNAME: %s" + COLUMN +
                "LASTNAME: %s" + COLUMN +
                "GENDER:%s" + COLUMN +
                "ADDRESS: %s" + COLUMN +
                "EMAIL: %s" + COLUMN +
                "PHONE: %s" + COLUMN +
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
