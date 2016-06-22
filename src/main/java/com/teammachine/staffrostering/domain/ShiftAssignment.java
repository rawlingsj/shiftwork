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

    @Column(name = "is_need_to_reassign")
    private Boolean isNeedToReassign;

    @Column(name = "is_dropped")
    private Boolean isDropped;

    @Column(name = "locked")
    private Boolean locked;

    @OneToOne
    @JoinColumn(unique = true)
    private Shift shift;

    @OneToOne
    @JoinColumn(unique = true)
    private Employee employee;

    @OneToMany(mappedBy = "shiftAssignment")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Task> taskLists = new HashSet<>();

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

    public Boolean isIsNeedToReassign() {
        return isNeedToReassign;
    }

    public void setIsNeedToReassign(Boolean isNeedToReassign) {
        this.isNeedToReassign = isNeedToReassign;
    }

    public Boolean isIsDropped() {
        return isDropped;
    }

    public void setIsDropped(Boolean isDropped) {
        this.isDropped = isDropped;
    }

    public Boolean isLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
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

    public Set<Task> getTaskLists() {
        return taskLists;
    }

    public void setTaskLists(Set<Task> tasks) {
        this.taskLists = tasks;
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
            ", isNeedToReassign='" + isNeedToReassign + "'" +
            ", isDropped='" + isDropped + "'" +
            ", locked='" + locked + "'" +
            '}';
    }
}
