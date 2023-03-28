package com.Webprac.tables;

import com.Webprac.jsons.JSONConverter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "SportEvent")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class SportEvent implements CommonEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "eventID")
    private Long id;

    @Column(nullable = false, name = "title")
    @NonNull
    private String title;

    @Column(nullable = false, name = "sport")
    @NonNull
    private String sport;

    @Column(name = "tournament")
    private String tournament;

    @Column(nullable = false, name = "description")
    @NonNull
    private String description;

    @Column(nullable = false, name = "venue")
    @NonNull
    private String venue;

    @Column(name = "date")
    private LocalDate date;

    @Convert(converter = JSONConverter.class)
    @Column(name = "seats")
    private JsonNode seats;


    @Convert(converter = JSONConverter.class)
    @Column(name = "results")
    private JsonNode results;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportEvent other = (SportEvent) o;
        return Objects.equals(id, other.id)
                && sport.equals(other.sport)
                && title.equals(other.title)
                && tournament.equals(other.tournament)
                && description.equals(other.description)
                && venue.equals(other.venue)
                && Objects.equals(date, other.date)
                && seats.equals(other.seats)
                && results.equals(other.results);
    }
}
