package com.Webprac.DAO;

import com.Webprac.tables.Sportsman;
import com.Webprac.tables.Team;
import com.Webprac.tables.TeamSportsmans;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public interface TeamDAOInterface extends CommonDAOInterface<Team, Long> {

    List<Team> getAllByName(String teamName);
    Team getByName(String teamName);
    List<Team> getByFilter(Filter filter);

    @Builder
    @Getter
    @AllArgsConstructor
    class Filter {
        private String teamName;
        private String sport;
        private String country;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }
}