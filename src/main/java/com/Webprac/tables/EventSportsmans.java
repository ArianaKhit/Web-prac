package com.Webprac.tables;

import lombok.*;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "EventSportsmans")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
public class EventSportsmans {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventID")
    @ToString.Exclude
    @NonNull
    private SportEvent eventID;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sportsmanID")
    @ToString.Exclude
    @NonNull
    private Sportsman sportsmanID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSportsmans other = (EventSportsmans) o;
        return Objects.equals(eventID, other.eventID)
                && Objects.equals(sportsmanID, other.sportsmanID);
    }
}
