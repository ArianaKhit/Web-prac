package com.Webprac.tables;

import lombok.*;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Team")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Team {
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
