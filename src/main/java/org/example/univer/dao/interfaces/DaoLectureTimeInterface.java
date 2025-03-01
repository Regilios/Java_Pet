package org.example.univer.dao.interfaces;

import org.example.univer.models.LectureTime;

public interface DaoLectureTimeInterface extends DaoInterfaces<LectureTime> {
    boolean isSingle(LectureTime lectureTime);
}
