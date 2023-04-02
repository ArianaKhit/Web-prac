package com.Webprac.tables;

import com.Webprac.jsons.JSONConverter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TeamSportsmans")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class TeamSportsmans implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamID")
    @NonNull
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sportsmanID")
    @NonNull
    private Sportsman sportsman;

    @Convert(converter = JSONConverter.class)
    @Column(name = "dates")
    private JsonNode dates;

    @Column(name = "current")
    @NonNull
    private Boolean current;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamSportsmans other = (TeamSportsmans) o;
        return Objects.equals(id, other.id)
                && Objects.equals(team, other.team)
                && Objects.equals(sportsman, other.sportsman)
                && Objects.equals(dates, other.dates)
                && Objects.equals(current, other.current);
    }
}
