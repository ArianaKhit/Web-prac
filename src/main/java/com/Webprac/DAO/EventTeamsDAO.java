package com.Webprac.DAO;

import com.Webprac.tables.EventSportsmans;
import com.Webprac.tables.EventTeams;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventTeamsDAO extends CommonDAO<EventTeams, Long> {

    public EventTeamsDAO(){
        super(EventTeams.class);
    }
}