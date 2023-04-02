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
public class EventSportsmans implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventID")
    @NonNull
    private SportEvent event;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sportsmanID")
    @NonNull
    private Sportsman sportsman;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSportsmans other = (EventSportsmans) o;
        return Objects.equals(event, other.event)
                && Objects.equals(sportsman, other.sportsman);
    }
}
