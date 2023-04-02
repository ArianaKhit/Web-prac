package com.Webprac.tables;

import com.Webprac.jsons.JSONConverter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TeamCoaches")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class TeamCoaches implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coachID")
    @NonNull
    private Coach coach;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamID")
    @NonNull
    private Team team;

    @Convert(converter = JSONConverter.class)
    @Column(name = "dates")
    private JsonNode dates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCoaches other = (TeamCoaches) o;
        return Objects.equals(coach, other.coach)
                && Objects.equals(team, other.team)
                && Objects.equals(dates, other.dates);
    }
}
