package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A EmployeeLeaveAbsence.
 */
@Entity
@Table(name = "employee_leave_absence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmployeeLeaveAbsence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "absent_from")
    private ZonedDateTime absentFrom;

    @Column(name = "absent_to")
    private ZonedDateTime absentTo;

    @ManyToOne
    private EmployeeAbsentReason reason;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAbsentFrom() {
        return absentFrom;
    }

    public void setAbsentFrom(ZonedDateTime absentFrom) {
        this.absentFrom = absentFrom;
    }

    public ZonedDateTime getAbsentTo() {
        return absentTo;
    }

    public void setAbsentTo(ZonedDateTime absentTo) {
        this.absentTo = absentTo;
    }

    public EmployeeAbsentReason getReason() {
        return reason;
    }

    public void setReason(EmployeeAbsentReason employeeAbsentReason) {
        this.reason = employeeAbsentReason;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeLeaveAbsence employeeLeaveAbsence = (EmployeeLeaveAbsence) o;
        if(employeeLeaveAbsence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employeeLeaveAbsence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeLeaveAbsence{" +
            "id=" + id +
            ", absentFrom='" + absentFrom + "'" +
            ", absentTo='" + absentTo + "'" +
            '}';
    }
}
