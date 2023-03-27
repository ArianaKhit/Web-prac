package com.Webprac.DAO;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.Webprac.tables.Coach;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class CoachDAOTest {

    @Autowired
    private CoachDAO coachDAO;
    @Autowired
    private SessionFactory sessionFactory;

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
        Timestamp birth = Timestamp.valueOf("2018-11-12 01:02:03.1234");

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

    @BeforeEach
    void beforeEach() {
        List<Coach> personList = new ArrayList<>();
        Timestamp birth = Timestamp.valueOf("2018-11-12 01:02:03.1234");
        personList.add(new Coach(1L, "Кто-то Там", "Hockey", birth, "Countryyyy"));
        personList.add(new Coach(null, "Noname 1", "Football", birth, "No country"));
        personList.add(new Coach(null, "Noname 2", "Football", birth, "France"));
        personList.add(new Coach(null, "Noname 3", "Hockey", birth, "Nowhere"));
        coachDAO.saveCollection(personList);
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
