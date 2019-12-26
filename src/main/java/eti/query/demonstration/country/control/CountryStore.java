package eti.query.demonstration.country.control;

import com.querydsl.jpa.impl.JPAQueryFactory;
import eti.query.demonstration.country.entities.CountryEntity;
import eti.query.demonstration.country.entities.metamodel.QCountryEntity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class CountryStore {

    @PersistenceContext
    private EntityManager entityManager;

    void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<CountryEntity> jpqlFindAll() {
        return entityManager.createQuery("SELECT o FROM CountryEntity o", CountryEntity.class).getResultList();
    }

    public List<CountryEntity> criteriaFindAll() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CountryEntity> criteria = builder.createQuery(CountryEntity.class);
        return entityManager.createQuery( criteria ).getResultList();
    }

    public List<CountryEntity> queryDSLFindAll() {
        return new JPAQueryFactory(this.entityManager).selectFrom(QCountryEntity.countryEntity).fetch();
    }
}
