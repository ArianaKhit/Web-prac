package com.Webprac.DAO;

import com.Webprac.jsons.JSONConverter;
import com.Webprac.tables.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class CoachDAOTest {

    @Autowired
    private CoachDAO coachDAO;
    @Autowired
    private SportsmanDAO sportsmanDAO;
    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private SportsmanCoachesDAO sportsmanCoachesDAO;
    @Autowired
    private TeamCoachesDAO teamCoachesDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
    private JSONConverter jsonConverter = new JSONConverter();

    @Test
    void testSimpleManipulations() {
        List<Coach> personListAll = (List<Coach>) coachDAO.getAll();
        assertEquals(4, personListAll.size());

        List<Coach> noname3 = coachDAO.getAllByName("Noname 3");
        assertEquals(1, noname3.size());
        assertEquals("Noname 3", noname3.get(0).getName());

        Coach noname2 = coachDAO.getById(3L);
        assertEquals(3, noname2.getId());

        Coach personNotExist = coachDAO.getById(666L);
        assertNull(personNotExist);

        CoachDAOInterface.Filter f = new CoachDAOInterface.Filter(null, "Hockey", "Nowhere");
        List<Coach> l = coachDAO.getByFilter(f);
        assertEquals(1, l.size());

        CoachDAOInterface.Filter f2 = new CoachDAOInterface.Filter("Noname 2", "Football", "France");
        List<Coach> l2 = coachDAO.getByFilter(f2);
        assertEquals(1, l2.size());
    }

    @Test
    void testUpdate() {
        LocalDate birth = LocalDate.parse("12-11-2018", dateFormatter);

        Coach updatePerson = coachDAO.getByName("Noname 2");
        updatePerson.setBirthDate(birth);
        coachDAO.update(updatePerson);

        Coach noname2 = coachDAO.getByName("Noname 2");
        assertEquals(birth, noname2.getBirthDate());
    }

    @Test
    void testDelete() {
        Coach deletePerson = coachDAO.getByName("Noname 1");
        coachDAO.delete(deletePerson);

        Coach noname1 = coachDAO.getByName("Noname 1");
        assertNull(noname1);
    }

    @Test
    void testAddCoachToTeam() {
        String datesStr = "{\"dates\" : [{\"start\":\"2008-03-23\", \"end\":\"2009-02-24\"}]}";
        JsonNode dates = jsonConverter.convertToEntityAttribute(datesStr);
        System.out.println(jsonConverter.convertToDatabaseColumn(dates));

        Coach coach = coachDAO.getById(4L);
        Team team = teamDAO.getById(1L);
        TeamCoaches tc = new TeamCoaches(coach, team);

        coach.getTeamCoaches().add(tc);
        team.getTeamCoaches().add(tc);
        teamDAO.update(team);
        coachDAO.update(coach);

        assertEquals(4L, team.getTeamCoaches().stream().toList().get(0).getCoach().getId());
        assertEquals(1L, coach.getTeamCoaches().stream().toList().get(0).getTeam().getId());

        coachDAO.delete(coach);
        assertEquals(0, teamCoachesDAO.getAll().size());
    }

    @Test
    void testAddCoachToSportsman() {
        String datesStr = "{\"dates\" : [{\"start\":\"2008-03-23\", \"end\":\"2009-02-24\"}]}";
        JsonNode dates = jsonConverter.convertToEntityAttribute(datesStr);
        System.out.println(jsonConverter.convertToDatabaseColumn(dates));

        Coach coach = coachDAO.getById(4L);
        Sportsman sp = sportsmanDAO.getById(1L);
        SportsmanCoaches cs = new SportsmanCoaches(coach, sp);

        coach.getSportsmanCoaches().add(cs);
        sp.getSportsmanCoaches().add(cs);
        sportsmanDAO.update(sp);
        coachDAO.update(coach);

        assertEquals(4L, sp.getSportsmanCoaches().stream().toList().get(0).getCoach().getId());
        assertEquals(1L, coach.getSportsmanCoaches().stream().toList().get(0).getSportsman().getId());

        coachDAO.delete(coach);
        assertEquals(0, sportsmanCoachesDAO.getAll().size());
    }

    @BeforeEach
    void beforeEach() {
        List<Coach> personList = new ArrayList<>();
        LocalDate birth = LocalDate.parse("12-11-2018", dateFormatter);

        personList.add(new Coach("Кто-то Там", "Hockey", birth, "Countryyyy"));
        personList.add(new Coach("Noname 1", "Football", birth, "No country"));
        personList.add(new Coach("Noname 2", "Football", birth, "France"));
        personList.add(new Coach("Noname 3", "Hockey", birth, "Nowhere"));
        coachDAO.saveCollection(personList);

        List<Team> teamList = new ArrayList<>();
        teamList.add(new Team("Zenit", "Football", "St. Petersburg"));
        teamList.add(new Team("Rubin", "Football", "Kazan"));
        teamList.add(new Team("Torpeda", "Hockey", "France"));
        teamList.add(new Team("Spartak", "Hockey", "Nowhere"));
        teamDAO.saveCollection(teamList);

        ArrayList<Sportsman> sportsmen = new ArrayList<>();
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
            NativeQuery query1 = session.createNativeQuery("TRUNCATE coach RESTART IDENTITY CASCADE;");
            query1.executeUpdate();
            NativeQuery query2 = session.createNativeQuery("ALTER SEQUENCE coach_coachid_seq RESTART WITH 1;");
            query2.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
