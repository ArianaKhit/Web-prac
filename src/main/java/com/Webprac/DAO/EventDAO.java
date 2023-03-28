package com.Webprac.DAO;

import com.Webprac.tables.SportEvent;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EventDAO extends CommonDAO<SportEvent, Long> implements EventDAOInterface {

    public EventDAO(){
        super(SportEvent.class);
    }

    @Override
    public List<SportEvent> getAllByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<SportEvent> query = session.createQuery("FROM SportEvent WHERE title LIKE :title", SportEvent.class)
                    .setParameter("title", likeExpr(title));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public SportEvent getByTitle(String title) {
        List<SportEvent> candidates = this.getAllByTitle(title);
        return candidates == null ? null :
                candidates.size() == 1 ? candidates.get(0) : null;
    }


    @Override
    public List<SportEvent> getByFilter(Filter filter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SportEvent> criteriaQuery = builder.createQuery(SportEvent.class);
            Root<SportEvent> root = criteriaQuery.from(SportEvent.class);

            List<Predicate> predicates = new ArrayList<>();
            if (filter.getTitle() != null)
                predicates.add(builder.like(root.get("title"), likeExpr(filter.getTitle())));

            if (filter.getSport() != null)
                predicates.add(builder.like(root.get("sport"), likeExpr(filter.getSport())));

            if (filter.getTournament() != null)
                predicates.add(builder.like(root.get("tournament"), likeExpr(filter.getTournament())));

            if (filter.getVenue() != null)
                predicates.add(builder.like(root.get("venue"), likeExpr(filter.getVenue())));

            if (filter.getStartDate() != null)
                predicates.add(builder.greaterThanOrEqualTo(root.get("date"), filter.getStartDate()));

            if (filter.getEndDate() != null)
                predicates.add(builder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}