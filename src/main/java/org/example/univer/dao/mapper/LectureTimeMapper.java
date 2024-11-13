package org.example.univer.dao.mapper;

import org.example.univer.models.LectureTime;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class LectureTimeMapper implements RowMapper<LectureTime> {

    @Override
    public LectureTime mapRow(ResultSet rs, int rowNum) throws SQLException {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(rs.getLong("id"));
        lectureTime.setStart(rs.getObject("start_lection", LocalDateTime.class));
        lectureTime.setEnd(rs.getObject("end_lection", LocalDateTime.class));
        return lectureTime;
    }
}
