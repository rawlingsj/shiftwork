package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EmployeeShiftOffRequest.
 */
@Entity
@Table(name = "employee_shift_off_request")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmployeeShiftOffRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "weight")
    private Integer weight;

    @ManyToOne
    private Shift shift;

    @ManyToOne
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public EntityRefInfo getEmployee() {
        return employee != null ? new EntityRefInfo(employee.getId(), employee.getCode(), employee.getName()) : null;
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
        EmployeeShiftOffRequest employeeShiftOffRequest = (EmployeeShiftOffRequest) o;
        if(employeeShiftOffRequest.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employeeShiftOffRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeShiftOffRequest{" +
            "id=" + id +
            ", weight='" + weight + "'" +
            '}';
    }
}
