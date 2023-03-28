package com.Webprac.DAO;

import com.Webprac.tables.SportEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.sql.Timestamp;

import java.time.LocalDate;
import java.util.List;

public interface EventDAOInterface extends CommonDAOInterface<SportEvent, Long> {

    List<SportEvent> getAllByTitle(String personName);
    SportEvent getByTitle(String personName);
    List<SportEvent> getByFilter(Filter filter);


    @Builder
    @Getter
    @AllArgsConstructor
    class Filter {
        private String title;
        private String sport;
        private String tournament;
        private String venue;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    static Filter.FilterBuilder getFilterBuilder() {
        return Filter.builder();
    }
}