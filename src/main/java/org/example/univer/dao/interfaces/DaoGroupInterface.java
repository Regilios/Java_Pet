package org.example.univer.dao.interfaces;

import org.example.univer.models.Group;

public interface DaoGroupInterface extends DaoInterfaces<Group> {
    void addLection(Long groupId, Long lectionId);
    void removeLection(Long groupId, Long lectionId);
}
