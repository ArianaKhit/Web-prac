package com.Webprac.tables;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.type.descriptor.jdbc.TimestampWithTimeZoneJdbcType;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Sportsman")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Sportsman implements CommonEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "sportsmanID")
    private Long id;

    @Column(nullable = false, name = "name")
    @NonNull
    private String name;

    @Column(nullable = false, name = "sport")
    @NonNull
    private String sport;

    @Column(name = "birthDate")
    @NonNull
    private LocalDate birthDate;

    @Column(name = "country")
    @NonNull
    private String country;

    @OneToMany(mappedBy = "sportsman", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<TeamSportsmans> teamSportsmans = new HashSet<TeamSportsmans>();

    @OneToMany(mappedBy = "sportsman", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<SportsmanCoaches> sportsmanCoaches = new HashSet<SportsmanCoaches>();

    @OneToMany(mappedBy = "sportsman", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<EventSportsmans> eventSportsmans = new HashSet<EventSportsmans>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sportsman other = (Sportsman) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && sport.equals(other.sport)
                && Objects.equals(birthDate, other.birthDate)
                && country.equals(other.country);
    }
}
