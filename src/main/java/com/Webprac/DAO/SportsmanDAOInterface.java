package com.Webprac.DAO;

import com.Webprac.tables.Sportsman;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public interface SportsmanDAOInterface extends CommonDAOInterface<Sportsman, Long> {

    List<Sportsman> getAllPersonByName(String personName);
    Sportsman getSinglePersonByName(String personName);
    String getYearsOfLife(Sportsman person);
    List<Sportsman> getByFilter(Filter filter);

    @Builder
    @Getter
    class Filter {
        private String name;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }
}