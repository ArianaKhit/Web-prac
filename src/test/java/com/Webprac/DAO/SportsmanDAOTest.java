package com.Webprac.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.Webprac.tables.Sportsman;

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
public class SportsmanDAOTest {

    @Autowired
    private SportsmanDAO sportsmanDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);


    @Test
    void testSimpleManipulations() {
        List<Sportsman> personListAll = (List<Sportsman>) sportsmanDAO.getAll();
        assertEquals(4, personListAll.size());

        List<Sportsman> noname3 = sportsmanDAO.getAllByName("Noname 3");
        assertEquals(1, noname3.size());
        assertEquals("Noname 3", noname3.get(0).getName());

        Sportsman noname2 = sportsmanDAO.getById(3L);
        assertEquals(3, noname2.getId());

        Sportsman personNotExist = sportsmanDAO.getById(666L);
        assertNull(personNotExist);

        SportsmanDAOInterface.Filter f = new SportsmanDAOInterface.Filter(null, "Hockey", "Nowhere");
        List<Sportsman> l = sportsmanDAO.getByFilter(f);
        assertEquals(1, l.size());

        SportsmanDAOInterface.Filter f2 = new SportsmanDAOInterface.Filter("Noname 2", "Football", "France");
        List<Sportsman> l2 = sportsmanDAO.getByFilter(f2);
        assertEquals(1, l2.size());
    }

    @Test
    void testUpdate() {
        LocalDate birth = LocalDate.parse("12-11-2018", dateFormatter);

         Sportsman updatePerson = sportsmanDAO.getByName("Noname 2");
         updatePerson.setBirthDate(birth);
         sportsmanDAO.update(updatePerson);

         Sportsman noname2 = sportsmanDAO.getByName("Noname 2");
         assertEquals(birth, noname2.getBirthDate());
    }

    @Test
    void testDelete() {
         Sportsman deletePerson = sportsmanDAO.getByName("Noname 1");
         sportsmanDAO.delete(deletePerson);

         Sportsman noname1 = sportsmanDAO.getByName("Noname 1");
         assertNull(noname1);
    }

    @BeforeEach
    void beforeEach() {
        List<Sportsman> personList = new ArrayList<>();
        LocalDate birth = LocalDate.parse("12-11-2018", dateFormatter);

        personList.add(new Sportsman("Кто-то Там", "Hockey", birth, "Countryyyy"));
        personList.add(new Sportsman("Noname 1", "Football", birth, "No country"));
        personList.add(new Sportsman("Noname 2", "Football", birth, "France"));
        personList.add(new Sportsman("Noname 3", "Hockey", birth, "Nowhere"));
        sportsmanDAO.saveCollection(personList);
    }

    @BeforeAll
    @AfterEach
    void annihilation() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
                NativeQuery query1 = session.createNativeQuery("TRUNCATE sportsman RESTART IDENTITY CASCADE;");
                query1.executeUpdate();
                NativeQuery query2 = session.createNativeQuery("ALTER SEQUENCE sportsman_sportsmanid_seq RESTART WITH 1;");
                query2.executeUpdate();
                session.getTransaction().commit();
        }
    }
}
