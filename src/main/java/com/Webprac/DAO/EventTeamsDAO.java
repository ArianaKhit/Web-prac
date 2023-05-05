package com.Webprac.DAO;

import com.Webprac.tables.EventSportsmans;
import com.Webprac.tables.EventTeams;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EventTeamsDAO extends CommonDAO<EventTeams, Long> {

    public EventTeamsDAO(){
        super(EventTeams.class);
    }

    public List<EventTeams> getByIDs(Long eventID, Long teamID) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<EventTeams> criteriaQuery = builder.createQuery(EventTeams.class);
            Root<EventTeams> root = criteriaQuery.from(EventTeams.class);

            List<Predicate> predicates = new ArrayList<>();
            if (eventID != null)
                predicates.add(builder.equal(root.get("event").get("id"), eventID));
            if (teamID != null)
                predicates.add(builder.equal(root.get("team").get("id"), teamID));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}