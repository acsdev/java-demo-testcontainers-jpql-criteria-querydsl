package eti.query.demonstration;

import eti.query.demonstration.country.entities.CountryEntity;
import eti.query.demonstration.country.entities.metamodel.CountryEntity_;
import eti.query.demonstration.department.entities.DepartmentEntity;
import eti.query.demonstration.department.entities.metamodel.DepartmentEntity_;
import eti.query.demonstration.employee.entities.EmployeeEntity;
import eti.query.demonstration.employee.entities.metamodel.EmployeeEntity_;
import eti.query.demonstration.location.entities.LocationEntity;
import eti.query.demonstration.location.entities.metamodel.LocationEntity_;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

public class CriteriaUtil {

    public static void configMetaModel(EntityManager em) {
        Metamodel metamodel = em.getMetamodel();

        configCountryEntity( metamodel );
        configDepartmentEntity( metamodel );
        configLocationEntity( metamodel );
        configEmployeeEntity( metamodel );
    }

    public static void configCountryEntity(Metamodel metamodel) {
        EntityType<CountryEntity> entity = metamodel.entity(CountryEntity.class);

        CountryEntity_.countryId = getSingularField(entity, String.class, "countryId");
        CountryEntity_.countryName = getSingularField( entity, String.class, "countryName");
        CountryEntity_.regionId = getSingularField(entity, Long.class, "regionId");
    }

    public static void configDepartmentEntity(Metamodel metamodel) {
        EntityType<DepartmentEntity> entity = metamodel.entity(DepartmentEntity.class);
        DepartmentEntity_.departmentId = getSingularField(entity, Long.class, "departmentId");
        DepartmentEntity_.departmentName = getSingularField(entity, String.class, "departmentName");
        DepartmentEntity_.locationId = getSingularField(entity, Long.class, "locationId");
        DepartmentEntity_.managerId = getSingularField(entity, Long.class, "managerId");
    }

    public static void configLocationEntity(Metamodel metamodel) {
        EntityType<LocationEntity> entity = metamodel.entity(LocationEntity.class);
        LocationEntity_.locationId = getSingularField(entity, Long.class, "locationId");
        LocationEntity_.streetAddress = getSingularField(entity, String.class, "streetAddress");
    }

    public static void configEmployeeEntity(Metamodel metamodel) {
        EntityType<EmployeeEntity> entity = metamodel.entity(EmployeeEntity.class);
        EmployeeEntity_.employeeId = getSingularField(entity, Long.class, "employeeId");
        EmployeeEntity_.firstName = getSingularField(entity, String.class, "firstName");
    }

    private static <E, F> SingularAttribute<E, F> getSingularField(EntityType<E> entityType,
                                                                   Class<F> fieldType,
                                                                   String fieldName) {

        return (SingularAttribute<E, F>) entityType.getDeclaredSingularAttributes()
                .stream()
                .filter(a -> a.getName().equals(fieldName))
                .findAny()
                .get();

    }

}
