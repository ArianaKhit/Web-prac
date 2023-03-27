package com.Webprac.DAO;

import com.Webprac.tables.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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