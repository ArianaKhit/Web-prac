package com.Webprac.tables;

import com.Webprac.jsons.JSONConverter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @NonNull
    private LocalDate date;

    @Convert(converter = JSONConverter.class)
    @Column(name = "seats", columnDefinition = "string")
    private JsonNode seats;


    @Convert(converter = JSONConverter.class)
    @Column(name = "results", columnDefinition = "json")
    private JsonNode results;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<EventSportsmans> eventSportsmans = new HashSet<EventSportsmans>();

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<EventTeams> eventTeams = new HashSet<EventTeams>();


    public SportEvent(String title, String sport, String tournament, String description, String venue, LocalDate date, JsonNode seats, JsonNode results){
        this.title = title;
        this.sport = sport;
        this.tournament = tournament;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.seats = seats;
        this.results = results;
    }

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
