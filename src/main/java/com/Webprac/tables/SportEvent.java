package com.Webprac.tables;

import lombok.*;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SportEvent")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class SportEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "eventID")
    private Long id;

    @Column(nullable = false, name = "sport")
    @NonNull
    private String sport;

    @Column(nullable = false, name = "title")
    @NonNull
    private String title;

    @Column(name = "tournament")
    private String tournament;

    @Column(nullable = false, name = "description")
    @NonNull
    private String description;

    @Column(nullable = false, name = "venue")
    @NonNull
    private String venue;

    @Column(name = "date")
    private Long date;

    @Column(name = "seats")
    private String seats;

    @Column(name = "results")
    private String results;

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
