package com.teammachine.staffrostering.domain;

import com.teammachine.staffrostering.domain.enumeration.DurationUnit;
import com.teammachine.staffrostering.domain.validators.AcceptDurationUnits;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Period;
import java.util.Objects;

/**
 * A Skill.
 */
@Entity
@Table(name = "skill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Skill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name = "code")
    private String code;

    @NotNull
    @Column(name = "rotation_period_value")
    private Integer rotationPeriodValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "rotation_period_unit")
    @AcceptDurationUnits(units = {DurationUnit.DAYS, DurationUnit.MONTHS, DurationUnit.YEARS})
    private DurationUnit rotationPeriodUnit;

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

    public Integer getRotationPeriodValue() {
        return rotationPeriodValue;
    }

    public void setRotationPeriodValue(int rotationPeriodValue) {
        this.rotationPeriodValue = rotationPeriodValue;
    }

    public DurationUnit getRotationPeriodUnit() {
        return rotationPeriodUnit;
    }

    public void setRotationPeriodUnit(DurationUnit rotationPeriodUnit) {
        this.rotationPeriodUnit = rotationPeriodUnit;
    }

    public Period getRotationPeriod() {
        if (rotationPeriodUnit != null && rotationPeriodValue != null) {
            switch (this.rotationPeriodUnit) {
                case DAYS:
                    return Period.ofDays(this.rotationPeriodValue);
                case MONTHS:
                    return Period.ofMonths(this.rotationPeriodValue);
                case YEARS:
                    return Period.ofYears(this.rotationPeriodValue);
                default:
                    throw new IllegalStateException("Only days, months, years are supported");
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Skill skill = (Skill) o;
        if (skill.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, skill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Skill{" +
            "id=" + id +
            ", code='" + code + "'" +
            '}';
    }
}
