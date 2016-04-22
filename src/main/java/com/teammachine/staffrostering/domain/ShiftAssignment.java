package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ShiftAssignment.
 */
@Entity
@Table(name = "shift_assignment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShiftAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "index_in_shift")
    private Integer indexInShift;

    @OneToOne
    @JoinColumn(unique = true)
    private Shift shift;

    @OneToOne
    @JoinColumn(unique = true)
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndexInShift() {
        return indexInShift;
    }

    public void setIndexInShift(Integer indexInShift) {
        this.indexInShift = indexInShift;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
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
        ShiftAssignment shiftAssignment = (ShiftAssignment) o;
        if(shiftAssignment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shiftAssignment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShiftAssignment{" +
            "id=" + id +
            ", indexInShift='" + indexInShift + "'" +
            '}';
    }
}
