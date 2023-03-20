package com.Webprac.tables;

        import lombok.*;
        import jakarta.persistence.*;
        import java.util.Objects;

@Entity
@Table(name = "TeamSportsmans")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class TeamSportsmans {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamID")
    @ToString.Exclude
    @NonNull
    private Team team_id;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sportsmanID")
    @ToString.Exclude
    @NonNull
    private Sportsman sportsman_id;

    @Column(name = "dates")
    private String dates;

    @Column(name = "current")
    private Boolean current;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamSportsmans other = (TeamSportsmans) o;
        return Objects.equals(team_id, other.team_id)
                && Objects.equals(sportsman_id, other.sportsman_id)
                && Objects.equals(dates, other.dates)
                && Objects.equals(current, other.current);
    }
}
