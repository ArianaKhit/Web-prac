package com.Webprac.DAO;

import com.Webprac.tables.Coach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface CoachDAOInterface extends CommonDAOInterface<Coach, Long> {

    List<Coach> getAllByName(String personName);
    Coach getByName(String personName);
    List<Coach> getByFilter(Filter filter);


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