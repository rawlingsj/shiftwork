package com.teammachine.staffrostering.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private Contract contract;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployeeDayOffRequest> dayOffRequests = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployeeDayOnRequest> dayOnRequests = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployeeShiftOffRequest> shiftOffRequests = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployeeShiftOnRequest> shiftOnRequests = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EmployeeLeaveAbsence> employeeLeaveAbsences = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Set<EmployeeDayOffRequest> getDayOffRequests() {
        return dayOffRequests;
    }

    public void setDayOffRequests(Set<EmployeeDayOffRequest> employeeDayOffRequests) {
        this.dayOffRequests = employeeDayOffRequests;
    }

    public Set<EmployeeDayOnRequest> getDayOnRequests() {
        return dayOnRequests;
    }

    public void setDayOnRequests(Set<EmployeeDayOnRequest> employeeDayOnRequests) {
        this.dayOnRequests = employeeDayOnRequests;
    }

    public Set<EmployeeShiftOffRequest> getShiftOffRequests() {
        return shiftOffRequests;
    }

    public void setShiftOffRequests(Set<EmployeeShiftOffRequest> employeeShiftOffRequests) {
        this.shiftOffRequests = employeeShiftOffRequests;
    }

    public Set<EmployeeShiftOnRequest> getShiftOnRequests() {
        return shiftOnRequests;
    }

    public void setShiftOnRequests(Set<EmployeeShiftOnRequest> employeeShiftOnRequests) {
        this.shiftOnRequests = employeeShiftOnRequests;
    }

    public Set<EmployeeLeaveAbsence> getEmployeeLeaveAbsences() {
        return employeeLeaveAbsences;
    }

    public void setEmployeeLeaveAbsences(Set<EmployeeLeaveAbsence> employeeLeaveAbsences) {
        this.employeeLeaveAbsences = employeeLeaveAbsences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if(employee.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
