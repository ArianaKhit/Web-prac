package com.Webprac.tables;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.type.descriptor.jdbc.TimestampWithTimeZoneJdbcType;

import java.sql.Timestamp;
import java.util.Objects;

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
    private Timestamp birthDate;

    @Column(name = "country")
    @NonNull
    private String country;

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
