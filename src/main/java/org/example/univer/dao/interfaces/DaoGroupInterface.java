package org.example.univer.dao.interfaces;

import org.example.univer.models.Group;

import java.util.List;

public interface DaoGroupInterface extends DaoInterfaces<Group> {
    /*
    void addlection(Long groupId, Long lectionId);
    void removeLection(Long groupId, Long lectionId);
*/
    List<Group> getGroupById(List<Long> groupIds);

    boolean isSingle(Group group);
}
