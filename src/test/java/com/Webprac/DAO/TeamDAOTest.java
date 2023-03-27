package com.Webprac.DAO;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.Webprac.tables.Team;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class TeamDAOTest {

    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private SessionFactory sessionFactory;

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

    @BeforeEach
    void beforeEach() {
        List<Team> teamList = new ArrayList<>();
        teamList.add(new Team(1L, "Zenit", "Football", "St. Petersburg"));
        teamList.add(new Team(null, "Rubin", "Football", "Kazan"));
        teamList.add(new Team(null, "Torpeda", "Hockey", "France"));
        teamList.add(new Team(null, "Spartak", "Hockey", "Nowhere"));
        teamDAO.saveCollection(teamList);
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
            session.getTransaction().commit();
        }
    }
}
