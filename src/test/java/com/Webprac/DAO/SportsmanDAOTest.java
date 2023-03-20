package com.Webprac.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.descriptor.jdbc.TimestampWithTimeZoneJdbcType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.test.context.TestPropertySource;
import com.Webprac.tables.Sportsman;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

//    @Test
    void testSimpleManipulations() {
        // List<Sportsman> personListAll = (List<Sportsman>) sportsmanDAO.getAll();
        // assertEquals(6, personListAll.size());

        // List<Sportsman> geraltQuery = sportsmanDAO.getAllPersonByName("Геральт");
        // assertEquals(1, geraltQuery.size());
        // assertEquals("Геральт из Ривии", geraltQuery.get(0).getName());

        // Sportsman personId3 = sportsmanDAO.getById(3L);
        // assertEquals(3, personId3.getId());

        Sportsman personNotExist = sportsmanDAO.getById(100L);
        assertNull(personNotExist);
    }

    @Test
    void testUpdate() {
//         TimestampWithTimeZoneJdbcType birth = "2000-05-10 15:00:00+03";
        Timestamp birth = Timestamp.valueOf("2018-11-12 01:02:03.1234");

         Sportsman updatePerson = sportsmanDAO.getSinglePersonByName("Noname 2");
         updatePerson.setBirthDate(birth);
         sportsmanDAO.update(updatePerson);

         Sportsman noname2 = sportsmanDAO.getSinglePersonByName("Noname 2");
         assertEquals(birth, noname2.getBirthDate());
    }

    @Test
    void testDelete() {
         Sportsman deletePerson = sportsmanDAO.getSinglePersonByName("Noname 1");
         sportsmanDAO.delete(deletePerson);

         Sportsman noname1 = sportsmanDAO.getSinglePersonByName("Noname 1");
         assertNull(noname1);
    }

    @BeforeEach
    void beforeEach() {
        List<Sportsman> personList = new ArrayList<>();
        Timestamp birth = Timestamp.valueOf("2018-11-12 01:02:03.1234");
        personList.add(new Sportsman(1L, "Кто-то Там", "Hockey", birth, "Countryyyy"));
        personList.add(new Sportsman(null, "Noname 1", "Football", birth, "No country"));
        personList.add(new Sportsman(null, "Noname 2", "Football", birth, "France"));
        personList.add(new Sportsman(null, "Noname 3", "Hockey", birth, "Nowhere"));
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
    //            session.createSQLQuery("TRUNCATE sportsman RESTART IDENTITY CASCADE;").executeUpdate();
    //            session.createSQLQuery("ALTER SEQUENCE sportsman_sportsman_id_seq RESTART WITH 1;").executeUpdate();
                session.getTransaction().commit();
        }
    }
}
