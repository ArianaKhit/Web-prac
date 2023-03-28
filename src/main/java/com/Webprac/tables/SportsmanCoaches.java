package com.Webprac.tables;

import com.Webprac.jsons.JSONConverter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SportsmanCoaches")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class SportsmanCoaches {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coachID")
    @ToString.Exclude
    @NonNull
    private Coach coach_id;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sportsmanID")
    @ToString.Exclude
    @NonNull
    private Sportsman sportsman_id;

    @Convert(converter = JSONConverter.class)
    @Column(name = "dates")
    private JsonNode dates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportsmanCoaches other = (SportsmanCoaches) o;
        return Objects.equals(coach_id, other.coach_id)
                && Objects.equals(sportsman_id, other.sportsman_id)
                && Objects.equals(dates, other.dates);
    }
}
