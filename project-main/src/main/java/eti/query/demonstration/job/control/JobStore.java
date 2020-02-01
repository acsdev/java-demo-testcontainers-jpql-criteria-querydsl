package eti.query.demonstration.job.control;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import eti.query.demonstration.department.entities.DepartmentEntity;
import eti.query.demonstration.department.entities.dslmodel.QDepartmentEntity;
import eti.query.demonstration.department.entities.metamodel.DepartmentEntity_;
import eti.query.demonstration.employee.entities.EmployeeEntity;
import eti.query.demonstration.employee.entities.dslmodel.QEmployeeEntity;
import eti.query.demonstration.employee.entities.metamodel.EmployeeEntity_;
import eti.query.demonstration.job.entities.JobDomain;
import eti.query.demonstration.job.entities.JobEntity;
import eti.query.demonstration.job.entities.JobHistoryEntity;
import eti.query.demonstration.job.entities.dslmodel.QJobEntity;
import eti.query.demonstration.job.entities.dslmodel.QJobHistoryEntity;
import eti.query.demonstration.job.entities.metamodel.JobEntity_;
import eti.query.demonstration.job.entities.metamodel.JobHistoryEntityPK_;
import eti.query.demonstration.job.entities.metamodel.JobHistoryEntity_;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

public class JobStore {

    @PersistenceContext
    private EntityManager entityManager;

    void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<JobDomain> jpqlFindHistoryJobByJobId(String jobId) {
        StringBuffer sql = new StringBuffer();
        sql.append("      SELECT new eti.query.demonstration.job.entities.JobDomain(o.jobTitle, d.departmentName, e.firstName, h.pk.startDate, h.endDate)");
        sql.append("        FROM JobEntity o ");
        sql.append("        JOIN o.jobHistoryEntityList h");
        sql.append("        JOIN DepartmentEntity d ON d.departmentId = h.departmentId ");
        sql.append("        JOIN EmployeeEntity e ON e.employeeId = h.pk.employeeId ");
        sql.append("       WHERE o.jobId = :jobId");
        return entityManager
                .createQuery(sql.toString(), JobDomain.class)
                .setParameter("jobId", jobId).getResultList();
    }

    public List<JobDomain> criteriaFindHistoryJobByJobId(String jobId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<JobDomain> criteria = builder.createQuery(JobDomain.class);

        Root<JobEntity> job = criteria.from(JobEntity.class);
        ListJoin<JobEntity, JobHistoryEntity> history = job.join(JobEntity_.jobHistoryEntityList);
        Root<DepartmentEntity> department = criteria.from(DepartmentEntity.class);
        Root<EmployeeEntity> employee = criteria.from(EmployeeEntity.class);

        criteria.select(builder.construct(JobDomain.class,
                job.get(JobEntity_.jobId),
                department.get(DepartmentEntity_.departmentName),
                employee.get(EmployeeEntity_.firstName),
                history.get(JobHistoryEntity_.pk).get(JobHistoryEntityPK_.startDate),
                history.get(JobHistoryEntity_.endDate)));

        criteria.where(
                builder.equal(history.get(JobHistoryEntity_.jobId), job.get(JobEntity_.jobId)),
                builder.equal(department.get(DepartmentEntity_.departmentId), history.get(JobHistoryEntity_.departmentId)),
                builder.equal(employee.get(EmployeeEntity_.employeeId), history.get(JobHistoryEntity_.pk).get(JobHistoryEntityPK_.employeeId)),
                builder.equal(job.get(JobEntity_.jobId), jobId));

        return entityManager.createQuery(criteria).getResultList();
    }

    public List<JobDomain> queryDSLFindHistoryJobByJobId(String jobId) {

        QJobEntity job = QJobEntity.jobEntity;
        QJobHistoryEntity jobHistory = QJobHistoryEntity.jobHistoryEntity;

        QDepartmentEntity department = QDepartmentEntity.departmentEntity;
        QEmployeeEntity employee = QEmployeeEntity.employeeEntity;

        QBean<JobDomain> projection = Projections.bean(JobDomain.class,
                job.jobTitle.as("title"),
                department.departmentName.as("department"),
                employee.firstName.as("employee"),
                jobHistory.pk.startDate,
                jobHistory.endDate);

        return new JPAQueryFactory(this.entityManager)
                .select(projection)
                .from(job)
                .innerJoin(jobHistory)
                .on(jobHistory.jobId.eq(job.jobId))
                .innerJoin(department)
                .on(department.departmentId.eq(jobHistory.departmentId))
                .innerJoin(employee)
                .on(employee.employeeId.eq(jobHistory.pk.employeeId))
                .where(job.jobId.eq(jobId))
                .fetch();
    }

}
