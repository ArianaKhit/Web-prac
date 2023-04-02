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
public class TeamCoaches {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coachID")
    @ToString.Exclude
    @NonNull
    private Coach coachID;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamID")
    @ToString.Exclude
    @NonNull
    private Team teamID;

    @Convert(converter = JSONConverter.class)
    @Column(name = "dates")
    private JsonNode dates;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamCoaches other = (TeamCoaches) o;
        return Objects.equals(coachID, other.coachID)
                && Objects.equals(teamID, other.teamID)
                && Objects.equals(dates, other.dates);
    }
}
