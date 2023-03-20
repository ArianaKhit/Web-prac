package com.Webprac.DAO;

import com.Webprac.tables.Sportsman;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import com.Webprac.DAO.SportsmanDAOInterface;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SportsmanDAO extends CommonDAO<Sportsman, Long> implements SportsmanDAOInterface {

    public SportsmanDAO(){
        super(Sportsman.class);
    }

    @Override
    public List<Sportsman> getAllPersonByName(String personName) {
        try (Session session = sessionFactory.openSession()) {
            Query<Sportsman> query = session.createQuery("FROM Sportsman WHERE name LIKE :name", Sportsman.class)
                    .setParameter("name", likeExpr(personName));
            return query.getResultList().size() == 0 ? null : query.getResultList();
        }
    }

    @Override
    public Sportsman getSinglePersonByName(String personName) {
        List<Sportsman> candidates = this.getAllPersonByName(personName);
        return candidates == null ? null :
                candidates.size() == 1 ? candidates.get(0) : null;
    }

    @Override
    public String getYearsOfLife(Sportsman person) {
        String ret = "";
        ret += person.getBirthDate() != null ? person.getBirthDate().toString() + '-' : " no birth date";
        return ret;
    }

    @Override
    public List<Sportsman> getByFilter(Filter filter) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Sportsman> criteriaQuery = builder.createQuery(Sportsman.class);
            Root<Sportsman> root = criteriaQuery.from(Sportsman.class);

            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null)
                predicates.add(builder.like(root.get("name"), likeExpr(filter.getName())));

            if (predicates.size() != 0)
                criteriaQuery.where(predicates.toArray(new Predicate[0]));

            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    private String likeExpr(String param) {
        return "%" + param + "%";
    }
}