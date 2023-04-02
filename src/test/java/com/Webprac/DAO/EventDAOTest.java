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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class EventDAOTest {

    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private SportsmanDAO sportsmanDAO;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private TeamDAO teamDAO;
    @Autowired
    private EventSportsmansDAO eventSportsmansDAO;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
    private JSONConverter jsonConverter = new JSONConverter();

    @Test
    void testSimpleManipulations() {
        List<SportEvent> eventListAll = (List<SportEvent>) eventDAO.getAll();
        assertEquals(4, eventListAll.size());

        List<SportEvent> noname3 = eventDAO.getAllByTitle("Ice Show");
        assertEquals(1, noname3.size());
        assertEquals("Ice Show", noname3.get(0).getTitle());

        SportEvent noname2 = eventDAO.getById(2L);
        assertEquals(2, noname2.getId());
        assertNotNull(jsonConverter.convertToDatabaseColumn(noname2.getSeats()));

        SportEvent personNotExist = eventDAO.getById(666L);
        assertNull(personNotExist);

        LocalDate date1 = LocalDate.parse("08-04-2023", dateFormatter);
        LocalDate date2 = LocalDate.parse("17-04-2023", dateFormatter);

        EventDAOInterface.Filter f = new EventDAOInterface.Filter(null, null, null,null, date1, date2);
        List<SportEvent> l = eventDAO.getByFilter(f);
        assertEquals(4, l.size());

        EventDAOInterface.Filter f2 = new EventDAOInterface.Filter(null, "Football", "France", null, null, null);
        List<SportEvent> l2 = eventDAO.getByFilter(f2);
        assertEquals(0, l2.size());
    }

    @Test
    void testUpdate() {
        SportEvent stest = eventDAO.getById(1L);
        assertEquals("Hockey Grand Game", stest.getTitle());
        assertNotNull(stest.getSeats());

        LocalDate dateStart = LocalDate.parse("08-04-2023", dateFormatter);
        LocalDate dateEnd = LocalDate.parse("15-04-2023", dateFormatter);

        SportEvent updateEvent = eventDAO.getByTitle("Football Match");
        updateEvent.setVenue("St. Petersburg, Sport St. 13");
        eventDAO.update(updateEvent);

        SportEvent noname2 = eventDAO.getByTitle("Football Match");
        assertEquals("St. Petersburg, Sport St. 13", noname2.getVenue());
    }

    @Test
    void testDelete() {
        SportEvent deleteEvent = eventDAO.getByTitle("Ice Show");
        eventDAO.delete(deleteEvent);

        SportEvent noname1 = eventDAO.getByTitle("No game");
        assertNull(noname1);
    }


    @Test
    void testAddTeamToEvent() {
        String datesStr = "{\"dates\" : [{\"start\":\"2008-03-23\", \"end\":\"2009-02-24\"}]}";
        JsonNode dates = jsonConverter.convertToEntityAttribute(datesStr);
        System.out.println(jsonConverter.convertToDatabaseColumn(dates));

        SportEvent sportEvent = eventDAO.getById(4L);
        Team team = teamDAO.getById(1L);
        EventTeams et = new EventTeams(sportEvent, team);

        sportEvent.getEventTeams().add(et);
        team.getEventTeams().add(et);
        teamDAO.update(team);
        eventDAO.update(sportEvent);

        assertEquals(4L, team.getEventTeams().stream().toList().get(0).getEvent().getId());
        assertEquals(1L, sportEvent.getEventTeams().stream().toList().get(0).getTeam().getId());

        eventDAO.delete(sportEvent);
        assertEquals(0, eventSportsmansDAO.getAll().size());
    }

    @Test
    void testAddSportsmanToEvent() {
        String datesStr = "{\"dates\" : [{\"start\":\"2008-03-23\", \"end\":\"2009-02-24\"}]}";
        JsonNode dates = jsonConverter.convertToEntityAttribute(datesStr);
        System.out.println(jsonConverter.convertToDatabaseColumn(dates));

        SportEvent sportEvent = eventDAO.getById(4L);
        Sportsman sp = sportsmanDAO.getById(1L);
        EventSportsmans es = new EventSportsmans(sportEvent, sp);

        sportEvent.getEventSportsmans().add(es);
        sp.getEventSportsmans().add(es);
        sportsmanDAO.update(sp);
        eventDAO.update(sportEvent);

        assertEquals(4L, sp.getEventSportsmans().stream().toList().get(0).getEvent().getId());
        assertEquals(1L, sportEvent.getEventSportsmans().stream().toList().get(0).getSportsman().getId());

        eventDAO.delete(sportEvent);
        assertEquals(0, eventSportsmansDAO.getAll().size());
    }

    @BeforeEach
    void beforeEach() {
        List<SportEvent> eventList = new ArrayList<>();
        LocalDate date1 = LocalDate.parse("08-04-2023", dateFormatter);
        LocalDate date2 = LocalDate.parse("12-04-2023", dateFormatter);

        String seat = "{\"seatTypes\": [{\"type\": \"Classic\", \"count\": 15, \"price\": 100.99}, {\"type\": \"VIP\", \"count\": 10, \"price\": 200.99}], \"scheme\":\"Type1\", \"seats\":[{\"type\":\"Classic\", \"sector\":\"A\", \"rowNum\":1, \"seatNum\":1, \"occupied\":false},{\"type\":\"Classic\", \"sector\":\"A\", \"rowNum\":1, \"seatNum\":2, \"occupied\":false}]}";
        JsonNode seats = jsonConverter.convertToEntityAttribute(seat);
        JsonNode result = jsonConverter.convertToEntityAttribute("{}");
        System.out.println(jsonConverter.convertToDatabaseColumn(seats));

        eventList.add(new SportEvent("Hockey Grand Game", "Hockey", "Tour de Ice", "Greatest game of the year", "Moscow, Snow St. 12", date1, seats, result));
        eventList.add(new SportEvent("Football Match", "Football", null, "Greatest game of the year", "Moscow, Snow St. 12", date2, seats, null));
        eventList.add(new SportEvent("Home Team Train game", "Volleyball", null, "Greatest game of the year", "Moscow, Snow St. 12", date1, null, null));
        eventList.add(new SportEvent("Ice Show", "Figure skating", null, "Greatest show of the year", "Moscow, Snow St. 12", date2, seats, null));
        eventDAO.saveCollection(eventList);

        List<Team> teamList = new ArrayList<>();
        teamList.add(new Team("Zenit", "Football", "St. Petersburg"));
        teamList.add(new Team("Rubin", "Football", "Kazan"));
        teamList.add(new Team("Torpeda", "Hockey", "France"));
        teamList.add(new Team("Spartak", "Hockey", "Nowhere"));
        teamDAO.saveCollection(teamList);
    }

    @BeforeAll
    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            NativeQuery query1 = session.createNativeQuery("TRUNCATE sportevent RESTART IDENTITY CASCADE;");
            query1.executeUpdate();
            NativeQuery query2 = session.createNativeQuery("ALTER SEQUENCE sportevent_eventid_seq RESTART WITH 1;");
            query2.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
