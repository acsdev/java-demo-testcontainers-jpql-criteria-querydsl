package eti.query.demonstration.employee.entities;

import eti.query.demonstration.converters.LocalDateTimeAttributeConverter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMPLOYEES", schema = "HR", catalog = "")
public class EmployeeEntity {

    private long employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime hireDate;
    private String jobId;
    private Long salary;
    private Long commissionPct;
    private Long managerId;
    private Long departmentId;

    @Id
    @Column(name = "EMPLOYEE_ID")
    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Convert(converter = LocalDateTimeAttributeConverter.class)
    @Column(name = "HIRE_DATE")
    public LocalDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDateTime hireDate) {
        this.hireDate = hireDate;
    }

    @Basic
    @Column(name = "JOB_ID")
    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Basic
    @Column(name = "SALARY")
    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Basic
    @Column(name = "COMMISSION_PCT")
    public Long getCommissionPct() {
        return commissionPct;
    }

    public void setCommissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
    }

    @Basic
    @Column(name = "MANAGER_ID")
    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    @Basic
    @Column(name = "DEPARTMENT_ID")
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, lastName, email, phoneNumber, hireDate, jobId, salary, commissionPct,
                managerId, departmentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeEntity that = (EmployeeEntity) o;
        return employeeId == that.employeeId && Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(hireDate, that.hireDate) &&
                Objects.equals(jobId, that.jobId) && Objects.equals(salary, that.salary) &&
                Objects.equals(commissionPct, that.commissionPct) && Objects.equals(managerId, that.managerId) &&
                Objects.equals(departmentId, that.departmentId);
    }
}
