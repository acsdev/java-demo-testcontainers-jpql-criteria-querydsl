package eti.query.demonstration.country.control;

import eti.query.demonstration.AbstractPersistenceTest;
import eti.query.demonstration.country.entities.CountryEntity;
import eti.query.demonstration.country.entities.metamodel.CountryEntity_;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.function.BiFunction;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

@RunWith(JUnit4.class)
public class CountryStoreTest extends AbstractPersistenceTest {

    private CountryStore storage;

    @Override
    protected void setEntityManager(final EntityManager em) {
        storage = new CountryStore();
        storage.setEntityManager(em);
    }

    @Before
    public void init() {

        super.init();

        this.configMetaModel();
    }

    @Test
    public void testJpqlFindAll() {
        Assert.assertEquals(25, storage.jpqlFindAll().size());
    }

    @Test
    public void testCriteriaFindAll() {
        Assert.assertEquals(25, storage.criteriaFindAll().size());
    }

    @Test
    public void testQueryDSLFindAll() {
        Assert.assertEquals(25, storage.queryDSLFindAll().size());
    }

    private void configMetaModel() {

        BiFunction<EntityType<CountryEntity>, String, Attribute> bfAttr = (entity, field) -> entity.getAttributes()
                .stream()
                .filter(a -> a.getName().equals(field))
                .findAny()
                .get();

        Function<Attribute, SingularAttribute<CountryEntity, String>> fToStr =
                attribute -> (SingularAttribute<CountryEntity, String>) attribute;

        Function<Attribute, SingularAttribute<CountryEntity, Long>> fToLng =
                attribute -> (SingularAttribute<CountryEntity, Long>) attribute;

        Metamodel metamodel = super.getEntityManager().getMetamodel();
        EntityType<CountryEntity> entity = metamodel.entity(CountryEntity.class);
        CountryEntity_.countryId = fToStr.apply(bfAttr.apply(entity, "countryId"));
        CountryEntity_.countryName = fToStr.apply(bfAttr.apply(entity, "countryName"));
        CountryEntity_.regionId = fToLng.apply(bfAttr.apply(entity, "regionId"));
    }

}
