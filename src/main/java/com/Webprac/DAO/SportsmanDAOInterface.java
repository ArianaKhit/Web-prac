package com.Webprac.DAO;

import com.Webprac.tables.Sportsman;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

public interface SportsmanDAOInterface extends CommonDAOInterface<Sportsman, Long> {

    List<Sportsman> getAllByName(String personName);
    Sportsman getByName(String personName);
    List<Sportsman> getByFilter(Filter filter);


    @Builder
    @Getter
    @AllArgsConstructor
    class Filter {
        private String name;
        private String sport;
        private String country;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }
}