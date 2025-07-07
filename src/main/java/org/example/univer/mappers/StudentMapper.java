
package org.example.univer.mappers;

import org.example.univer.dto.StudentDto;
import org.example.univer.models.Student;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {GroupMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface StudentMapper {
     Student toEntity(StudentDto dto);
     StudentDto toDto(Student student);
}
