package eti.query.demonstration.department.control;

import eti.query.demonstration.AbstractPersistenceTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.persistence.EntityManager;

@RunWith(JUnit4.class)
public class DepartmentStoreTest extends AbstractPersistenceTest {

    private DepartmentStore storage;

    @Override
    protected void setEntityManager(final EntityManager em) {
        storage = new DepartmentStore();
        storage.setEntityManager(em);
    }

    @Test
    public void testJpqlFindDepartamentWithManagerLocation() {
        Assert.assertEquals(11, storage.jpqlFindDepartamentWithManagerLocation().size());
    }

    @Test
    public void criteriaFindDepartamentWithManagerLocation() {
        Assert.assertEquals(11, storage.criteriaFindDepartamentWithManagerLocation().size());

    }


}
