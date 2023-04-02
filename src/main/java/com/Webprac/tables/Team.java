package com.Webprac.tables;

import lombok.*;
import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Team")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Team implements CommonEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "teamID")
    private Long id;

    @Column(nullable = false, name = "teamName")
    @NonNull
    private String teamName;

    @Column(nullable = false, name = "sport")
    @NonNull
    private String sport;

    @Column(name = "country")
    @NonNull
    private String country;


    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<TeamSportsmans> teamSportsmans = new HashSet<TeamSportsmans>();

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<TeamCoaches> teamCoaches = new HashSet<TeamCoaches>();

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<EventTeams> eventTeams = new HashSet<EventTeams>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team other = (Team) o;
        return Objects.equals(id, other.id)
                && teamName.equals(other.teamName)
                && sport.equals(other.sport)
                && country.equals(other.country);
    }
}
