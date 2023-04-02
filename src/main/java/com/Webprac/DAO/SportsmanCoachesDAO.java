package com.Webprac.DAO;

import com.Webprac.tables.SportsmanCoaches;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SportsmanCoachesDAO extends CommonDAO<SportsmanCoaches, Long> {

    public SportsmanCoachesDAO(){
        super(SportsmanCoaches.class);
    }


    public List<SportsmanCoaches> getAllBySportsman(Long sportsmanID) {
        try (Session session = sessionFactory.openSession()) {
            Query<SportsmanCoaches> query = session.createQuery("FROM SportsmanCoaches WHERE sportsmanID = :sportsmanID", SportsmanCoaches.class)
                    .setParameter("sportsmanID", sportsmanID);
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    public SportsmanCoaches getBySportsman(Long sportsmanID) {
        List<SportsmanCoaches> candidates = this.getAllBySportsman(sportsmanID);
        return candidates == null ? null :
                candidates.size() == 1 ? candidates.get(0) : null;
    }
}