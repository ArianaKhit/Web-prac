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
public class EventTeams implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventID")
    @NonNull
    private SportEvent event;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamID")
    @NonNull
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventTeams other = (EventTeams) o;
        return Objects.equals(event, other.event)
                && Objects.equals(team, other.team);
    }
}
