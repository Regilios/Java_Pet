package org.example.univer.dao.interfaces;

import org.example.univer.models.Audience;

public interface DaoAudienceInterface extends DaoInterfaces<Audience>{
    boolean findRoom(Audience audience);
}
