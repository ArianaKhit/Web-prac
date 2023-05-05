package com.Webprac.DAO;


import com.Webprac.tables.EventSportsmans;
import com.Webprac.tables.TeamSportsmans;
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
public class TeamSportsmansDAO extends CommonDAO<TeamSportsmans, Long> {

    public TeamSportsmansDAO(){
        super(TeamSportsmans.class);
    }


    public List<TeamSportsmans> getByIDs(Long teamID, Long sportsmanID) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<TeamSportsmans> criteriaQuery = builder.createQuery(TeamSportsmans.class);
            Root<TeamSportsmans> root = criteriaQuery.from(TeamSportsmans.class);

            List<Predicate> predicates = new ArrayList<>();
            if (teamID != null)
                predicates.add(builder.equal(root.get("team").get("id"), teamID));
            if (sportsmanID != null)
                predicates.add(builder.equal(root.get("sportsman").get("id"), sportsmanID));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}