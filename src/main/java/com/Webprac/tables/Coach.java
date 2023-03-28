package com.Webprac.tables;

import lombok.*;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Coach")
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Coach implements CommonEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "coachID")
    private Long id;

    @Column(nullable = false, name = "name")
    @NonNull
    private String name;

    @Column(nullable = false, name = "sport")
    @NonNull
    private String sport;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "country")
    @NonNull
    private String country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coach other = (Coach) o;
        return Objects.equals(id, other.id)
                && name.equals(other.name)
                && sport.equals(other.sport)
                && Objects.equals(birthDate, other.birthDate)
                && country.equals(other.country);
    }
}
