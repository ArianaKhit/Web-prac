package com.Webprac.DAO;

import com.Webprac.tables.EventSportsmans;
import com.Webprac.tables.SportsmanCoaches;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SportsmanCoachesDAO extends CommonDAO<SportsmanCoaches, Long> {

    public SportsmanCoachesDAO(){
        super(SportsmanCoaches.class);
    }

    public List<SportsmanCoaches> getByIDs(Long coachID, Long sportsmanID) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SportsmanCoaches> criteriaQuery = builder.createQuery(SportsmanCoaches.class);
            Root<SportsmanCoaches> root = criteriaQuery.from(SportsmanCoaches.class);

            List<Predicate> predicates = new ArrayList<>();
            if (coachID != null)
                predicates.add(builder.equal(root.get("coach").get("id"), coachID));
            if (sportsmanID != null)
                predicates.add(builder.equal(root.get("sportsman").get("id"), sportsmanID));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}