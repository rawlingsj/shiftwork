package com.teammachine.staffrostering.domain;

import com.teammachine.staffrostering.domain.enumeration.DurationUnit;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A EmployeeAbsentReason.
 */
@Entity
@Table(name = "employee_absent_reason")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EmployeeAbsentReason implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "code validation failed.")   
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "default_duration")
    private Integer defaultDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_duration_unit")
    private DurationUnit defaultDurationUnit;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDefaultDuration() {
        return defaultDuration;
    }

    public void setDefaultDuration(Integer defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public DurationUnit getDefaultDurationUnit() {
        return defaultDurationUnit;
    }

    public void setDefaultDurationUnit(DurationUnit defaultDurationUnit) {
        this.defaultDurationUnit = defaultDurationUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeAbsentReason employeeAbsentReason = (EmployeeAbsentReason) o;
        if(employeeAbsentReason.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employeeAbsentReason.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EmployeeAbsentReason{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", defaultDuration='" + defaultDuration + "'" +
            ", defaultDurationUnit='" + defaultDurationUnit + "'" +
            '}';
    }
}
