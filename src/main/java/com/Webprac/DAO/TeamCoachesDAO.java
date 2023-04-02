package com.Webprac.DAO;

import com.Webprac.tables.SportsmanCoaches;
import com.Webprac.tables.TeamCoaches;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamCoachesDAO extends CommonDAO<TeamCoaches, Long> {

    public TeamCoachesDAO(){
        super(TeamCoaches.class);
    }


    public List<TeamCoaches> getAllByTeam(Long teamID) {
        try (Session session = sessionFactory.openSession()) {
            Query<TeamCoaches> query = session.createQuery("FROM TeamCoaches WHERE teamID = :teamID", TeamCoaches.class)
                    .setParameter("teamID", teamID);
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    public TeamCoaches getByTeam(Long teamID) {
        List<TeamCoaches> candidates = this.getAllByTeam(teamID);
        return candidates == null ? null :
                candidates.size() == 1 ? candidates.get(0) : null;
    }
}