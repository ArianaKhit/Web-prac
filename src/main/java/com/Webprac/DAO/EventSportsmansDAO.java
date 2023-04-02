package com.Webprac.DAO;

import com.Webprac.tables.EventSportsmans;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventSportsmansDAO extends CommonDAO<EventSportsmans, Long> {

    public EventSportsmansDAO(){
        super(EventSportsmans.class);
    }
}