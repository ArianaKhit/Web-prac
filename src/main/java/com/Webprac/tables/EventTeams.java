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
    private SportEvent eventID;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamID")
    @ToString.Exclude
    @NonNull
    private Team teamID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTeams other = (EventTeams) o;
        return Objects.equals(eventID, other.eventID)
                && Objects.equals(teamID, other.teamID);
    }
}
