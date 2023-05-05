package com.Webprac.DAO;

import com.Webprac.tables.Coach;
import com.Webprac.tables.EventSportsmans;
import com.Webprac.tables.SportEvent;
import com.Webprac.tables.Sportsman;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EventSportsmansDAO extends CommonDAO<EventSportsmans, Long> {

    public EventSportsmansDAO(){
        super(EventSportsmans.class);
    }


    public List<EventSportsmans> getByIDs(Long eventID, Long sportsmanID) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<EventSportsmans> criteriaQuery = builder.createQuery(EventSportsmans.class);
            Root<EventSportsmans> root = criteriaQuery.from(EventSportsmans.class);

            List<Predicate> predicates = new ArrayList<>();
            if (eventID != null)
                predicates.add(builder.equal(root.get("event").get("id"), eventID));
            if (sportsmanID != null)
                predicates.add(builder.equal(root.get("sportsman").get("id"), sportsmanID));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

}