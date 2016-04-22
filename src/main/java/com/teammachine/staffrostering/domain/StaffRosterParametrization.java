package com.teammachine.staffrostering.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A StaffRosterParametrization.
 */
@Entity
@Table(name = "staff_roster_parametrization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StaffRosterParametrization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private ShiftDate firstShiftDate;

    @OneToOne
    @JoinColumn(unique = true)
    private ShiftDate lastShiftDate;

    @OneToOne
    @JoinColumn(unique = true)
    private ShiftDate planningWindowStart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShiftDate getFirstShiftDate() {
        return firstShiftDate;
    }

    public void setFirstShiftDate(ShiftDate shiftDate) {
        this.firstShiftDate = shiftDate;
    }

    public ShiftDate getLastShiftDate() {
        return lastShiftDate;
    }

    public void setLastShiftDate(ShiftDate shiftDate) {
        this.lastShiftDate = shiftDate;
    }

    public ShiftDate getPlanningWindowStart() {
        return planningWindowStart;
    }

    public void setPlanningWindowStart(ShiftDate shiftDate) {
        this.planningWindowStart = shiftDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StaffRosterParametrization staffRosterParametrization = (StaffRosterParametrization) o;
        if(staffRosterParametrization.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, staffRosterParametrization.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StaffRosterParametrization{" +
            "id=" + id +
            '}';
    }
}
