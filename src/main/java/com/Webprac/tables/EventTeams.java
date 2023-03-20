package com.Webprac.tables;

import lombok.*;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "EventTeams")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class EventTeams {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventID")
    @ToString.Exclude
    @NonNull
    private SportEvent event_id;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamID")
    @ToString.Exclude
    @NonNull
    private Team team_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTeams other = (EventTeams) o;
        return Objects.equals(event_id, other.event_id)
                && Objects.equals(team_id, other.team_id);
    }
}
