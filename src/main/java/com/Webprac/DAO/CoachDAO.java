package com.Webprac.DAO;

import com.Webprac.tables.Coach;
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
public class CoachDAO extends CommonDAO<Coach, Long> implements CoachDAOInterface {

    public CoachDAO(){
        super(Coach.class);
    }

    @Override
    public List<Coach> getAllByName(String personName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Coach> query = session.createQuery("FROM Coach WHERE name LIKE :name", Coach.class)
                    .setParameter("name", likeExpr(personName));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public Coach getByName(String personName) {
        List<Coach> candidates = this.getAllByName(personName);
        return candidates == null ? null :
                candidates.size() == 1 ? candidates.get(0) : null;
    }


    @Override
    public List<Coach> getByFilter(Filter filter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Coach> criteriaQuery = builder.createQuery(Coach.class);
            Root<Coach> root = criteriaQuery.from(Coach.class);

            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null)
                predicates.add(builder.like(root.get("name"), likeExpr(filter.getName())));

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