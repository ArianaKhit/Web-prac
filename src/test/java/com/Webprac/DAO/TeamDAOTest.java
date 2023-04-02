package com.Webprac.DAO;

import com.Webprac.jsons.JSONConverter;
import com.Webprac.tables.Sportsman;
import com.Webprac.tables.TeamSportsmans;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.mapping.Array;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.Webprac.tables.Team;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class TeamDAOTest {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private SportsmanDAO sportsmanDAO;
    @Autowired
    private TeamSportsmansDAO teamSportsmansDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
    private JSONConverter jsonConverter = new JSONConverter();

    @Test
    void testSimpleManipulations() {
        List<Team> teamListAll = (List<Team>) teamDAO.getAll();
        assertEquals(4, teamListAll.size());

        List<Team> rubin = teamDAO.getAllByName("Rubin");
        assertEquals(1, rubin.size());
        assertEquals("Rubin", rubin.get(0).getTeamName());

        Team noname2 = teamDAO.getById(3L);
        assertEquals(3, noname2.getId());

        Team personNotExist = teamDAO.getById(666L);
        assertNull(personNotExist);

        TeamDAOInterface.Filter f = new TeamDAOInterface.Filter(null, "Hockey", "France");
        List<Team> l = teamDAO.getByFilter(f);
        assertEquals(1, l.size());
        assertEquals("Torpeda", l.get(0).getTeamName());

        TeamDAOInterface.Filter f2 = new TeamDAOInterface.Filter("No Team", "Football", "France");
        List<Team> l2 = teamDAO.getByFilter(f2);
        assertEquals(0, l2.size());
    }

    @Test
    void testUpdate() {

        Team updateTeam = teamDAO.getByName("Spartak");
        updateTeam.setTeamName("Spartack");
        teamDAO.update(updateTeam);
    }

    @Test
    void testDelete() {
        Team deleteTeam = teamDAO.getByName("Spartak");
        teamDAO.delete(deleteTeam);

        Team noname1 = teamDAO.getByName("No team");
        assertNull(noname1);
    }


    @Test
    void testAddToTeam() {
        String datesStr = "{\"dates\" : [{\"start\":\"2008-03-23\", \"end\":\"2009-02-24\"}]}";
        JsonNode dates = jsonConverter.convertToEntityAttribute(datesStr);
        System.out.println(jsonConverter.convertToDatabaseColumn(dates));

        Team team = teamDAO.getById(4L);
        Sportsman sp = sportsmanDAO.getById(1L);
        TeamSportsmans ts = new TeamSportsmans(team, sp, Boolean.TRUE);

        team.getTeamSportsmans().add(ts);
        sp.getTeamSportsmans().add(ts);
        sportsmanDAO.update(sp);
        teamDAO.update(team);

        assertEquals(4L, sp.getTeamSportsmans().stream().toList().get(0).getTeam().getId());
        assertEquals(1L, team.getTeamSportsmans().stream().toList().get(0).getSportsman().getId());

        teamDAO.delete(team);
        assertEquals(0, teamSportsmansDAO.getAll().size());
    }

    @BeforeEach
    void beforeEach() {
        List<Team> teamList = new ArrayList<>();
        teamList.add(new Team("Zenit", "Football", "St. Petersburg"));
        teamList.add(new Team("Rubin", "Football", "Kazan"));
        teamList.add(new Team("Torpeda", "Hockey", "France"));
        teamList.add(new Team("Spartak", "Hockey", "Nowhere"));
        teamDAO.saveCollection(teamList);

        ArrayList<Sportsman> sportsmen = new ArrayList<>();
        LocalDate birth = LocalDate.parse("12-11-2018", dateFormatter);
        sportsmen.add(new Sportsman("Jack", "Hockey", birth, "Bleburg"));
        sportsmen.add(new Sportsman("John", "Football", birth, "Iucrus"));
        sportsmen.add(new Sportsman("James", "Tennis", birth, "France"));
        sportsmanDAO.saveCollection(sportsmen);
    }

    @BeforeAll
    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            NativeQuery query1 = session.createNativeQuery("TRUNCATE team RESTART IDENTITY CASCADE;");
            query1.executeUpdate();
            NativeQuery query2 = session.createNativeQuery("ALTER SEQUENCE team_teamid_seq RESTART WITH 1;");
            query2.executeUpdate();

            NativeQuery query3 = session.createNativeQuery("TRUNCATE teamsportsmans RESTART IDENTITY CASCADE;");
            query3.executeUpdate();
            NativeQuery query4 = session.createNativeQuery("ALTER SEQUENCE teamsportsmans_id_seq RESTART WITH 1;");
            query4.executeUpdate();

            NativeQuery query5 = session.createNativeQuery("TRUNCATE Sportsman RESTART IDENTITY CASCADE;");
            query5.executeUpdate();
            NativeQuery query6 = session.createNativeQuery("ALTER SEQUENCE sportsman_sportsmanid_seq RESTART WITH 1;");
            query6.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
