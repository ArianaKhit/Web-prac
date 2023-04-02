package com.Webprac.DAO;

import com.Webprac.jsons.JSONConverter;
import com.Webprac.tables.Coach;
import com.Webprac.tables.Sportsman;
import com.Webprac.tables.Team;
import com.Webprac.tables.TeamSportsmans;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamDAO extends CommonDAO<Team, Long> implements TeamDAOInterface {

    public TeamDAO(){
        super(Team.class);
    }

    @Override
    public List<Team> getAllByName(String teamName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Team> query = session.createQuery("FROM Team WHERE teamName LIKE :teamName", Team.class)
                    .setParameter("teamName", likeExpr(teamName));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public Team getByName(String teamName) {
        List<Team> candidates = this.getAllByName(teamName);
        return candidates == null ? null :
                candidates.size() == 1 ? candidates.get(0) : null;
    }

    @Override
    public List<Team> getByFilter(Filter filter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Team> criteriaQuery = builder.createQuery(Team.class);
            Root<Team> root = criteriaQuery.from(Team.class);

            List<Predicate> predicates = new ArrayList<>();
            if (filter.getTeamName() != null)
                predicates.add(builder.like(root.get("teamName"), likeExpr(filter.getTeamName())));

            if (filter.getSport() != null)
                predicates.add(builder.like(root.get("sport"), likeExpr(filter.getSport())));

            if (filter.getCountry() != null)
                predicates.add(builder.like(root.get("country"), likeExpr(filter.getCountry())));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}