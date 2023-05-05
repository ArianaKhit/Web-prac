package com.Webprac.DAO;

import com.Webprac.tables.SportsmanCoaches;
import com.Webprac.tables.TeamCoaches;
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
public class TeamCoachesDAO extends CommonDAO<TeamCoaches, Long> {

    public TeamCoachesDAO(){
        super(TeamCoaches.class);
    }


    public List<TeamCoaches> getByIDs(Long coachID, Long teamID) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<TeamCoaches> criteriaQuery = builder.createQuery(TeamCoaches.class);
            Root<TeamCoaches> root = criteriaQuery.from(TeamCoaches.class);

            List<Predicate> predicates = new ArrayList<>();
            if (coachID != null)
                predicates.add(builder.equal(root.get("coach").get("id"), coachID));
            if (teamID != null)
                predicates.add(builder.equal(root.get("team").get("id"), teamID));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}