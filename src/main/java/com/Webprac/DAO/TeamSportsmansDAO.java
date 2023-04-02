package com.Webprac.DAO;


import com.Webprac.tables.TeamSportsmans;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeamSportsmansDAO extends CommonDAO<TeamSportsmans, Long> {

    public TeamSportsmansDAO(){
        super(TeamSportsmans.class);
    }


    public List<TeamSportsmans> getAllByTeam(Long teamID) {
        try (Session session = sessionFactory.openSession()) {
            Query<TeamSportsmans> query = session.createQuery("FROM TeamSportsmans WHERE teamID = :teamID", TeamSportsmans.class)
                    .setParameter("teamID", teamID);
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    public TeamSportsmans getByTeam(Long teamID) {
        List<TeamSportsmans> candidates = this.getAllByTeam(teamID);
        return candidates == null ? null :
                candidates.size() == 1 ? candidates.get(0) : null;
    }
}