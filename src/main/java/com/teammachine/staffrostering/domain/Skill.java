package com.teammachine.staffrostering.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.validation.constraints.Pattern;
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

    @Column(name = "code")
    @Pattern(regexp = "/^[a-zA-Z0-9]*$/", message = "code validation failed.")     
    private String code;

    @ManyToMany(mappedBy = "skillList")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SkillProficiency> skillProficiencies = new HashSet<>();

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

    public Set<SkillProficiency> getSkillProficiencies() {
        return skillProficiencies;
    }

    public void setSkillProficiencies(Set<SkillProficiency> skillProficiencies) {
        this.skillProficiencies = skillProficiencies;
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
        if(skill.id == null || id == null) {
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
