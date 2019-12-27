package eti.query.demonstration.country.control;

import eti.query.demonstration.AbstractPersistenceTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.persistence.EntityManager;

@RunWith(JUnit4.class)
public class CountryStoreTest extends AbstractPersistenceTest {

    private CountryStore storage;

    @Override
    protected void setEntityManager(final EntityManager em) {
        storage = new CountryStore();
        storage.setEntityManager(em);
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
}
