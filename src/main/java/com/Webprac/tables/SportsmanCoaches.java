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
public class SportsmanCoaches implements CommonEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coachID")
    @NonNull
    private Coach coach;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sportsmanID")
    @NonNull
    private Sportsman sportsman;

    @Convert(converter = JSONConverter.class)
    @Column(name = "dates")
    private JsonNode dates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportsmanCoaches other = (SportsmanCoaches) o;
        return Objects.equals(coach, other.coach)
                && Objects.equals(sportsman, other.sportsman)
                && Objects.equals(dates, other.dates);
    }
}
